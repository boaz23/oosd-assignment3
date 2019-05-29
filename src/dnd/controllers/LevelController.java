package dnd.controllers;

import dnd.logic.board.Board;
import dnd.logic.board.BoardSquare;
import dnd.logic.player.Player;

public class LevelController {
    private Player player;
    private Board board;
    private View view;

    public ActionController loadLevel(int level) {
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

    public PlayerDTO getPlayer() {
        
    }
}
