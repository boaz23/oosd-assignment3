package dnd.logic.player;

import dnd.dto.levelup.WarriorLevelUpDTO;
import dnd.dto.units.UnitDTO;
import dnd.dto.units.WarriorDTO;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.LogicException;
import dnd.logic.Tick;
import dnd.logic.board.Board;
import dnd.logic.tileOccupiers.TileOccupier;

public class Warrior extends Player {
    private static final int LEVEL_HEALTH_DIFF = 5;
    private static final int LEVEL_DEFENSE_DIFF = 1;

    private static final int ABILITY_HEAL_POWER = 2;

    Tick coolDown;
    Tick remaining;

    public Warrior(String name,
                   int healthPool, int attack, int defense,
                   Tick cooldown) {
        super(name, healthPool, attack, defense);
        this.init(cooldown);
    }

    protected Warrior(String name,
                      int healthPool, int attack, int defense,
                      Tick cooldown,
                      RandomGenerator randomGenerator,
                      Board board) {
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
        WarriorLevelUpDTO rougeLevelUpDTO = this.initLevelUpDto(new WarriorLevelUpDTO());

        super.levelUp();
        this.resetCooldown();
        this.healthPool = this.level * LEVEL_HEALTH_DIFF;
        this.defense += this.level * LEVEL_DEFENSE_DIFF;

        this.calculateLevelUpStatsDiffs(rougeLevelUpDTO);
        this.callLevelUpObservers(rougeLevelUpDTO);
    }

    @Override
    public void useSpecialAbilityCore() throws LogicException {
        if (this.remaining.isGreaterThan(Tick.Zero)) {
            throw new LogicException("Cannot use the special ability because the cooldown isn't ready.");
        }

        this.remaining = this.coolDown;
        this.currentHealth += Math.min(this.currentHealth + (this.defense * ABILITY_HEAL_POWER), this.healthPool);
    }

    @Override
    protected String getSpecialAbilityName() {
        return "Heal";
    }

    @Override
    public void onTick(Tick current) {
        this.remaining = this.remaining.decrement();
    }

    @Override
    public UnitDTO createDTO() {
        WarriorDTO warriorDTO = new WarriorDTO();
        this.fillPlayerDtoFields(warriorDTO);
        warriorDTO.abilityCooldown = this.coolDown.getValue();
        warriorDTO.remaining = this.remaining.getValue();
        return  warriorDTO;
    }

    @Override
    public TileOccupier clone(RandomGenerator randomGenerator, Board board) {
        return new Warrior(
                this.name,
                this.healthPool, this.attack, this.defense,
                this.coolDown,
                randomGenerator, board
        );
    }
}
