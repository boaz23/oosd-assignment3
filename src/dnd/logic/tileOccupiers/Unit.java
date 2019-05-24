package dnd.logic.tileOccupiers;

import dnd.RandomGenerator;
import dnd.logic.*;
import dnd.logic.board.Board;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;

public abstract class Unit implements TickObserver, TileOccupier {
    protected final String name;
    protected int healthPool;
    protected int currentHealth;
    protected int attack;
    protected int defense;
    protected Point position;

    protected Board board;
    protected final RandomGenerator randomGenerator;

    public Unit(String name,
                int healthPool, int attack, int defense,
                RandomGenerator randomGenerator) {
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
            throw new IllegalArgumentException("a unit's visit must be a positive number");
        }
        if (defense <= 0) {
            throw new IllegalArgumentException("a unit's defense must be a positive number");
        }

        this.name = name;
        this.healthPool = healthPool;
        this.currentHealth = healthPool;
        this.attack = attack;
        this.defense = defense;

        this.randomGenerator = randomGenerator;
    }

    protected Unit(String name,
                   int healthPool, int attack, int defense,
                   RandomGenerator randomGenerator,
                   Board board) {
        this(name, healthPool, attack, defense, randomGenerator);
        if (board == null) {
            throw new IllegalArgumentException("unitsInRangeFinder is null.");
        }

        this.board = board;
    }

    public Point getPosition() {
        return this.position;
    }

    public MoveResult move(Point newPosition) {
        if (newPosition == null) {
            throw new IllegalArgumentException("newPosition is null.");
        }

        MoveResult moveResult;
        try {
            TileOccupier targetTileOccupier = this.board.getTileOccupier(newPosition);
            moveResult = targetTileOccupier.accept(this);
            if (moveResult == MoveResult.Allowed) {
                this.moveActual(newPosition);
            }
        }
        catch (PositionOutOfBoundsException | LogicException e) {
            moveResult = MoveResult.Invalid;
        }

        return moveResult;
    }

    private void moveActual(Point newPosition) {
        this.board.move(this, newPosition);
        this.position = newPosition;
    }

    public void visit(FreeTile freeTile) {
        if (freeTile == null) {
            throw new IllegalArgumentException("freeTile is null.");
        }

        // Do nothing
    }
    public void visit(Wall wall) {
        if (wall == null) {
            throw new IllegalArgumentException("wall is null.");
        }

        // Do nothing
    }

    public abstract MoveResult attack(Enemy enemy) throws LogicException;
    public abstract MoveResult attack(Player player) throws LogicException;

    protected boolean attackCore(Enemy enemy) {
        return attackCore((Unit)enemy);
    }

    protected boolean attackCore(Unit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("unit is null.");
        }

        int damage = this.randomGenerator.nextInt(this.attack);
        return this.attackCore(unit, damage);
    }

    protected boolean attackCore(Unit unit, int damage) {
        return unit.defend(damage);
    }

    /**
     * Defends from taking 'damage' amount of damage and lowers
     * the current health according to the actual damage dealth
     * (it might have been lowered by rolling a number between 0 and defense)
     * @param damage The amount of damage to visit from
     * @return Whether the unit died
     */
    public boolean defend(int damage) {
        int reduction = this.randomGenerator.nextInt(this.defense);
        this.currentHealth = Math.max(this.currentHealth - reduction, 0);
        return this.currentHealth == 0;
    }
}
