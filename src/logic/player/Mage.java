package logic.player;

import interfaces.Observable;

public class Mage extends Player {

    private Integer spellPower;
    private Integer manaPool;
    private Integer currentMana;
    private Integer cost;
    private Integer hitTimes;
    private Integer range;

    public void Mage(Integer spellPower, Integer manaPool) {
        this.spellPower = spellPower;
        this.manaPool = manaPool;
        currentMana = manaPool/4;
    }

    public Integer getSpellPower() {
        return spellPower;
    }

    public void setSpellPower(Integer spellPower) {
        this.spellPower = spellPower;
    }

    public Integer getManaPool() {
        return manaPool;
    }

    public void setManaPool(Integer manaPool) {
        this.manaPool = manaPool;
    }

    public Integer getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(Integer currentMana) {
        this.currentMana = currentMana;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getHitTimes() {
        return hitTimes;
    }

    public void setHitTimes(Integer hitTimes) {
        this.hitTimes = hitTimes;
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }


    public void levelUp() {
        super.levelUp();
        manaPool = manaPool + (25 * getLevel());
        currentMana = Math.min(currentMana+(manaPool/4), manaPool);
        spellPower = spellPower + (10 * getLevel());
    }

    public void useSpecialAbility() {

    }

    public void callback(Observable<TEventArgs> observable, TEventArgs args){

    }
}
