package dnd.logic.tileOccupiers;

import dnd.logic.LogicException;
import dnd.logic.Point;
import dnd.logic.board.Board;
import dnd.logic.random_generator.RandomGenerator;

public class DeadPlayer implements TileOccupier {
    @Override
    public char toTileChar() {
        return 'X';
    }

    @Override
    public Object accept(TileVisitor visitor, Object state) throws LogicException {
        throw new LogicException("Player is dead, cannot accept unit.");
    }

    @Override
    public boolean isFree() {
        return false;
    }

    @Override
    public TileOccupier clone(Point position, RandomGenerator randomGenerator, Board board) {
        return new DeadPlayer();
    }

    @Override
    public String toString() {
        return toTileChar() + "";
    }
}
