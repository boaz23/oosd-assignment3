package dnd.logic;

import java.util.List;
import java.util.Set;

public interface Board {
    /**
     * Find all the tile occupiers with range 'range' of the given position which has the specified properties
     * (ignoring the given position)
     * @param position The position to take range from
     * @param range The range to look for tiles from position
     * @param properties The properties the tiles should have
     * @return A list of all tiles that has the given properties
     */
    List<TileOccupier> findTileOccupiers(Point position, int range, Set<TileProperty> properties);

    /**
     * Requests to move the specified unit to the specified location
     * @param unit
     * @param targetPosition
     * @return Whether the move was successful or not (the position might have a wall in it or it is out of range of the board).
     */
    boolean move(Unit unit, Point targetPosition);
}
