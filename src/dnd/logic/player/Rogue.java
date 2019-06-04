package dnd.logic.player;

import dnd.dto.levelup.MageLevelUpDTO;
import dnd.dto.levelup.RougeLevelUpDTO;
import dnd.dto.units.MageDTO;
import dnd.dto.units.RougeDTO;
import dnd.dto.units.UnitDTO;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.LogicException;
import dnd.logic.Tick;
import dnd.logic.board.Board;
import dnd.logic.enemies.Enemy;

import java.util.List;

public class Rogue extends Player {
    private static final int LEVEL_ATTACK_DIFF = 3;
    private static final int MAX_ENERGY = 100;
    private static final int ENERGY_REGEN = 10;
    private static final int ABILITY_RANGE = 2;

    int cost;
    int currentEnergy;

    public Rogue(String name,
                 int healthPool, int attack, int defense,
                 RandomGenerator randomGenerator,
                 int cost) {
        super(name, healthPool, attack, defense, randomGenerator);
        this.init(cost);
    }

    protected Rogue(String name,
                    int healthPool, int attack, int defense,
                    RandomGenerator randomGenerator,
                    Board board,
                    int cost) {
        super(name, healthPool, attack, defense, randomGenerator, board);
        this.init(cost);
    }

    private void init(int cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("cost must be a non-negative number.");
        }

        this.cost = cost;
        this.currentEnergy = MAX_ENERGY;
    }

    @Override
    protected void levelUp() {
        RougeLevelUpDTO rougeLevelUpDTO = this.initLevelUpDto(new RougeLevelUpDTO());

        super.levelUp();
        this.currentEnergy = MAX_ENERGY;
        this.attack += this.level * LEVEL_ATTACK_DIFF;

        this.calculateLevelUpStatsDiffs(rougeLevelUpDTO);
        this.callLevelUpObservers(rougeLevelUpDTO);
    }

    @Override
    public void useSpecialAbilityCore() throws LogicException {
        if (this.currentEnergy < this.cost) {
            throw new LogicException("Cannot use special ability due insufficient energy.");
        }

        this.currentEnergy -= this.cost;
        List<Enemy> enemiesInRange = this.board.getEnemiesInRange(this.position, ABILITY_RANGE);
        for (Enemy enemy : enemiesInRange) {
            super.attack(enemy, this.attack);
        }
    }

    @Override
    protected String getSpecialAbilityName() {
        return "Fan of Knives";
    }

    @Override
    public void onTick(Tick current) {
        this.currentEnergy = Math.min(this.currentEnergy + ENERGY_REGEN, MAX_ENERGY);
    }

    @Override
    public UnitDTO createDTO() {
        RougeDTO rougeDTO = new RougeDTO();
        this.fillPlayerDtoFields(rougeDTO);
        rougeDTO.currentEnergy = this.currentEnergy;
        rougeDTO.maxEnergy = MAX_ENERGY;
        return  rougeDTO;
    }
}
