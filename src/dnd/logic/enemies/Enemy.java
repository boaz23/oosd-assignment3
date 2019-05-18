package dnd.logic.enemies;

import dnd.logic.Unit;
import dnd.logic.UnitType;
import dnd.logic.UnitsInRangeFinder;

public abstract class Enemy extends Unit {
    int experienceValue;
    char tile;

    public Enemy(UnitsInRangeFinder unitsInRangeFinder) {
        super(unitsInRangeFinder);
    }

    @Override
    public final UnitType getUnitType() {
        return UnitType.Enemy;
    }
}
