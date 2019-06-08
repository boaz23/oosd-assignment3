package dnd.logic.player;

import dnd.GameException;
import dnd.dto.levelup.WarriorLevelUpDTO;
import dnd.dto.units.WarriorDTO;
import dnd.logic.LogicException;
import dnd.logic.Tick;

public class Warrior extends Player {
    private static final int LEVEL_HEALTH_DIFF = 5;
    private static final int LEVEL_DEFENSE_DIFF = 1;

    private static final int ABILITY_HEAL_POWER = 2;

    private Tick coolDown;
    private Tick remaining;

    public Warrior(String name,
                   int healthPool, int attack, int defense,
                   Tick cooldown) {
        super(name, healthPool, attack, defense);
        init(cooldown);
    }

    private void init(Tick cooldown) {
        if (cooldown == null) {
            throw new IllegalArgumentException("cooldown cannot be null.");
        }

        coolDown = cooldown;
        resetCooldown();
    }

    private void resetCooldown() {
        remaining = Tick.Zero;
    }

    @Override
    protected void levelUp() {
        WarriorLevelUpDTO warriorLevelUpDTO = initLevelUpDto(new WarriorLevelUpDTO());

        super.levelUp();
        resetCooldown();
        healthPool = level * LEVEL_HEALTH_DIFF;
        defense += level * LEVEL_DEFENSE_DIFF;

        calculateLevelUpStatsDiffs(warriorLevelUpDTO);
        callLevelUpObservers(warriorLevelUpDTO);
    }

    @Override
    public void useSpecialAbility() throws GameException {
        if (remaining.isGreaterThan(Tick.Zero)) {
            throw new LogicException("Cannot use the special ability because the cooldown isn't ready.");
        }

        super.useSpecialAbility();
    }

    @Override
    protected void useSpecialAbilityCore() {
        remaining = coolDown;
        currentHealth += Math.min(currentHealth + (defense * ABILITY_HEAL_POWER), healthPool);
    }

    @Override
    protected String getSpecialAbilityName() {
        return "Heal";
    }

    @Override
    public void onTick(Tick current) {
        remaining = remaining.decrement();
    }

    @Override
    public WarriorDTO createDTO() {
        WarriorDTO warriorDTO = new WarriorDTO();
        fillPlayerDtoFields(warriorDTO);
        warriorDTO.abilityCooldown = coolDown.getValue();
        warriorDTO.remaining = remaining.getValue();
        return warriorDTO;
    }

    @Override
    public Warrior clone() {
        return new Warrior(
            name,
            healthPool, attack, defense,
            coolDown
        );
    }
}
