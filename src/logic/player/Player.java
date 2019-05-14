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


    public abstract void levelUp();
    public abstract void useSpecialAbility();
}
