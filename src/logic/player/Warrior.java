package logic.player;

import interfaces.Observable;

public class Warrior extends Player {

    private Integer coolDown;
    private Integer remaining;

    public void Warrior(Integer coolDown) {
        this.coolDown = coolDown;
        remaining = 0;
    }

    public Integer getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(Integer coolDown) {
        this.coolDown = coolDown;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    public void levelUp() {
        super.levelUp();
        remaining = 0;
        setHealthPool(getHealthPool() + (5 * getLevel()));
        setDefense(getDefense() + getLevel());
    }

    public void useSpecialAbility() {

    }

    public void callback(Observable<TEventArgs> observable, TEventArgs args){

    }
}
