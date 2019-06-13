package dnd.logic.tileOccupiers;

import dnd.cli.view.GameEventObserver;
import dnd.dto.units.UnitDTO;
import dnd.logic.*;
import dnd.logic.board.Board;
import dnd.logic.board.PositionOutOfBoundsException;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;
import dnd.logic.random_generator.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for every unit of the game (player and enemies).
 */
public abstract class Unit extends TileOccupier implements TickObserver, TileVisitor {
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
        currentHealth = healthPool;
        this.attack = attack;
        this.defense = defense;
    }

    @Override
    public boolean isFree() {
        return false;
    }

    public Point getPosition() {
        return position;
    }

    public MoveResult moveLeft() throws GameException {
        return move(new Point(position.getX() - 1, position.getY()));
    }

    public MoveResult moveRight() throws GameException {
        return move(new Point(position.getX() + 1, position.getY()));
    }

    public MoveResult moveUp() throws GameException {
        return move(new Point(position.getX(), position.getY() - 1));
    }

    public MoveResult moveDown() throws GameException {
        return move(new Point(position.getX(), position.getY() + 1));
    }

    /**
     * Perform a move to the specified position. It may result in a combat or other logic
     * @param newPosition The target position to attempt to move
     * @return The result of the move
     */
    protected MoveResult move(Point newPosition) throws GameException {
        if (newPosition == null) {
            throw new IllegalArgumentException("newPosition is null.");
        }

        MoveResult moveResult;
        try {
            TileOccupier targetTileOccupier = board.getTileOccupier(newPosition);
            moveResult = targetTileOccupier.accept(this);
            if (moveResult == MoveResult.Allowed) {
                boolean moved = moveActual(newPosition);
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

    /**
     * Act when moved on a free tile
     */
    public MoveResult visit(FreeTile freeTile) {
        if (freeTile == null) {
            throw new IllegalArgumentException("freeTile is null.");
        }

        return MoveResult.Allowed;
    }

    /**
     * Act when moved to a wall
     */
    public MoveResult visit(Wall wall) {
        if (wall == null) {
            throw new IllegalArgumentException("wall is null.");
        }

        return MoveResult.Invalid;
    }

    /**
     * Act when encoutered an enemy
     */
    public abstract MoveResult visit(Enemy enemy) throws GameException;

    /**
     * Act when encountered a player
     */
    public abstract MoveResult visit(Player player) throws GameException;

    public void addDeathObserver(DeathObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("observer is null.");
        }

        deathObservers.add(observer);
    }

    public void addGameEventObserver(GameEventObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("observer is null.");
        }

        gameEventObservers.add(observer);
    }

    /**
     * Melee attacks a unit
     */
    protected boolean meleeAttack(Unit unit) throws GameException {
        if (unit == null) {
            throw new IllegalArgumentException("unit is null.");
        }

        int damage = randomGenerator.nextInt(attack);
        callAttackPointsRollObservers(damage);
        return attackCore(unit, damage);
    }

    /**
     * Core logic of attacking a unit
     * @param unit The unit to attack
     * @param damage The amount of damage to attack the unit with
     */
    protected boolean attackCore(Unit unit, int damage) throws GameException {
        return unit.defend(this, damage);
    }

    /**
     * Defends from taking 'damage' amount of damage and lowers
     * the current health according to the actual damage dealt
     * (it might have been lowered by rolling a number between 0 and defense)
     *
     * @param damage The amount of damage to defend from
     * @return Whether the unit died
     */
    public boolean defend(Unit attacker, int damage) throws GameException {
        int reduction = randomGenerator.nextInt(defense);
        callDefensePointsRollObservers(reduction);
        damage = Math.max(damage - reduction, 0);
        callOnHitObservers(attacker, damage);
        currentHealth = Math.max(currentHealth - damage, 0);
        return currentHealth == 0;
    }

    /**
     * Changes the position of this to the specfied position
     * @param newPosition The position to change this unit's position to
     * @return Whether the move was successful or not
     */
    private boolean moveActual(Point newPosition) {
        try {
            board.move(this, newPosition);
            position = newPosition;
            return true;
        }
        catch (PositionOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Initializes this unit's state to new level ready state
     */
    public void initNewLevelState(Point position, RandomGenerator randomGenerator, Board board) {
        if (randomGenerator == null) {
            throw new IllegalArgumentException("randomGenerator is null.");
        }
        if (board == null) {
            throw new IllegalArgumentException("unitsInRangeFinder is null.");
        }

        deathObservers = new ArrayList<>();
        gameEventObservers = new ArrayList<>();
        this.randomGenerator = randomGenerator;
        this.board = board;
        this.position = position;
    }

    /**
     * Creates a DTO of this unit's stats
     */
    protected abstract UnitDTO createDTO();

    /**
     * Fills the given unit DTO fields with this unit's stats
     */
    protected void fillUnitDtoFields(UnitDTO unitDTO) {
        unitDTO.name = name;
        unitDTO.healthPool = healthPool;
        unitDTO.currentHealth = currentHealth;
        unitDTO.attack = attack;
        unitDTO.defense = defense;
    }

    private void callAttackPointsRollObservers(int attackPointsRoll) {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onAttackPointsRoll(createDTO(), attackPointsRoll);
        }
    }

    private void callDefensePointsRollObservers(int defensePointsRoll) {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onDefensePointsRoll(createDTO(), defensePointsRoll);
        }
    }

    private void callOnHitObservers(Unit attacker, int damage) {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onHit(attacker.createDTO(), createDTO(), damage);
        }
    }

    @Override
    public String toString() {
        return toTileChar() + ", " + name;
    }
}
