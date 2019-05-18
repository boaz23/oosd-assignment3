package dnd.logic;

import dnd.RandomGenerator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class Unit extends BoardSquare implements TickObserver {
    protected String name;
    protected int healthPool;
    protected int currentHealth;
    protected int attack;
    protected int defense;
    protected Point position;

    protected final UnitsInRangeFinder unitsInRangeFinder;
    protected final RandomGenerator randomGenerator;

    public Unit(String name,
                int healthPool, int attack, int defense,
                UnitsInRangeFinder unitsInRangeFinder,
                RandomGenerator randomGenerator) {
        if (unitsInRangeFinder == null) {
            throw new IllegalArgumentException("unitsInRangeFinder is null.");
        }
        if (randomGenerator == null) {
            throw new IllegalArgumentException("randomGenerator is null.");
        }
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("a unit's name cannot be null or empty.");
        }
        if (healthPool <= 0) {
            throw new IllegalArgumentException("a unit's health pool must be a positive number");
        }
        if (attack <= 0) {
            throw new IllegalArgumentException("a unit's attack must be a positive number");
        }
        if (defense <= 0) {
            throw new IllegalArgumentException("a unit's defense must be a positive number");
        }

        this.name = name;
        this.healthPool = healthPool;
        this.currentHealth = healthPool;
        this.attack = attack;
        this.defense = defense;

        this.unitsInRangeFinder = unitsInRangeFinder;
        this.randomGenerator = randomGenerator;
    }

    public abstract UnitType getUnitType();

    public void takeDamage(int damage) {
        throw new NotImplementedException();
    }

    @Override
    public BoardSqaureType getSquareType() {
        return BoardSqaureType.Unit;
    }
}
