package dnd.logic.enemies;

import dnd.RandomGenerator;
import dnd.logic.LogicException;
import dnd.logic.MoveResult;
import dnd.logic.board.Board;
import dnd.logic.player.Player;
import dnd.logic.tileOccupiers.TileVisitor;
import dnd.logic.tileOccupiers.Unit;

public abstract class Enemy extends Unit {
    int experienceValue;
    char tile;

    public Enemy(String name,
                 int healthPool, int attack, int defense,
                 RandomGenerator randomGenerator,
                 int experienceValue, char tile) {
        super(name, healthPool, attack, defense, randomGenerator);
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
    public boolean defend(int damage) {
        boolean died = super.defend(damage);
        if (died) {
            this.board.reportDeath(this);
        }

        return died;
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
        return this.meeleAttack(player) ? MoveResult.Dead : MoveResult.Engaged;
    }

    @Override
    public char getTileChar() {
        return this.tile;
    }
}
