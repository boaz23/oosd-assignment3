package dnd.logic.player;

import dnd.RandomGenerator;
import dnd.logic.LogicException;
import dnd.logic.MoveResult;
import dnd.logic.board.Board;
import dnd.logic.enemies.Enemy;
import dnd.logic.tileOccupiers.Unit;

public abstract class Player extends Unit {
    private static final int LEVEL_EXP_DIFF = 50;
    private static final int LEVEL_HEALTH_DIFF = 10;
    private static final int LEVEL_ATTACK_DIFF = 5;
    private static final int LEVEL_DEFENSE_DIFF = 2;

    protected int experience;
    protected int level;

    public Player(String name,
                  int healthPool, int attack, int defense,
                  RandomGenerator randomGenerator) {
        super(name, healthPool, attack, defense, randomGenerator);
    }

    protected Player(String name,
                     int healthPool, int attack, int defense,
                     RandomGenerator randomGenerator,
                     Board board) {
        super(name, healthPool, attack, defense, randomGenerator, board);
    }

    @Override
    public char getTileChar() {
        return '@';
    }

    protected void levelUp() {
        this.experience -= this.level * LEVEL_EXP_DIFF;
        this.level++;
        this.healthPool += this.level * LEVEL_HEALTH_DIFF;
        this.currentHealth = this.healthPool;
        this.attack += this.level * LEVEL_ATTACK_DIFF;
        this.defense += this.level * LEVEL_DEFENSE_DIFF;
    }

    public void gainExp(int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("exp gained must be a non-negative number.");
        }

        int expLeftToNextLevel = getExpLeftToNextLevel();
        while (exp > expLeftToNextLevel) {
            exp -= expLeftToNextLevel;
            this.experience += expLeftToNextLevel;
            this.levelUp();
            expLeftToNextLevel = getExpLeftToNextLevel();
        }

        this.experience += exp;
    }

    private int getExpLeftToNextLevel() {
        return (this.level * LEVEL_EXP_DIFF) - this.experience;
    }

    public abstract void useSpecialAbility() throws LogicException;

    @Override
    public MoveResult accept(Unit unit, Object state) throws LogicException {
        return unit.attack(this, state);
    }

    @Override
    public MoveResult attack(Enemy enemy, Object state) throws LogicException {
        return this.attackMove(enemy);
    }

    @Override
    public MoveResult attack(Player player, Object state) throws LogicException {
        throw new LogicException("player fights another player");
    }

    protected MoveResult attackMove(Enemy enemy) {
        return this.attackCore(enemy) ? MoveResult.Dead : MoveResult.Engaged;
    }

    @Override
    protected boolean attackCore(Enemy enemy) {
        boolean died = super.attackCore(enemy);
        if (died) {
            this.gainExp(enemy.getExperienceValue());
        }

        return died;
    }

    @Override
    public boolean defend(int damage) {
        boolean died = super.defend(damage);
        if (died) {
            this.board.reportDeath(this);
        }

        return died;
    }

    protected class MoveAttackState implements AttackState {
        @Override
        public Object visit(Enemy enemy) {
            return Player.this.attackMove(enemy);
        }
    }
}
