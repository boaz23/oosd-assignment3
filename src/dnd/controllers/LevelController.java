package dnd.controllers;

import dnd.View;
import dnd.dto.units.PlayerDTO;
import dnd.logic.board.Board;
import dnd.logic.board.BoardSquare;
import dnd.logic.player.Player;

// TODO: implement methods
public class LevelController {
    private Player player;
    private Board board;

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
