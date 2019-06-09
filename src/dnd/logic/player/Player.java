package dnd.logic.player;

import dnd.cli.view.GameEventObserver;
import dnd.logic.GameException;
import dnd.dto.levelup.LevelUpDTO;
import dnd.dto.units.PlayerDTO;
import dnd.logic.DeathObserver;
import dnd.logic.LogicException;
import dnd.logic.MoveResult;
import dnd.logic.enemies.Enemy;
import dnd.logic.tileOccupiers.TileVisitor;
import dnd.logic.tileOccupiers.Unit;

public abstract class Player extends Unit {
    private static final int LEVEL_EXP_DIFF = 50;
    private static final int LEVEL_HEALTH_DIFF = 10;
    private static final int LEVEL_ATTACK_DIFF = 5;
    private static final int LEVEL_DEFENSE_DIFF = 2;

    int experience;
    int level;

    Player(String name,
           int healthPool, int attack, int defense) {
        super(name, healthPool, attack, defense);
        init();
    }

    private void init() {
        level = 1;
    }

    @Override
    public char toTileChar() {
        return '@';
    }

    @Override
    public MoveResult accept(TileVisitor visitor) throws GameException {
        return visitor.visit(this);
    }

    @Override
    public MoveResult visit(Player player) throws LogicException {
        throw new LogicException("player fights another player");
    }

    @Override
    public MoveResult visit(Enemy enemy) throws GameException {
        return moveMeleeAttack(enemy);
    }

    public void useSpecialAbility() throws GameException {
        callSpecialAbilityUseObservers();
        useSpecialAbilityCore();
    }

    protected abstract void useSpecialAbilityCore() throws GameException;

    @Override
    public boolean defend(Unit attacker, int damage) throws GameException {
        boolean died = super.defend(attacker, damage);
        if (died) {
            callDeathObservers();
            callPlayerDeathObservers();
        }

        return died;
    }

    @Override
    public abstract Player clone();

    protected abstract String getSpecialAbilityName();

    @Override
    public abstract PlayerDTO createDTO();

    boolean attack(Enemy enemy, int damage) throws GameException {
        boolean died = attackCore(enemy, damage);
        if (died) {
            gainExp(enemy.getExperienceValue());
        }

        return died;
    }

    private MoveResult moveMeleeAttack(Enemy enemy) throws GameException {
        callOnPlayerEngageObservers(enemy);

        boolean died = meleeAttack(enemy);
        if (died) {
            gainExp(enemy.getExperienceValue());
        }

        return died ? MoveResult.Dead : MoveResult.Engaged;
    }

    private void gainExp(int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("exp gained must be a non-negative number.");
        }

        callOnExpGainObservers(exp);

        int expLeftToNextLevel = getExpLeftToNextLevel();
        while (exp >= expLeftToNextLevel) {
            exp -= expLeftToNextLevel;
            experience += expLeftToNextLevel;
            levelUp();
            expLeftToNextLevel = getExpLeftToNextLevel();
        }

        experience += exp;
    }

    void levelUp() {
        experience -= getExperienceRequiredForLevel();
        level++;

        healthPool += level * LEVEL_HEALTH_DIFF;
        currentHealth = healthPool;
        attack += level * LEVEL_ATTACK_DIFF;
        defense += level * LEVEL_DEFENSE_DIFF;
    }

    private int getExpLeftToNextLevel() {
        return getExperienceRequiredForLevel() - experience;
    }

    private int getExperienceRequiredForLevel() {
        return level * LEVEL_EXP_DIFF;
    }

    void fillPlayerDtoFields(PlayerDTO playerDTO) {
        fillUnitDtoFields(playerDTO);
        playerDTO.level = level;
        playerDTO.experience = experience;
        playerDTO.totalExperienceToNextLevel = getExperienceRequiredForLevel();
        playerDTO.specialAbilityName = getSpecialAbilityName();
    }

    <T extends LevelUpDTO> T initLevelUpDto(T levelUpDTO) {
        levelUpDTO.healthBonus = healthPool;
        levelUpDTO.attackBonus = attack;
        levelUpDTO.defenseBonus = defense;
        return levelUpDTO;
    }

    void calculateLevelUpStatsDiffs(LevelUpDTO levelUpDTO) {
        levelUpDTO.healthBonus = healthPool - levelUpDTO.healthBonus;
        levelUpDTO.attackBonus = attack - levelUpDTO.attackBonus;
        levelUpDTO.defenseBonus = defense - levelUpDTO.defenseBonus;
    }

    private void callSpecialAbilityUseObservers() {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onCastingSpecialAbility(createDTO());
        }
    }

    private void callOnExpGainObservers(int exp) {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onExpGain(createDTO(), exp);
        }
    }

    void callLevelUpObservers(LevelUpDTO levelUpDTO) {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onLevelUp(levelUpDTO.clone());
        }
    }

    private void callOnPlayerEngageObservers(Enemy enemy) {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onPlayerEngage(createDTO(), enemy.createDTO());
        }
    }

    private void callDeathObservers() throws GameException {
        for (DeathObserver observer : deathObservers) {
            observer.onDeath(this);
        }
    }

    private void callPlayerDeathObservers() {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onPlayerDeath(createDTO());
        }
    }
}
