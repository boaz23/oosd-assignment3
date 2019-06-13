package dnd.logic.board;

import dnd.logic.Point;
import dnd.logic.PositionOutOfBoundsException;
import dnd.logic.tileOccupiers.TileOccupier;

/**
 * Encapsulates the access to the game board matrix (It doesn't have to be an actual matrix in memory, it's a symbolic name).
 */
public class PositionsMatrix {
    private final BoardSquare[][] boardSquares;

    PositionsMatrix(BoardSquare[][] boardSquares) {
        if (boardSquares == null) {
            throw new IllegalArgumentException("board squares is null");
        }
        if (boardSquares.length == 0 || boardSquares[0].length == 0) {
            throw new IllegalArgumentException("board squares be a valid matrix.");
        }

        this.boardSquares = boardSquares;
    }

    TileOccupier get(Point position) throws PositionOutOfBoundsException {
        Indices indices = getIndices(position);
        return boardSquares[indices.i][indices.j].getTileOccupier();
    }

    void set(Point position, TileOccupier tileOccupier) throws PositionOutOfBoundsException {
        if (tileOccupier == null) {
            throw new IllegalArgumentException("tile occupier is null");
        }

        Indices indices = getIndices(position);
        boardSquares[indices.i][indices.j].setTileOccupier(tileOccupier);
    }

    /**
     * Returns the last x point on the x axis
     */
    int lastX() {
        return boardSquares[0].length - 1;
    }

    /**
     * Returns the last y point on the y axis
     */
    int lastY() {
        return boardSquares.length - 1;
    }

    /**
     * Gets the tile occupier at the specified indices (required for printing the board).
     */
    public TileOccupier get(int i, int j) {
        return boardSquares[i][j].getTileOccupier();
    }

    /**
     * Returns the numbers of rows of the actual matrix stored by this object (required for printing the board).
     */
    public int rows() {
        return boardSquares.length;
    }

    /**
     * Returns the numbers of columns of the actual matrix stored by this object (required for printing the board).
     */
    public int columns() {
        return boardSquares[0].length;
    }

    private Indices getIndices(Point position) throws PositionOutOfBoundsException {
        Indices indices = new Indices(position);
        validateIndices(indices);
        return indices;
    }

    private void validateIndices(Indices indices) throws PositionOutOfBoundsException {
        if (indices.i < 0 | indices.i >= rows() |
            indices.j < 0 | indices.j >= columns()) {
            throw new PositionOutOfBoundsException();
        }
    }

    /**
     * Encapsulates the logic of mapping a position point to their indices on the actual matrix stored by the matrix object
     */
    static class Indices {
        private final int i;
        private final int j;

        Indices(Point position) {
            if (position == null) {
                throw new IllegalArgumentException("position is nul.");
            }

            i = position.getY();
            j = position.getX();
        }

        static Point getPosition(int row, int column) {
            Indices indices = new Indices(new Point(row, column));
            return new Point(indices.i, indices.j);
        }
    }
}
