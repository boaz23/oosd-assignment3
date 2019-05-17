package dnd.logic.player;

import dnd.logic.LogicException;
import dnd.logic.Tick;
import dnd.logic.enemies.Enemy;

import java.util.List;

public class Rogue extends Player {
    private static final int LEVEL_ATTACK_DIFF = 3;
    private static final int MAX_ENERGY = 100;
    private static final int ENERGY_REGEN = 10;
    private static final int ABILITY_RANGE = 2;

    int cost;
    int currentEnergy;

    public Rogue(int cost) {
        this.cost = cost;
        this.currentEnergy = MAX_ENERGY;
    }

    @Override
    protected void levelUp() {
        super.levelUp();
        this.currentEnergy = MAX_ENERGY;
        this.attack += this.level * LEVEL_ATTACK_DIFF;
    }

    @Override
    public void useSpecialAbility() throws LogicException {
        if (this.currentEnergy < this.cost) {
            throw new LogicException("Cannot use special ability due insufficient energy.");
        }

        this.currentEnergy -= this.cost;
        // TODO: get enemies in range
        List<Enemy> enemiesInRange = getEnemiesInRange(ABILITY_RANGE);
        for (Enemy enemy : enemiesInRange) {
            enemy.takeDamage(this.attack);
        }
        // TODO: figure out what 'an enemy can defend itself' mean
    }

    @Override
    public void onTick(Tick current) {
        this.currentEnergy = Math.min(this.currentEnergy + ENERGY_REGEN, MAX_ENERGY);
    }
}
