package dnd.logic.player;

import dnd.dto.levelup.RogueLevelUpDTO;
import dnd.dto.units.RogueDTO;
import dnd.logic.GameException;
import dnd.logic.LogicException;
import dnd.logic.Tick;
import dnd.logic.enemies.Enemy;

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

    private void init(int cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("cost must be a non-negative number.");
        }

        this.cost = cost;
        currentEnergy = MAX_ENERGY;
    }

    @Override
    public void onTick(Tick current) {
        currentEnergy = Math.min(currentEnergy + ENERGY_REGEN, MAX_ENERGY);
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
    public void useSpecialAbility() throws GameException {
        if (currentEnergy < cost) {
            throw new LogicException("Cannot use special ability due insufficient energy.");
        }

        super.useSpecialAbility();
    }

    @Override
    protected void useSpecialAbilityCore() throws GameException {
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

    @Override
    public RogueDTO createDTO() {
        RogueDTO rogueDTO = new RogueDTO();
        fillPlayerDtoFields(rogueDTO);
        rogueDTO.currentEnergy = currentEnergy;
        rogueDTO.maxEnergy = MAX_ENERGY;
        return rogueDTO;
    }

    @Override
    public Rogue clone() {
        return new Rogue(
            name,
            healthPool, attack, defense,
            cost
        );
    }
}
