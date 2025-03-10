package dnd.logic.board;

import dnd.logic.Point;
import dnd.logic.tileOccupiers.TileOccupier;

import java.util.ArrayList;

/**
 * Encapsulates the logic of building a game board matrix when loading a level
 */
public class PositionMatrixBuilder {
    private final ArrayList<BoardSquare[]> boardSquares;
    private final int columns;

    public PositionMatrixBuilder(int columns) {
        boardSquares = new ArrayList<>();
        this.columns = columns;
    }

    public void addRow() {
        boardSquares.add(new BoardSquare[columns]);
    }

    public Point getPosition(int row, int column) {
        return PositionsMatrix.Indices.getPosition(row, column);
    }

    public void set(int row, int column, TileOccupier tileOccupier) {
        boardSquares.get(row)[column] = new BoardSquare(tileOccupier);
    }

    public PositionsMatrix build() {
        return new PositionsMatrix(boardSquares.toArray(new BoardSquare[][]{}));
    }
}
