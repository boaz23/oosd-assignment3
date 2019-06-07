package dnd.logic.tileOccupiers;

import dnd.GameEventObserver;
import dnd.GameException;
import dnd.dto.units.UnitDTO;
import dnd.logic.*;
import dnd.logic.board.Board;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;
import dnd.logic.random_generator.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public abstract class Unit implements TickObserver, TileOccupier, TileVisitor {
    protected final String name;
    protected int healthPool;
    protected int currentHealth;
    protected int attack;
    protected int defense;
    protected Point position;

    protected Board board;
    protected RandomGenerator randomGenerator;

    protected List<DeathObserver> deathObservers;
    protected List<GameEventObserver> gameEventObservers;

    protected Unit(String name,
                   int healthPool, int attack, int defense) {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("a unit's name cannot be null or empty.");
        }
        if (healthPool <= 0) {
            throw new IllegalArgumentException("a unit's health pool must be a positive number");
        }
        if (attack <= 0) {
            throw new IllegalArgumentException("a unit's formatString must be a positive number");
        }
        if (defense <= 0) {
            throw new IllegalArgumentException("a unit's defense must be a positive number");
        }

        this.name = name;
        this.healthPool = healthPool;
        this.currentHealth = healthPool;
        this.attack = attack;
        this.defense = defense;

        this.deathObservers = new ArrayList<>();
        this.gameEventObservers = new ArrayList<>();
    }

    protected Unit(String name,
                   int healthPool, int attack, int defense,
                   RandomGenerator randomGenerator,
                   Board board) {
        this(name, healthPool, attack, defense);
        if (randomGenerator == null) {
            throw new IllegalArgumentException("randomGenerator is null.");
        }
        if (board == null) {
            throw new IllegalArgumentException("unitsInRangeFinder is null.");
        }

        this.randomGenerator = randomGenerator;
        this.board = board;
    }

    public Point getPosition() {
        return this.position;
    }

    public MoveResult move(Point newPosition) throws GameException {
        if (newPosition == null) {
            throw new IllegalArgumentException("newPosition is null.");
        }

        MoveResult moveResult;
        try {
            TileOccupier targetTileOccupier = this.board.getTileOccupier(newPosition);
            moveResult = targetTileOccupier.accept(this);
            if (moveResult == MoveResult.Allowed) {
                boolean moved = this.moveActual(newPosition);
                if (!moved) {
                    moveResult = MoveResult.Invalid;
                }
            }
        }
        catch (PositionOutOfBoundsException | LogicException e) {
            moveResult = MoveResult.Invalid;
        }

        return moveResult;
    }

    private boolean moveActual(Point newPosition) {
        try {
            this.board.move(this, newPosition);
            this.position = newPosition;
            return true;
        }
        catch (PositionOutOfBoundsException e) {
            return false;
        }
    }

    public MoveResult visit(FreeTile freeTile) {
        if (freeTile == null) {
            throw new IllegalArgumentException("freeTile is null.");
        }

        return MoveResult.Allowed;
    }

    public MoveResult visit(Wall wall) {
        if (wall == null) {
            throw new IllegalArgumentException("wall is null.");
        }

        return MoveResult.Invalid;
    }

    public abstract MoveResult visit(Enemy enemy) throws GameException;

    public abstract MoveResult visit(Player player) throws GameException;

    protected boolean meleeAttack(Unit unit) throws GameException {
        if (unit == null) {
            throw new IllegalArgumentException("unit is null.");
        }

        int damage = this.randomGenerator.nextInt(this.attack);
        this.callAttackPointsRollObservers(damage);
        return this.attackCore(unit, damage);
    }

    private void callAttackPointsRollObservers(int attackPointsRoll) {
        for (GameEventObserver observer : this.gameEventObservers) {
            observer.onAttackPointsRoll(this.createDTO(), attackPointsRoll);
        }
    }

    private void callDefensePointsRollObservers(int defensePointsRoll) {
        for (GameEventObserver observer : this.gameEventObservers) {
            observer.onDefensePointsRoll(this.createDTO(), defensePointsRoll);
        }
    }

    protected boolean attackCore(Unit unit, int damage) throws GameException {
        return unit.defend(this, damage);
    }

    /**
     * Defends from taking 'damage' amount of damage and lowers
     * the current health according to the actual damage dealt
     * (it might have been lowered by rolling a number between 0 and defense)
     *
     * @param damage The amount of damage to formatString from
     * @return Whether the unit died
     */
    public boolean defend(Unit attacker, int damage) throws GameException {
        int reduction = this.randomGenerator.nextInt(this.defense);
        this.callDefensePointsRollObservers(reduction);
        damage = Math.max(damage - reduction, 0);
        this.callOnHitObservers(attacker, damage);
        this.currentHealth = Math.max(this.currentHealth - damage, 0);
        return this.currentHealth == 0;
    }

    private void callOnHitObservers(Unit attacker, int damage) {
        for (GameEventObserver observer : this.gameEventObservers) {
            observer.onHit(attacker.createDTO(), this.createDTO(), damage);
        }
    }

    public void addDeathObserver(DeathObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("observer is null.");
        }

        this.deathObservers.add(observer);
    }

    public void addGameEventObserver(GameEventObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("observer is null.");
        }

        this.gameEventObservers.add(observer);
    }

    @Override
    public boolean isFree() {
        return false;
    }

    public MoveResult moveLeft() throws GameException {
        return this.move(new Point(this.position.getX() - 1, this.position.getY()));
    }

    public MoveResult moveRight() throws GameException {
        return this.move(new Point(this.position.getX() + 1, this.position.getY()));
    }

    public MoveResult moveUp() throws GameException {
        return this.move(new Point(this.position.getX(), this.position.getY() - 1));
    }

    public MoveResult moveDown() throws GameException {
        return this.move(new Point(this.position.getX(), this.position.getY() + 1));
    }

    public abstract UnitDTO createDTO();

    protected void fillUnitDtoFields(UnitDTO unitDTO) {
        unitDTO.name = this.name;
        unitDTO.healthPool = this.healthPool;
        unitDTO.currentHealth = this.currentHealth;
        unitDTO.attack = this.attack;
        unitDTO.defense = this.defense;
    }

    @Override
    public String toString() {
        return toTileChar() + ", " + name;
    }
}
