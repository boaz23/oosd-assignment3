package dnd.logic.tileOccupiers;

import dnd.GameException;
import dnd.logic.MoveResult;
import dnd.logic.Point;
import dnd.logic.board.Board;
import dnd.logic.random_generator.RandomGenerator;

public interface TileOccupier {
    char toTileChar();
    MoveResult accept(TileVisitor visitor) throws GameException;

    boolean isFree();

    TileOccupier clone(Point position, RandomGenerator randomGenerator, Board board);
}
