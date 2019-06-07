package dnd.logic.player;

import dnd.GameException;
import dnd.dto.levelup.RougeLevelUpDTO;
import dnd.dto.units.RogueDTO;
import dnd.dto.units.UnitDTO;
import dnd.logic.LogicException;
import dnd.logic.Point;
import dnd.logic.Tick;
import dnd.logic.board.Board;
import dnd.logic.enemies.Enemy;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.TileOccupier;

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
                 int cost) {
        super(name, healthPool, attack, defense);
        this.init(cost);
    }

    protected Rogue(String name,
                    int healthPool, int attack, int defense,
                    int experience, int level,
                    int cost,
                    Point position,
                    RandomGenerator randomGenerator,
                    Board board) {
        super(name, healthPool, attack, defense, experience, level, randomGenerator, board);
        this.init(cost);
        this.position = position;
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
    public void useSpecialAbilityCore() throws GameException {
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
        RogueDTO rogueDTO = new RogueDTO();
        this.fillPlayerDtoFields(rogueDTO);
        rogueDTO.currentEnergy = this.currentEnergy;
        rogueDTO.maxEnergy = MAX_ENERGY;
        return rogueDTO;
    }

    @Override
    public TileOccupier clone(Point position, RandomGenerator randomGenerator, Board board) {
        return new Rogue(
                this.name,
                this.healthPool, this.attack, this.defense,
                this.experience, this.level,
                this.cost,
                position,
                randomGenerator, board
        );
    }
}
