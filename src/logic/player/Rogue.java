package logic.player;

import interfaces.Observable;

public class Rogue extends Player {

    private Integer cost;
    private Integer currentEnergy;

    public void Rogue(Integer cost) {
        this.cost = cost;
        currentEnergy = 100;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getCurrentEnergy() {
        return currentEnergy;
    }

    public void setCurrentEnergy(Integer currentEnergy) {
        if (currentEnergy <= 100)
            this.currentEnergy = currentEnergy;
        else
            //exeption?
    }

    public void levelUp() {
        currentEnergy = 100;
        setAttackPoints(getAttackPoints() + (3 * getLevel()));
    }

    public void useSpecialAbility() {

    }

    public void callback(Observable<TEventArgs> observable, TEventArgs args){

    }
}
