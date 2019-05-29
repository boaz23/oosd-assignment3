package dnd.logic.tileOccupiers;

public interface TileFactory {
    FreeTile createFreeTile();
    DeadPlayer createDeadPlayer();
}
