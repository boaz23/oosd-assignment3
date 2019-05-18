package dnd.logic.enemies;

import dnd.RandomGenerator;
import dnd.logic.Unit;
import dnd.logic.UnitType;
import dnd.logic.UnitsInRangeFinder;

public abstract class Enemy extends Unit {
    int experienceValue;
    char tile;

    public Enemy(String name,
                 int healthPool, int attack, int defense,
                 UnitsInRangeFinder unitsInRangeFinder,
                 RandomGenerator randomGenerator) {
        super(name, healthPool, attack, defense, unitsInRangeFinder, randomGenerator);
    }

    @Override
    public final UnitType getUnitType() {
        return UnitType.Enemy;
    }
}
