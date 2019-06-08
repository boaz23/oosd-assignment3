package dnd.logic.tileOccupiers;

import dnd.logic.available_units.AvailableInanimate;

public class TileFactoryImpl implements TileFactory {
    @Override
    public FreeTile createFreeTile() {
        return AvailableInanimate.FreeTile.clone();
    }

    @Override
    public DeadPlayer createDeadPlayer() {
        return AvailableInanimate.DeadPlayer.clone();
    }
}
