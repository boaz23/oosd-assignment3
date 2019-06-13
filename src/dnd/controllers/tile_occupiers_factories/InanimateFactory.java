package dnd.controllers.tile_occupiers_factories;

import dnd.logic.Point;
import dnd.logic.tileOccupiers.Inanimate;

/**
 * Responsible for creating new inanimate units from a template enemy using the prototype pattern.
 */
public class InanimateFactory implements TileOccupierFactory {
    private final Inanimate inanimate;

    public InanimateFactory(Inanimate inanimate) {
        this.inanimate = inanimate;
    }

    @Override
    public Inanimate createTileOccupier(Point position) {
        return inanimate.clone();
    }
}
