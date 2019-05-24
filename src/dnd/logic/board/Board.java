package dnd.logic.board;

import dnd.logic.Point;
import dnd.logic.PositionOutOfBoundsException;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;
import dnd.logic.tileOccupiers.TileOccupier;
import dnd.logic.tileOccupiers.Unit;

import java.util.List;

public interface Board {
    /**
     * Finds all the tile occupiers with range 'range' of the given position.
     * (ignoring the given position)
     * @param position The position to take range from
     * @param range The range to look for tiles from position
     * @return A list of all tiles occupiers within the given range of the given position
     */
    List<TileOccupier> findTileOccupiers(Point position, int range);

    /**
     * Requests to move the specified unit to the specified location
     * @param unit
     * @param targetPosition
     * @return
     */
    void move(Unit unit, Point targetPosition);

    TileOccupier getTileOccupier(Point position) throws PositionOutOfBoundsException;

    void reportDeath(Player player);
    void reportDeath(Enemy enemy);
}
