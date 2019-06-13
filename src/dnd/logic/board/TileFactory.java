package dnd.logic.board;

import dnd.logic.tileOccupiers.DeadPlayer;
import dnd.logic.tileOccupiers.FreeTile;

/**
 * A factory for specific tile occupiers used by the board
 */
public interface TileFactory {
    FreeTile createFreeTile();
    DeadPlayer createDeadPlayer();
}
