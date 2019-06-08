package dnd.controllers;

import dnd.GameEventObserver;
import dnd.GameException;
import dnd.cli.view.View;
import dnd.controllers.tile_occupiers_factories.FactoriesMapBuilder;
import dnd.controllers.tile_occupiers_factories.TileOccupierFactory;
import dnd.controllers.tile_occupiers_factories.UnitFactory;
import dnd.dto.units.PlayerDTO;
import dnd.logic.LevelEndObserver;
import dnd.logic.LevelFlow;
import dnd.logic.Point;
import dnd.logic.available_units.AvailableInanimate;
import dnd.logic.available_units.AvailableMonsters;
import dnd.logic.available_units.AvailablePlayers;
import dnd.logic.available_units.AvailableTraps;
import dnd.logic.board.BoardImpl;
import dnd.logic.board.InitializableBoard;
import dnd.logic.board.PositionMatrixBuilder;
import dnd.logic.board.PositionsMatrix;
import dnd.logic.player.Player;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.TileFactoryImpl;
import dnd.logic.tileOccupiers.TileOccupier;

import java.io.*;
import java.util.Map;

public class LevelController implements LevelEndObserver {
    private final RandomGenerator randomGenerator;
    private final String levelsDirPath;
    private Player player;
    private InitializableBoard board;
    private View view;

    public LevelController(String levelsDirPath, RandomGenerator randomGenerator) {
        if (randomGenerator == null) {
            throw new IllegalArgumentException("random generator is null.");
        }

        this.randomGenerator = randomGenerator;
        this.levelsDirPath = levelsDirPath;
    }

    public void setView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("view is null");
        }

        this.view = view;
    }

    @Override
    public void onDeath(Player player) {
        if (view != null) {
            view.onGameLose();
        }
    }

    @Override
    public void onLevelComplete() {
        if (view != null) {
            view.onLevelComplete();
        }
    }

    public PlayerDTO[] getPlayerChoices() {
        Player[] availablePlayers = AvailablePlayers.Players;
        PlayerDTO[] playerChoices = new PlayerDTO[availablePlayers.length];
        for (int i = 0; i < availablePlayers.length; i++) {
            playerChoices[i] = availablePlayers[i].createDTO();
        }

        return playerChoices;
    }

    public PlayerDTO choosePlayer(int choice) {
        if (0 >= choice | choice > AvailablePlayers.Players.length) {
            return null;
        }

        Player player = AvailablePlayers.Players[choice - 1];
        this.player = player.clone();
        return player.createDTO();
    }

    public PlayerDTO getPlayer() {
        return player.createDTO();
    }

    public char[][] getBoard() {
        PositionsMatrix boardSquares = board.getBoard();
        int rows = boardSquares.rows();
        int columns = boardSquares.columns();
        char[][] tiles = new char[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                tiles[i][j] = boardSquares.get(i, j).toTileChar();
            }
        }
        return tiles;
    }

    public ActionController loadLevel(int level) throws GameException {
        File levelFile = getLevelFile(level);
        if (hasLevel(levelFile)) {
            return loadLevel(levelFile);
        }
        else {
            view.onGameWin();
            return null;
        }
    }

    private File getLevelFile(int level) {
        return new File(levelsDirPath + "Level " + level + ".txt");
    }

    private boolean hasLevel(File levelFile) {
        return levelFile.exists();
    }

    private ActionController loadLevel(File levelFile) throws GameException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(levelFile));
            return loadLevel(reader);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("file not found", e);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("unexpected IO exception when loading level file", e);
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ActionController loadLevel(BufferedReader reader) throws IOException, GameException {
        InitializableBoard board = new BoardImpl(new TileFactoryImpl());
        LevelFlow levelFlow = new LevelFlow();

        levelFlow.addTickObserver(player);
        board.addLevelEndObserver(this);
        board.addLevelEndObserver(levelFlow);
        this.board = board;

        Map<Character, TileOccupierFactory> tileOccupierFactoryMap = prepareFactoriesMap(board, levelFlow);
        PositionsMatrix positionsMatrix = parseFile(reader, tileOccupierFactoryMap);
        board.setBoard(positionsMatrix);
        return new ActionController(board.getPlayer(), levelFlow);
    }

    private Map<Character, TileOccupierFactory> prepareFactoriesMap(InitializableBoard board, LevelFlow levelFlow) {
        Map<Character, TileOccupierFactory> tileOccupierFactoryMap = new FactoriesMapBuilder()
            .addEnemyFactories(AvailableMonsters.Monsters, randomGenerator, board, levelFlow, view)
            .addEnemyFactories(AvailableTraps.Traps, randomGenerator, board, levelFlow, view)
            .addInanimatesFactories(AvailableInanimate.Inanimates)
            .build();
        addPlayerFactory(board, levelFlow, tileOccupierFactoryMap);
        return tileOccupierFactoryMap;
    }

    private void addPlayerFactory(
        InitializableBoard board,
        LevelFlow levelFlow,
        Map<Character, TileOccupierFactory> tileOccupierFactoryMap
    ) {
        tileOccupierFactoryMap.put(player.toTileChar(), this.new PlayerFactory(randomGenerator, board, levelFlow, view));
    }

    private PositionsMatrix parseFile(
        BufferedReader reader,
        Map<Character, TileOccupierFactory> tileOccupierFactoryMap
    ) throws IOException, GameException {
        PositionsMatrix positionsMatrix = null;

        String line = reader.readLine();
        if (line != null) {
            int lineNum = 0;
            int boardWidth = line.length();
            PositionMatrixBuilder positionMatrixBuilder = new PositionMatrixBuilder(boardWidth);
            parseFileLine(line, lineNum, positionMatrixBuilder, tileOccupierFactoryMap);

            while ((line = reader.readLine()) != null) {
                if (line.length() != boardWidth) {
                    throw new GameException("Invalid board, not a matrix.");
                }

                lineNum++;
                parseFileLine(line, lineNum, positionMatrixBuilder, tileOccupierFactoryMap);
            }

            positionsMatrix = positionMatrixBuilder.build();
        }

        return positionsMatrix;
    }

    private void parseFileLine(
        String line,
        int lineNum,
        PositionMatrixBuilder positionMatrixBuilder,
        Map<Character, TileOccupierFactory> tileOccupierFactoryMap
    ) throws GameException {
        positionMatrixBuilder.addRow();
        for (int i = 0; i < line.length(); i++) {
            char tileChar = line.charAt(i);
            if (!tileOccupierFactoryMap.containsKey(tileChar)) {
                throw new GameException("invalid tile character");
            }

            Point position = positionMatrixBuilder.getPosition(lineNum, i);
            TileOccupier tileOccupier = tileOccupierFactoryMap.get(tileChar).createTileOccupier(position);
            positionMatrixBuilder.set(lineNum, i, tileOccupier);
        }
    }

    private class PlayerFactory extends UnitFactory {
        PlayerFactory(
            RandomGenerator randomGenerator,
            InitializableBoard board,
            LevelFlow levelFlow,
            GameEventObserver gameEventObserver) {
            super(randomGenerator, board, levelFlow, gameEventObserver);
        }

        @Override
        public TileOccupier createTileOccupier(Point position) {
            Player player = LevelController.this.player;
            board.setPlayer(player);
            prepareForNewLevel(player, position);
            return player;
        }
    }
}
