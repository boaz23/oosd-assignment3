package logic.player;

import logic.Unit;

public abstract class Player extends Unit {
    private Integer experience;
    private Integer level;

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void levelUp() {
        experience = experience - (50 * level);
        level++;
        setHealthPool(getHealthPool() + (10 * level));
        setCurrentHealth(getHealthPool());
        setAttackPoints(getAttackPoints() + (5 * level));
        setDefense(getDefense() + (2 * level));
    }
    public abstract void useSpecialAbility();
}
