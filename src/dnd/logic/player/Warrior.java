package dnd.logic.player;

import dnd.logic.LogicException;
import dnd.logic.Tick;

public class Warrior extends Player {
    private static final int LEVEL_HEALTH_DIFF = 5;
    private static final int LEVEL_DEFENSE_DIFF = 1;

    private static final int ABILITY_HEAL_POWER = 2;

    private static final Tick AbilityReady = new Tick(0);

    Tick coolDown;
    Tick remaining;

    public Warrior(Tick cooldown) {
        if (cooldown == null) {
           throw new IllegalArgumentException("cooldown cannot be null.");
        }
        if (AbilityReady.isGreaterThan(cooldown)) {
            throw new IllegalArgumentException("cooldown must be a non-negative number.");
        }

        this.coolDown = cooldown;
        this.resetCooldown();
    }

    private void resetCooldown() {
        this.remaining = AbilityReady;
    }

    @Override
    protected void levelUp() {
        super.levelUp();
        this.resetCooldown();
        this.healthPool = this.level * LEVEL_HEALTH_DIFF;
        this.defense += this.level * LEVEL_DEFENSE_DIFF;
    }

    @Override
    public void useSpecialAbility() throws LogicException {
        if (this.remaining.isGreaterThan(AbilityReady)) {
            throw new LogicException("Cannot use the special ability because the cooldown isn't ready.");
        }

        this.remaining = this.coolDown;
        this.currentHealth += Math.min(this.currentHealth + (this.defense * ABILITY_HEAL_POWER), this.healthPool);
    }

    @Override
    public void onTick(Tick current) {
        if (this.remaining.isGreaterThan(AbilityReady)) {
            this.remaining = new Tick(this.remaining.getValue() - 1);
        }
    }
}
