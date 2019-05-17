package dnd.logic.player;

import dnd.logic.LogicException;
import dnd.logic.Unit;

public abstract class Player extends Unit {
    private static final int LEVEL_EXP_DIFF = 50;
    private static final int LEVEL_HEALTH_DIFF = 10;
    private static final int LEVEL_ATTACK_DIFF = 5;
    private static final int LEVEL_DEFENSE_DIFF = 2;

    protected Integer experience;
    protected Integer level;

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

        int leftToNextLevel = getExpLeftToNextLevel();
        while (exp > leftToNextLevel) {
            exp -= leftToNextLevel;
            this.experience += leftToNextLevel;
            this.levelUp();
            leftToNextLevel = getExpLeftToNextLevel();
        }

        this.experience += exp;
    }

    private int getExpLeftToNextLevel() {
        return (this.level * LEVEL_EXP_DIFF) - this.experience;
    }

    public abstract void useSpecialAbility() throws LogicException;
}
