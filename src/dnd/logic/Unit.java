package dnd.logic;

public abstract class Unit implements TickObserver {
    protected String name;
    protected int healthPool;
    protected int currentHealth;
    protected int attack;
    protected int defense;
    protected Point position;
}
