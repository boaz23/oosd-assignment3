package dnd.logic.enemies;

import dnd.cli.view.GameEventObserver;
import dnd.dto.units.EnemyDTO;
import dnd.logic.DeathObserver;
import dnd.logic.GameException;
import dnd.logic.MoveResult;
import dnd.logic.player.Player;
import dnd.logic.tileOccupiers.TileVisitor;
import dnd.logic.tileOccupiers.Unit;

/**
 * Base class for all enemies in the game
 */
public abstract class Enemy extends Unit {
    int experienceValue;
    char tile;

    Enemy(String name,
          int healthPool, int attack, int defense,
          int experienceValue, char tile) {
        super(name, healthPool, attack, defense);
        init(experienceValue, tile);
    }

    private void init(int experienceValue, char tile) {
        if (experienceValue < 0) {
            throw new IllegalArgumentException("experienceValue must be a non-negative number.");
        }
        if (tile == '\0') {
            throw new IllegalArgumentException("the tile must be a valid character.");
        }

        this.experienceValue = experienceValue;
        this.tile = tile;
    }

    public int getExperienceValue() {
        return experienceValue;
    }

    /**
     * Used to get the tile char for the factory map when loading a level
     * @return
     */
    public char getTileChar() {
        return tile;
    }

    @Override
    public char toTileChar() {
        return tile;
    }

    @Override
    public MoveResult accept(TileVisitor visitor) throws GameException {
        return visitor.visit(this);
    }

    @Override
    public MoveResult visit(Enemy enemy) {
        return MoveResult.Invalid;
    }

    @Override
    public MoveResult visit(Player player) throws GameException {
        callEngageObservers(player);
        return meleeAttack(player) ? MoveResult.Dead : MoveResult.Engaged;
    }

    @Override
    public boolean defend(Unit attacker, int damage) throws GameException {
        boolean died = super.defend(attacker, damage);
        if (died) {
            callDeathObservers();
            callOnEnemyDeathObservers();
        }

        return died;
    }

    @Override
    public abstract Enemy clone();

    @Override
    public EnemyDTO createDTO() {
        EnemyDTO enemyDTO = new EnemyDTO();
        fillUnitDtoFields(enemyDTO);
        return enemyDTO;
    }

    @Override
    public String toString() {
        return getTileChar() + ", " + name;
    }

    private void callOnEnemyDeathObservers() {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onEnemyDeath(createDTO());
        }
    }

    void callEngageObservers(Player player) {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onEnemyEngage(createDTO(), player.createDTO());
        }
    }

    private void callDeathObservers() throws GameException {
        for (DeathObserver observer : deathObservers) {
            observer.onDeath(this);
        }
    }
}
