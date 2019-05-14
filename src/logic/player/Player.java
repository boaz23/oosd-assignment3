package logic.player;

import logic.Unit;

public abstract class Player extends Unit {
    private Integer experience;
    private Integer level;

    public abstract void levelUp();
    public abstract void useSpecialAbility();
}
