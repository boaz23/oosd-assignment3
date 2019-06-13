package dnd.logic.board;

import dnd.logic.DeathObserver;
import dnd.logic.Point;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;
import dnd.logic.tileOccupiers.TileOccupier;
import dnd.logic.tileOccupiers.Unit;

import java.util.List;

/**
 * The board interface each unit needs in order to perform it's logic
 */
public interface Board extends DeathObserver {
    /**
     * Changes the unit location on the board itself to the given position
     */
    void move(Unit unit, Point targetPosition) throws PositionOutOfBoundsException;

    /**
     * Returns the game object which occupies the tile on the specified position.
     */
    TileOccupier getTileOccupier(Point position) throws PositionOutOfBoundsException;

    /**
     * Returns a list of positions of all free tiles in the specified radius of the specified position.
     * @param position The position which is the center of the circle which we are looking free tiles at
     * @param range The radius of the circle
     */
    List<Point> getFreeTilesPositionsInRange(Point position, int range) throws PositionOutOfBoundsException;

    /**
     * Returns a list of all enemies in the specified radius of the specified position.
     * @param position The position which is the center of the circle which we are looking enemies at
     * @param range The radius of the circle
     */
    List<Enemy> getEnemiesInRange(Point position, int range) throws PositionOutOfBoundsException;

    /**
     * Returns the player if it's in the specified range of the specified position.
     * @param position The position to look for the player
     * @param range The maximum distance from the position to look for the player
     */
    Player getPlayerInRange(Point position, int range) throws PositionOutOfBoundsException;
}
