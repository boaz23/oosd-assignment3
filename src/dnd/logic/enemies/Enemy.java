package dnd.logic.enemies;

import dnd.GameEventObserver;
import dnd.dto.units.EnemyDTO;
import dnd.dto.units.PlayerDTO;
import dnd.dto.units.UnitDTO;
import dnd.logic.DeathObserver;
import dnd.logic.LogicException;
import dnd.logic.MoveResult;
import dnd.logic.board.Board;
import dnd.logic.player.Player;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.TileVisitor;
import dnd.logic.tileOccupiers.Unit;

public abstract class Enemy extends Unit {
    int experienceValue;
    char tile;

    public Enemy(String name,
                 int healthPool, int attack, int defense,
                 int experienceValue, char tile) {
        super(name, healthPool, attack, defense);
        this.init(experienceValue, tile);
    }

    protected Enemy(String name,
                    int healthPool, int attack, int defense,
                    RandomGenerator randomGenerator,
                    Board board,
                    int experienceValue, char tile) {
        super(name, healthPool, attack, defense, randomGenerator, board);
        this.init(experienceValue, tile);
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
    public boolean defend(Unit attacker, int damage) {
        boolean died = super.defend(attacker, damage);
        if (died) {
            this.callDeathObservers();
            this.callOnEnemyDeathObservers();
        }

        return died;
    }

    private void callOnEnemyDeathObservers() {
        for (GameEventObserver observer : this.gameEventObservers) {
            observer.onEnemyDeath((EnemyDTO)this.createDTO());
        }
    }

    @Override
    public Object accept(TileVisitor visitor, Object state) throws LogicException {
        return visitor.visit(this, state);
    }

    @Override
    public MoveResult visit(Enemy enemy, Object state) {
        return MoveResult.Invalid;
    }

    @Override
    public MoveResult visit(Player player, Object state) {
        for (GameEventObserver observer : this.gameEventObservers) {
            observer.onEnemyEngage((EnemyDTO)this.createDTO(), (PlayerDTO)player.createDTO());
        }

        return this.meleeAttack(player) ? MoveResult.Dead : MoveResult.Engaged;
    }

    @Override
    protected void callDeathObservers() {
        for (DeathObserver observer : this.deathObservers) {
            observer.onDeath(this);
        }
    }

    @Override
    public char toTileChar() {
        return this.tile;
    }

    @Override
    public UnitDTO createDTO() {
        EnemyDTO enemyDTO = new EnemyDTO();
        this.fillUnitDtoFields(enemyDTO);
        return enemyDTO;
    }

    public char getTileChar() {
        return tile;
    }
}
