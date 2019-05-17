package dnd.logic.player;

import dnd.RandomGenerator;
import dnd.logic.LogicException;
import dnd.logic.Tick;
import dnd.logic.enemies.Enemy;

import java.util.List;

public class Mage extends Player {
    private static final int LEVEL_MANA_DIFF = 25;
    private static final int LEVEL_SPELL_POWER_DIFF = 10;
    private static final int MANA_REGEN = 1;
    private static final int MANA_ADDITION_DIV = 4;

    private final RandomGenerator randomGenerator;

    int spellPower;
    int manaPool;
    int currentMana;
    int cost;
    int hitTimes;
    int range;

    public Mage(int spellPower, int manaPool, int cost, int hitTimes, int range, RandomGenerator randomGenerator) {
        if (randomGenerator == null) {
            throw new IllegalArgumentException("random generator cannot be null.");
        }
        if (spellPower <= 0) {
            throw new IllegalArgumentException("spell power must be a positive number.");
        }
        if (manaPool <= 0) {
            throw new IllegalArgumentException("mana pool must be a positive number.");
        }
        if (cost < 0) {
            throw new IllegalArgumentException("cost must be a non-negative number.");
        }
        if (hitTimes <= 0) {
            throw new IllegalArgumentException("hitTimes must be a positive number.");
        }
        if (range <= 0) {
            throw new IllegalArgumentException("range must be a positive number.");
        }


        this.spellPower = spellPower;
        this.manaPool = manaPool;
        this.currentMana = getManaAddition();
        this.cost = cost;
        this.hitTimes = hitTimes;
        this.range = range;

        this.randomGenerator = randomGenerator;
    }

    @Override
    protected void levelUp() {
        super.levelUp();
        this.manaPool += this.level * LEVEL_MANA_DIFF;
        this.currentMana = Math.min(this.currentMana + this.getManaAddition(), this.manaPool);
        this.spellPower += this.level * LEVEL_SPELL_POWER_DIFF;
    }

    @Override
    public void useSpecialAbility() throws LogicException {
        if (this.currentMana < cost) {
            throw new LogicException("Cannot use special ability due insufficient mana.");
        }

        this.currentMana -= this.cost;
        int hits = 0;
        // TODO: get enemies in range
        List<Enemy> enemiesInRange = getEnemiesInRange(this.range);
        while (hits < this.hitTimes) {
            // TODO: figure out what 'an enemy can defend itself' mean
            int enemyIndex = randomGenerator.nextInt(enemiesInRange.size());
            enemiesInRange.get(enemyIndex).takeDamage(this.spellPower);
            hits++;
        }
    }

    @Override
    public void onTick(Tick current) {
        this.currentMana = Math.min(this.manaPool, this.currentMana + MANA_REGEN);
    }

    private int getManaAddition() {
        return this.manaPool / MANA_ADDITION_DIV;
    }
}
