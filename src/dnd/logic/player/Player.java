package dnd.logic.player;

import dnd.RandomGenerator;
import dnd.logic.LogicException;
import dnd.logic.TileProperty;
import dnd.logic.Unit;
import dnd.logic.enemies.Enemy;

public abstract class Player extends Unit {
    private static final int LEVEL_EXP_DIFF = 50;
    private static final int LEVEL_HEALTH_DIFF = 10;
    private static final int LEVEL_ATTACK_DIFF = 5;
    private static final int LEVEL_DEFENSE_DIFF = 2;

    protected int experience;
    protected int level;

    public Player(String name,
                  int healthPool, int attack, int defense,
                  UnitsInRangeFinder unitsInRangeFinder,
                  RandomGenerator randomGenerator) {
        super(name, healthPool, attack, defense, unitsInRangeFinder, randomGenerator);
        this.addProperty(TileProperty.Player);
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
    protected boolean attack(Unit unit, int damage) {
        boolean hasUnitDied = super.attack(unit, damage);
        if (hasUnitDied & unit.getUnitType() == UnitType.Enemy) {
            Enemy enemy = (Enemy)unit;
            this.gainExp(enemy.getExperienceValue());
        }

        return hasUnitDied;
    }
}
