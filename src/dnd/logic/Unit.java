package dnd.logic;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class Unit extends BoardSquare implements TickObserver {
    protected String name;
    protected int healthPool;
    protected int currentHealth;
    protected int attack;
    protected int defense;
    protected Point position;

    protected UnitsInRangeFinder unitsInRangeFinder;

    public Unit(UnitsInRangeFinder unitsInRangeFinder) {
        if (unitsInRangeFinder == null) {
            throw new IllegalArgumentException("unitsInRangeFinder is null.");
        }

        this.unitsInRangeFinder = unitsInRangeFinder;
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
