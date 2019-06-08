package dnd.controllers.tile_occupiers_factories;

import dnd.logic.Point;
import dnd.logic.tileOccupiers.TileOccupier;

public interface TileOccupierFactory {
    TileOccupier createTileOccupier(Point position);
}
