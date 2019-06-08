package dnd.logic.player;

import dnd.GameException;
import dnd.dto.levelup.RogueLevelUpDTO;
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

    private int cost;
    private int currentEnergy;

    public Rogue(String name,
                 int healthPool, int attack, int defense,
                 int cost) {
        super(name, healthPool, attack, defense);
        init(cost);
    }

    private Rogue(String name,
                  int healthPool, int attack, int defense,
                  int experience, int level,
                  int cost,
                  Point position,
                  RandomGenerator randomGenerator,
                  Board board) {
        super(name, healthPool, attack, defense, experience, level, randomGenerator, board);
        init(cost);
        this.position = position;
    }

    private void init(int cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("cost must be a non-negative number.");
        }

        this.cost = cost;
        currentEnergy = MAX_ENERGY;
    }

    @Override
    protected void levelUp() {
        RogueLevelUpDTO rogueLevelUpDTO = initLevelUpDto(new RogueLevelUpDTO());

        super.levelUp();
        currentEnergy = MAX_ENERGY;
        attack += level * LEVEL_ATTACK_DIFF;

        calculateLevelUpStatsDiffs(rogueLevelUpDTO);
        callLevelUpObservers(rogueLevelUpDTO);
    }

    @Override
    public void useSpecialAbilityCore() throws GameException {
        if (currentEnergy < cost) {
            throw new LogicException("Cannot use special ability due insufficient energy.");
        }

        currentEnergy -= cost;
        List<Enemy> enemiesInRange = board.getEnemiesInRange(position, ABILITY_RANGE);
        for (Enemy enemy : enemiesInRange) {
            attack(enemy, attack);
        }
    }

    @Override
    protected String getSpecialAbilityName() {
        return "Fan of Knives";
    }

    @SuppressWarnings("unused")
    @Override
    public void onTick(Tick current) {
        currentEnergy = Math.min(currentEnergy + ENERGY_REGEN, MAX_ENERGY);
    }

    @Override
    public UnitDTO createDTO() {
        RogueDTO rogueDTO = new RogueDTO();
        fillPlayerDtoFields(rogueDTO);
        rogueDTO.currentEnergy = currentEnergy;
        rogueDTO.maxEnergy = MAX_ENERGY;
        return rogueDTO;
    }

    @Override
    public TileOccupier clone(Point position, RandomGenerator randomGenerator, Board board) {
        return new Rogue(
            name,
            healthPool, attack, defense,
            experience, level,
            cost,
            position,
            randomGenerator, board
        );
    }
}
