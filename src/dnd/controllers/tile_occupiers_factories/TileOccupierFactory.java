package dnd.controllers.tile_occupiers_factories;

import dnd.logic.Point;
import dnd.logic.tileOccupiers.TileOccupier;

/**
 * Factory which responsible for creating new units and tile occupiers when loading a level.
 */
public interface TileOccupierFactory {
    TileOccupier createTileOccupier(Point position);
}
