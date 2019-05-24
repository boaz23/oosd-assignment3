package dnd.logic.player;

import dnd.RandomGenerator;
import dnd.logic.LogicException;
import dnd.logic.Tick;
import dnd.logic.board.Board;

public class Warrior extends Player {
    private static final int LEVEL_HEALTH_DIFF = 5;
    private static final int LEVEL_DEFENSE_DIFF = 1;

    private static final int ABILITY_HEAL_POWER = 2;

    Tick coolDown;
    Tick remaining;

    public Warrior(String name,
                   int healthPool, int attack, int defense,
                   RandomGenerator randomGenerator,
                   Tick cooldown) {
        super(name, healthPool, attack, defense, randomGenerator);
        this.init(cooldown);
    }

    protected Warrior(String name,
                      int healthPool, int attack, int defense,
                      RandomGenerator randomGenerator,
                      Board board,
                      Tick cooldown) {
        super(name, healthPool, attack, defense, randomGenerator, board);
        this.init(cooldown);
    }

    private void init(Tick cooldown) {
        if (cooldown == null) {
           throw new IllegalArgumentException("cooldown cannot be null.");
        }

        this.coolDown = cooldown;
        this.resetCooldown();
    }

    private void resetCooldown() {
        this.remaining = Tick.Zero;
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
        if (this.remaining.isGreaterThan(Tick.Zero)) {
            throw new LogicException("Cannot use the special ability because the cooldown isn't ready.");
        }

        this.remaining = this.coolDown;
        this.currentHealth += Math.min(this.currentHealth + (this.defense * ABILITY_HEAL_POWER), this.healthPool);
    }

    @Override
    public void onTick(Tick current) {
        this.remaining = this.remaining.decrement();
    }
}
