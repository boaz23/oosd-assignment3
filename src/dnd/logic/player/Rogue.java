package dnd.logic.player;

import dnd.RandomGenerator;
import dnd.logic.*;
import dnd.logic.enemies.Enemy;

import java.util.List;

public class Rogue extends Player {
    private static final int LEVEL_ATTACK_DIFF = 3;
    private static final int MAX_ENERGY = 100;
    private static final int ENERGY_REGEN = 10;
    private static final int ABILITY_RANGE = 2;

    final int cost;
    int currentEnergy;

    public Rogue(String name,
                 int healthPool, int attack, int defense,
                 UnitsInRangeFinder unitsInRangeFinder,
                 RandomGenerator randomGenerator,
                 int cost) {
        super(name, healthPool, attack, defense, unitsInRangeFinder, randomGenerator);

        if (cost < 0) {
            throw new IllegalArgumentException("cost must be a non-negative number.");
        }

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
        List<Unit> enemiesInRange = this.unitsInRangeFinder.findUnitsInRange(this.position, ABILITY_RANGE, UnitType.Enemy);
        for (Unit enemy : enemiesInRange) {
            // TODO: figure out what 'an enemy can defend itself' mean
            enemy.takeDamage(this.attack);
        }
    }

    @Override
    public void onTick(Tick current) {
        this.currentEnergy = Math.min(this.currentEnergy + ENERGY_REGEN, MAX_ENERGY);
    }
}
