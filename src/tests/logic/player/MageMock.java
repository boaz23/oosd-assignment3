package tests.logic.player;

import dnd.logic.player.Mage;

public class MageMock extends Mage {
    public MageMock(String name, int healthPool, int attack, int defense, int spellPower, int manaPool, int cost, int hitTimes, int range) {
        super(name, healthPool, attack, defense, spellPower, manaPool, cost, hitTimes, range);
    }

    public void setCurrentMana(int mana) {
        currentMana = mana;
    }

    public int getCurrentMana() {
        return currentMana;
    }
}
