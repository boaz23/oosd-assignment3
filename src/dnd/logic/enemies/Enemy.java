package dnd.logic.enemies;

import dnd.RandomGenerator;
import dnd.logic.Unit;
import dnd.logic.UnitType;
import dnd.logic.UnitsInRangeFinder;

public abstract class Enemy extends Unit {
    final int experienceValue;
    final char tile;

    public Enemy(String name,
                 int healthPool, int attack, int defense,
                 UnitsInRangeFinder unitsInRangeFinder,
                 RandomGenerator randomGenerator,
                 int experienceValue, char tile) {
        super(name, healthPool, attack, defense, unitsInRangeFinder, randomGenerator);

        if (experienceValue < 0) {
            throw new IllegalArgumentException("experienceValue must be a non-negative number.");
        }
        if (tile == '\0') {
            throw new IllegalArgumentException("the tile must be a valid character.");
        }

        this.experienceValue = experienceValue;
        this.tile = tile;
    }

    @Override
    public final UnitType getUnitType() {
        return UnitType.Enemy;
    }
}
