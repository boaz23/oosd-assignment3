package dnd.logic.tileOccupiers;

import dnd.GameException;
import dnd.logic.Point;
import dnd.logic.board.Board;
import dnd.logic.random_generator.RandomGenerator;

public interface TileOccupier {
    char toTileChar();
    Object accept(TileVisitor visitor, Object state) throws GameException;

    boolean isFree();

    TileOccupier clone(Point poisiton, RandomGenerator randomGenerator, Board board);
}
