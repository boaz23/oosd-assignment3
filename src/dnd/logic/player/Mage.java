package dnd.logic.player;

import dnd.GameException;
import dnd.dto.levelup.MageLevelUpDTO;
import dnd.dto.units.MageDTO;
import dnd.logic.LogicException;
import dnd.logic.Tick;
import dnd.logic.enemies.Enemy;

import java.util.List;

public class Mage extends Player {
    private static final int LEVEL_MANA_DIFF = 25;
    private static final int LEVEL_SPELL_POWER_DIFF = 10;
    private static final int MANA_REGEN = 1;
    private static final int MANA_ADDITION_DIV = 4;

    private int spellPower;
    private int manaPool;
    private int currentMana;

    private int cost;
    private int hitTimes;
    private int range;

    public Mage(String name,
                int healthPool, int attack, int defense,
                int spellPower, int manaPool, int cost,
                int hitTimes, int range) {
        super(name, healthPool, attack, defense);
        init(spellPower, manaPool, cost, hitTimes, range);
    }

    private void init(int spellPower, int manaPool, int cost, int hitTimes, int range) {
        if (spellPower <= 0) {
            throw new IllegalArgumentException("spell power must be a positive number.");
        }
        if (manaPool <= 0) {
            throw new IllegalArgumentException("mana pool must be a positive number.");
        }
        if (cost < 0) {
            throw new IllegalArgumentException("cost must be a non-negative number.");
        }
        if (hitTimes <= 0) {
            throw new IllegalArgumentException("hitTimes must be a positive number.");
        }
        if (range <= 0) {
            throw new IllegalArgumentException("range must be a positive number.");
        }


        this.spellPower = spellPower;
        this.manaPool = manaPool;
        currentMana = getManaAddition();
        this.cost = cost;
        this.hitTimes = hitTimes;
        this.range = range;
    }

    @Override
    protected void levelUp() {
        MageLevelUpDTO mageLevelUpDTO = initLevelUpDto();

        super.levelUp();
        manaPool += level * LEVEL_MANA_DIFF;
        currentMana = Math.min(currentMana + getManaAddition(), manaPool);
        spellPower += level * LEVEL_SPELL_POWER_DIFF;

        calculateLevelUpStatsDiffs(mageLevelUpDTO);
        callLevelUpObservers(mageLevelUpDTO);
    }

    private MageLevelUpDTO initLevelUpDto() {
        MageLevelUpDTO mageLevelUpDTO = initLevelUpDto(new MageLevelUpDTO());
        mageLevelUpDTO.manaPoolBonus = manaPool;
        mageLevelUpDTO.spellPowerBonus = spellPower;
        return mageLevelUpDTO;
    }

    private void calculateLevelUpStatsDiffs(MageLevelUpDTO mageLevelUpDTO) {
        super.calculateLevelUpStatsDiffs(mageLevelUpDTO);
        mageLevelUpDTO.manaPoolBonus = manaPool - mageLevelUpDTO.manaPoolBonus;
        mageLevelUpDTO.spellPowerBonus = spellPower - mageLevelUpDTO.spellPowerBonus;
    }

    private int getManaAddition() {
        return manaPool / MANA_ADDITION_DIV;
    }

    @Override
    public void useSpecialAbility() throws GameException {
        if (currentMana < cost) {
            throw new LogicException("Cannot use special ability due insufficient mana.");
        }

        super.useSpecialAbility();
    }

    @Override
    protected void useSpecialAbilityCore() throws GameException {
        currentMana -= cost;
        List<Enemy> enemiesInRange = board.getEnemiesInRange(position, range);
        for (int hits = 0; hits < hitTimes & !enemiesInRange.isEmpty(); hits++) {
            int enemyIndex = randomGenerator.nextInt(enemiesInRange.size() - 1);
            Enemy enemy = enemiesInRange.get(enemyIndex);
            boolean died = attack(enemy, spellPower);
            if (died) {
                enemiesInRange.remove(enemyIndex);
            }
        }
    }

    @Override
    protected String getSpecialAbilityName() {
        return "Blizzard";
    }

    @Override
    public void onTick(Tick current) {
        currentMana = Math.min(manaPool, currentMana + MANA_REGEN);
    }

    @Override
    public MageDTO createDTO() {
        MageDTO mageDTO = new MageDTO();
        fillPlayerDtoFields(mageDTO);
        mageDTO.currentMana = currentMana;
        mageDTO.manaPool = manaPool;
        mageDTO.spellPower = spellPower;
        return mageDTO;
    }

    @Override
    public Mage clone() {
        return new Mage(
            name,
            healthPool, attack, defense,
            spellPower, manaPool, cost,
            hitTimes, range
        );
    }
}
