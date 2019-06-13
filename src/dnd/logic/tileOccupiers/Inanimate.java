package dnd.logic.tileOccupiers;

/**
 * Represents any non-unit tile occupier (free tile, wall and dead player)
 */
public abstract class Inanimate extends TileOccupier {
    @Override
    public abstract Inanimate clone();
}
