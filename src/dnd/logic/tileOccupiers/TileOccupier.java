package dnd.logic.tileOccupiers;

import dnd.logic.LogicException;
import dnd.logic.Point;
import dnd.logic.board.Board;
import dnd.logic.random_generator.RandomGenerator;

public interface TileOccupier {
    char toTileChar();
    Object accept(TileVisitor visitor, Object state) throws LogicException;

    boolean isFree();

    TileOccupier clone(Point poisiton, RandomGenerator randomGenerator, Board board);
}
