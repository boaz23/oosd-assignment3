package dnd.logic;

import dnd.RandomGenerator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class Unit extends TileOccupierImpl implements TickObserver {
    protected final String name;
    protected int healthPool;
    protected int currentHealth;
    protected int attack;
    protected int defense;
    protected Point position;

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

        this.addProperty(TileProperty.Unit);
    }

    public Point getPosition() {
        return this.position;
    }

    public boolean attack(Unit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("unit is null.");
        }

        int damage = this.randomGenerator.nextInt(this.attack);
        return this.attack(unit, damage);
    }

    protected boolean attack(Unit unit, int damage) {
        return unit.defend(damage);
    }

    /**
     * Defends from taking 'damage' amount of damage and lowers
     * the current health according to the actual damage dealth
     * (it might have been lowered by rolling a number between 0 and defense)
     * @param damage The amount of damage to defend from
     * @return Whether the unit died
     */
    public boolean defend(int damage) {
        int reduction = this.randomGenerator.nextInt(this.defense);
        this.currentHealth = Math.max(this.currentHealth - reduction, 0);
        return this.currentHealth == 0;
    }

    @Override
    public ReadOnlySet<TileProperty> getProperties() {
        return null;
    }
}
