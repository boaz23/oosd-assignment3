package dnd.logic.tileOccupiers;

import dnd.logic.LogicException;
import dnd.logic.MoveResult;
import dnd.logic.Point;
import dnd.logic.board.Board;
import dnd.logic.random_generator.RandomGenerator;

public class FreeTile implements TileOccupier {
    public static final char TileChar = '.';

    @Override
    public char toTileChar() {
        return TileChar;
    }

    @Override
    public MoveResult accept(TileVisitor visitor) throws LogicException {
        return visitor.visit(this);
    }

    @Override
    public boolean isFree() {
        return true;
    }

    @Override
    public TileOccupier clone(Point position, RandomGenerator randomGenerator, Board board) {
        return new FreeTile();
    }

    @Override
    public String toString() {
        return toTileChar() + "";
    }
}
