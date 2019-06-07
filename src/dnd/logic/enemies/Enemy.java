package dnd.logic.enemies;

import dnd.GameEventObserver;
import dnd.GameException;
import dnd.dto.units.EnemyDTO;
import dnd.dto.units.PlayerDTO;
import dnd.dto.units.UnitDTO;
import dnd.logic.DeathObserver;
import dnd.logic.MoveResult;
import dnd.logic.board.Board;
import dnd.logic.player.Player;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.TileVisitor;
import dnd.logic.tileOccupiers.Unit;

public abstract class Enemy extends Unit {
    int experienceValue;
    char tile;

    Enemy(String name,
          int healthPool, int attack, int defense,
          int experienceValue, char tile) {
        super(name, healthPool, attack, defense);
        init(experienceValue, tile);
    }

    Enemy(String name,
          int healthPool, int attack, int defense,
          RandomGenerator randomGenerator,
          Board board,
          int experienceValue, char tile) {
        super(name, healthPool, attack, defense, randomGenerator, board);
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

    @Override
    public boolean defend(Unit attacker, int damage) throws GameException {
        boolean died = super.defend(attacker, damage);
        if (died) {
            callDeathObservers();
            callOnEnemyDeathObservers();
        }

        return died;
    }

    private void callOnEnemyDeathObservers() {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onEnemyDeath((EnemyDTO)createDTO());
        }
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

    void callEngageObservers(Player player) {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onEnemyEngage((EnemyDTO)createDTO(), (PlayerDTO)player.createDTO());
        }
    }

    private void callDeathObservers() throws GameException {
        for (DeathObserver observer : deathObservers) {
            observer.onDeath(this);
        }
    }

    @Override
    public char toTileChar() {
        return tile;
    }

    @Override
    public UnitDTO createDTO() {
        EnemyDTO enemyDTO = new EnemyDTO();
        fillUnitDtoFields(enemyDTO);
        return enemyDTO;
    }

    public char getTileChar() {
        return tile;
    }

    @Override
    public String toString() {
        return getTileChar() + ", " + name;
    }
}
