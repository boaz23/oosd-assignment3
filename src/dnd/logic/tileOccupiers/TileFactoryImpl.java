package dnd.logic.tileOccupiers;

import dnd.logic.available_units.AvailableInanimate;

public class TileFactoryImpl implements TileFactory {
    @Override
    public FreeTile createFreeTile() {
        return (FreeTile)AvailableInanimate.FreeTile.clone(null, null);
    }

    @Override
    public DeadPlayer createDeadPlayer() {
        return (DeadPlayer)AvailableInanimate.DeadPlayer.clone(null, null);
    }
}
