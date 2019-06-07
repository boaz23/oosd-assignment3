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
import dnd.logic.board.Board;
import dnd.logic.board.BoardImpl;
import dnd.logic.board.BoardSquare;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.TileFactoryImpl;
import dnd.logic.tileOccupiers.TileOccupier;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelController implements LevelEndObserver {
    private Map<Character, TileOccupierFactory> tilesFactory;

    private final RandomGenerator randomGenerator;
    private final String levelsDirPath;
    private Player player;
    private Board board;
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
        for (int i = 0; i < enemies.length; i++) {
            Enemy enemy = enemies[i];
            tilesFactory.put(enemy.getTileChar(), new EnemyFactory(enemy));
        }
    }

    private void addInanimateFactories(TileOccupier[] inanimates) {
        for (int i = 0; i < inanimates.length; i++) {
            TileOccupier inanimte = inanimates[i];
            tilesFactory.put(inanimte.toTileChar(), new InanimateFactory(inanimte));
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
        File levelFile = this.getLevelFile(level);
        if (!this.hasLevel(levelFile)) {
            this.view.onGameWin();
            return null;
        }
        else {
            return this.loadLevel(levelFile);
        }
    }

    public char[][] getBoard() {
        BoardSquare[][] boardSquares = board.getBoard();
        char[][] tiles = new char[boardSquares.length][boardSquares[0].length];
        for (int i = 0; i < boardSquares.length; i++) {
            for (int j = 0; j < boardSquares[i].length; j++) {
                tiles[i][j] = boardSquares[i][j].getTileOccupier().toTileChar();
            }
        }
        return tiles;
    }

    public PlayerDTO[] getPlayerChoises() {
        Player[] avaiablePlayers = AvailablePlayers.Players;
        PlayerDTO[] playerChoises = new PlayerDTO[avaiablePlayers.length];
        for (int i = 0; i < avaiablePlayers.length; i++) {
            playerChoises[i] = (PlayerDTO)avaiablePlayers[i].createDTO();
        }

        return playerChoises;
    }

    public PlayerDTO choosePlayer(int choise) {
        if (0 >= choise | choise > AvailablePlayers.Players.length) {
            return null;
        }

        Player player = AvailablePlayers.Players[choise - 1];
        this.player = player;
        this.tilesFactory.put(player.toTileChar(), this.new PlayerFactory(player));
        return (PlayerDTO)player.createDTO();
    }

    public PlayerDTO getPlayer() {
        return (PlayerDTO)this.player.createDTO();
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
        return new File(this.levelsDirPath + "Level " + level + ".txt");
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
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ActionController loadLevel(BufferedReader reader) throws IOException {
        BoardImpl board = new BoardImpl(new TileFactoryImpl());
        LevelFlow levelFlow = new LevelFlow();
        this.board = board;

        this.player.addDeathObserver(board);
        this.player.addDeathObserver(levelFlow);

        List<BoardSquare[]> boardSquares = parseFile(board, levelFlow, reader);

        board.setBoard(boardSquares.toArray(new BoardSquare[][] {}));
        board.addLevelEndObserver(this);
        board.addLevelEndObserver(levelFlow);
        levelFlow.addTickObserver(this.player);

        return new ActionController(board.getPlayer(), levelFlow);
    }

    private List<BoardSquare[]> parseFile(BoardImpl board, LevelFlow levelFlow, BufferedReader reader) throws IOException {
        ArrayList<BoardSquare[]> boardSquares = new ArrayList<>();

        String line = reader.readLine();
        if (line != null) {
            int lineNum = 0;
            int boardWidth = line.length();
            boardSquares.add(parseFileLine(line, lineNum, board, levelFlow));

            while ((line = reader.readLine()) != null) {
                if (line.length() != boardWidth) {
                    throw new RuntimeException("Invalid board, not a matrix.");
                }

                lineNum++;
                boardSquares.add(parseFileLine(line, lineNum, board, levelFlow));
            }
        }

        return boardSquares;
    }

    private BoardSquare[] parseFileLine(String line, int lineNum, BoardImpl board, LevelFlow levelFlow) {
        BoardSquare[] squares = new BoardSquare[line.length()];
        for (int i = 0; i < line.length(); i++) {
            char tileChar = line.charAt(i);
            if (!tilesFactory.containsKey(tileChar)) {
                throw new RuntimeException("invalid tile character");
            }

            squares[i] = new BoardSquare(tilesFactory.get(tileChar).createTileOccupier(
                    new Point(lineNum, i),
                    this.randomGenerator,
                    board,
                    levelFlow,
                    this.view));
        }

        return squares;
    }

    private class PlayerFactory extends UnitFactory {
        private final Player player;

        PlayerFactory(Player player) {
            this.player = player;
        }

        @Override
        public TileOccupier createTileOccupier(
                Point position,
                RandomGenerator randomGenerator,
                BoardImpl board,
                LevelFlow levelFlow,
                GameEventObserver gameEventObserver) {
            Player player = (Player)this.player.clone(position, randomGenerator, board);
            LevelController.this.player = player;
            board.setPlayer(player);
            super.registerEventObservers(player, board, levelFlow, gameEventObserver);
            return player;
        }
    }
}
