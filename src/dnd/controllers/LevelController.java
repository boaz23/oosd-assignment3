package dnd.controllers;

import dnd.cli.view.View;
import dnd.logic.LevelEndObserver;
import dnd.dto.units.PlayerDTO;
import dnd.logic.board.Board;
import dnd.logic.board.BoardSquare;
import dnd.logic.player.Player;
import dnd.logic.random_generator.RandomGenerator;

import java.io.File;

// TODO: implement methods
public class LevelController implements LevelEndObserver {
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
            // TODO: load level
        }
    }

    public char[][] getBoard() {
        BoardSquare[][] boardSquares = board.getBoard();
        char[][] tiles = new char[boardSquares.length][boardSquares[0].length];
        for (int i = 0; i < boardSquares.length; i++) {
            for (int j = 0; j < boardSquares[i].length; j++) {
                tiles[i][j] = boardSquares[i][j].getTileOccupier().getTileChar();
            }
        }
        return tiles;
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
        return new File(this.levelsDirPath + "\\Level_" + level + ".txt");
    }
}
