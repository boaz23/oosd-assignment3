package dnd.logic.player;

import dnd.dto.levelup.MageLevelUpDTO;
import dnd.dto.units.MageDTO;
import dnd.dto.units.UnitDTO;
import dnd.logic.LogicException;
import dnd.logic.Point;
import dnd.logic.Tick;
import dnd.logic.board.Board;
import dnd.logic.enemies.Enemy;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.TileOccupier;

import java.util.List;

public class Mage extends Player {
    private static final int LEVEL_MANA_DIFF = 25;
    private static final int LEVEL_SPELL_POWER_DIFF = 10;
    private static final int MANA_REGEN = 1;
    private static final int MANA_ADDITION_DIV = 4;

    int spellPower;
    int manaPool;
    int currentMana;

    int cost;
    int hitTimes;
    int range;

    public Mage(String name,
                int healthPool, int attack, int defense,
                int spellPower, int manaPool, int cost,
                int hitTimes, int range) {
        super(name, healthPool, attack, defense);
        this.init(spellPower, manaPool, cost, hitTimes, range);
    }

    protected Mage(String name,
                   int healthPool, int attack, int defense,
                   int spellPower, int manaPool, int cost,
                   int hitTimes, int range,
                   Point position,
                   RandomGenerator randomGenerator,
                   Board board) {
        super(name, healthPool, attack, defense, randomGenerator, board);
        this.init(spellPower, manaPool, cost, hitTimes, range);
        this.position = position;
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
        this.currentMana = this.getManaAddition();
        this.cost = cost;
        this.hitTimes = hitTimes;
        this.range = range;
    }

    @Override
    protected void levelUp() {
        MageLevelUpDTO mageLevelUpDTO = this.initLevelUpDto();

        super.levelUp();
        this.manaPool += this.level * LEVEL_MANA_DIFF;
        this.currentMana = Math.min(this.currentMana + this.getManaAddition(), this.manaPool);
        this.spellPower += this.level * LEVEL_SPELL_POWER_DIFF;

        this.calculateLevelUpStatsDiffs(mageLevelUpDTO);
        this.callLevelUpObservers(mageLevelUpDTO);
    }

    private MageLevelUpDTO initLevelUpDto() {
        MageLevelUpDTO mageLevelUpDTO = this.initLevelUpDto(new MageLevelUpDTO());
        mageLevelUpDTO.manaPoolBonus = this.manaPool;
        mageLevelUpDTO.spellPowerBonus = this.spellPower;
        return mageLevelUpDTO;
    }

    private void calculateLevelUpStatsDiffs(MageLevelUpDTO mageLevelUpDTO) {
        super.calculateLevelUpStatsDiffs(mageLevelUpDTO);
        mageLevelUpDTO.manaPoolBonus = this.manaPool - mageLevelUpDTO.manaPoolBonus;
        mageLevelUpDTO.spellPowerBonus = this.spellPower - mageLevelUpDTO.spellPowerBonus;
    }

    private int getManaAddition() {
        return this.manaPool / MANA_ADDITION_DIV;
    }

    @Override
    public void useSpecialAbilityCore() throws LogicException {
        if (this.currentMana < cost) {
            throw new LogicException("Cannot use special ability due insufficient mana.");
        }

        this.currentMana -= this.cost;
        List<Enemy> enemiesInRange = this.board.getEnemiesInRange(this.position, this.range);
        if (enemiesInRange.size() > 0) {
            for (int hits = 0; hits < this.hitTimes; hits++) {
                int enemyIndex = randomGenerator.nextInt(enemiesInRange.size());
                Enemy enemy = enemiesInRange.get(enemyIndex);
                boolean died = this.attack(enemy, this.spellPower);
                if (died) {
                    enemiesInRange.remove(enemyIndex);
                }
            }
        }
    }

    @Override
    protected String getSpecialAbilityName() {
        return "Blizzard";
    }

    @Override
    public void onTick(Tick current) {
        this.currentMana = Math.min(this.manaPool, this.currentMana + MANA_REGEN);
    }

    @Override
    public UnitDTO createDTO() {
        MageDTO mageDTO = new MageDTO();
        this.fillPlayerDtoFields(mageDTO);
        mageDTO.currentMana = this.currentMana;
        mageDTO.manaPool = this.manaPool;
        mageDTO.spellPower = this.spellPower;
        return  mageDTO;
    }

    @Override
    public TileOccupier clone(Point position, RandomGenerator randomGenerator, Board board) {
        return new Mage(
                this.name,
                this.healthPool, this.attack, this.defense,
                this.spellPower, this.manaPool, this.cost,
                this.hitTimes, this.range,
                position,
                randomGenerator,
                board
        );
    }
}
