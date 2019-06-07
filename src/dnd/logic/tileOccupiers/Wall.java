package dnd.logic.tileOccupiers;

import dnd.logic.LogicException;
import dnd.logic.Point;
import dnd.logic.board.Board;
import dnd.logic.random_generator.RandomGenerator;

public class Wall implements TileOccupier {
    @Override
    public char toTileChar() {
        return '#';
    }

    @Override
    public Object accept(TileVisitor visitor, Object state) throws LogicException {
        return visitor.visit(this, state);
    }

    @Override
    public boolean isFree() {
        return false;
    }

    @Override
    public TileOccupier clone(Point position, RandomGenerator randomGenerator, Board board) {
        return new Wall();
    }

    @Override
    public String toString() {
        return toTileChar() + "";
    }
}
