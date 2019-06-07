package dnd.controllers;

import dnd.GameEventObserver;
import dnd.cli.view.View;
import dnd.controllers.tile_occupiers_factories.EnemyFactory;
import dnd.controllers.tile_occupiers_factories.InanimateFactory;
import dnd.controllers.tile_occupiers_factories.TileOccupierFactory;
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
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.TileFactoryImpl;
import dnd.logic.tileOccupiers.TileOccupier;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LevelController implements LevelEndObserver {
    private Map<Character, TileOccupierFactory> tilesFactory;

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
        initFactoriesMap();
    }

    private void initFactoriesMap() {
        tilesFactory = new HashMap<>();
        addEnemyFactories(AvailableMonsters.Monsters);
        addEnemyFactories(AvailableTraps.Traps);
        addInanimateFactories(AvailableInanimate.Inanimates);
    }

    private <T extends Enemy> void addEnemyFactories(T[] enemies) {
        for (Enemy enemy : enemies) {
            tilesFactory.put(enemy.getTileChar(), new EnemyFactory(enemy));
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void addInanimateFactories(TileOccupier[] inanimates) {
        for (TileOccupier inanimate : inanimates) {
            tilesFactory.put(inanimate.toTileChar(), new InanimateFactory(inanimate));
        }
    }

    public void setView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("view is null");
        }

        this.view = view;
    }

    private boolean hasLevel(File levelFile) {
        return levelFile.exists();
    }

    public ActionController loadLevel(int level) {
        File levelFile = getLevelFile(level);
        if (!hasLevel(levelFile)) {
            view.onGameWin();
            return null;
        }
        else {
            return loadLevel(levelFile);
        }
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

    public PlayerDTO[] getPlayerChoices() {
        Player[] availablePlayers = AvailablePlayers.Players;
        PlayerDTO[] playerChoices = new PlayerDTO[availablePlayers.length];
        for (int i = 0; i < availablePlayers.length; i++) {
            playerChoices[i] = (PlayerDTO)availablePlayers[i].createDTO();
        }

        return playerChoices;
    }

    public PlayerDTO choosePlayer(int choice) {
        if (0 >= choice | choice > AvailablePlayers.Players.length) {
            return null;
        }

        Player player = AvailablePlayers.Players[choice - 1];
        this.player = player;
        tilesFactory.put(player.toTileChar(), this.new PlayerFactory());
        return (PlayerDTO)player.createDTO();
    }

    public PlayerDTO getPlayer() {
        return (PlayerDTO)player.createDTO();
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

    private File getLevelFile(int level) {
        return new File(levelsDirPath + "Level " + level + ".txt");
    }

    private ActionController loadLevel(File levelFile) {
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

    private ActionController loadLevel(BufferedReader reader) throws IOException {
        InitializableBoard board = new BoardImpl(new TileFactoryImpl());
        LevelFlow levelFlow = new LevelFlow();
        this.board = board;

        player.addDeathObserver(board);
        player.addDeathObserver(levelFlow);

        PositionsMatrix positionsMatrix = parseFile(board, levelFlow, reader);
        board.setBoard(positionsMatrix);

        board.addLevelEndObserver(this);
        board.addLevelEndObserver(levelFlow);
        levelFlow.insertTickObserverAsFirst(player);

        return new ActionController(board.getPlayer(), levelFlow);
    }

    private PositionsMatrix parseFile(InitializableBoard board, LevelFlow levelFlow, BufferedReader reader) throws IOException {
        PositionsMatrix positionsMatrix = null;

        String line = reader.readLine();
        if (line != null) {
            int lineNum = 0;
            int boardWidth = line.length();
            PositionMatrixBuilder positionMatrixBuilder = new PositionMatrixBuilder(boardWidth);
            parseFileLine(line, lineNum, positionMatrixBuilder, board, levelFlow);

            while ((line = reader.readLine()) != null) {
                if (line.length() != boardWidth) {
                    throw new RuntimeException("Invalid board, not a matrix.");
                }

                lineNum++;
                parseFileLine(line, lineNum, positionMatrixBuilder, board, levelFlow);
            }

            positionsMatrix = positionMatrixBuilder.build();
        }

        return positionsMatrix;
    }

    private void parseFileLine(String line, int lineNum, PositionMatrixBuilder positionMatrixBuilder, InitializableBoard board, LevelFlow levelFlow) {
        positionMatrixBuilder.addRow();
        for (int i = 0; i < line.length(); i++) {
            char tileChar = line.charAt(i);
            if (!tilesFactory.containsKey(tileChar)) {
                throw new RuntimeException("invalid tile character");
            }

            TileOccupier tileOccupier = tilesFactory.get(tileChar).createTileOccupier(
                positionMatrixBuilder.getPosition(lineNum, i),
                randomGenerator,
                board,
                levelFlow,
                view);
            positionMatrixBuilder.set(lineNum, i, tileOccupier);
        }
    }

    private class PlayerFactory extends UnitFactory {
        @Override
        public TileOccupier createTileOccupier(
            Point position,
            RandomGenerator randomGenerator,
            InitializableBoard board,
            LevelFlow levelFlow,
            GameEventObserver gameEventObserver) {
            Player player = (Player)LevelController.this.player.clone(position, randomGenerator, board);
            LevelController.this.player = player;
            board.setPlayer(player);
            super.registerEventObservers(player, board, levelFlow, gameEventObserver);
            return player;
        }
    }
}
