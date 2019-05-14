package logic;

import interfaces.Observer;

public abstract class Unit implements Observer<Void> {
    private String name;
    private int healthPool;
    private int currentHealth;
    private int attackPoints;
    private int defense;
    private Point position;
}
