package dnd.logic.player;

import dnd.GameEventObserver;
import dnd.GameException;
import dnd.dto.levelup.LevelUpDTO;
import dnd.dto.units.EnemyDTO;
import dnd.dto.units.PlayerDTO;
import dnd.logic.DeathObserver;
import dnd.logic.LogicException;
import dnd.logic.MoveResult;
import dnd.logic.board.Board;
import dnd.logic.enemies.Enemy;
import dnd.logic.random_generator.RandomGenerator;
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

    Player(String name,
           int healthPool, int attack, int defense,
           int experience, int level,
           RandomGenerator randomGenerator,
           Board board) {
        super(name, healthPool, attack, defense, randomGenerator, board);
        init(experience, level);
    }

    private void init() {
        level = 1;
    }

    private void init(int experience, int level) {
        this.experience = experience;
        this.level = level;
    }

    @Override
    public char toTileChar() {
        return '@';
    }

    void callLevelUpObservers(LevelUpDTO levelUpDTO) {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onLevelUp(levelUpDTO.clone());
        }
    }

    void levelUp() {
        experience -= getExperienceRequiredForLevel();
        level++;

        healthPool += level * LEVEL_HEALTH_DIFF;
        currentHealth = healthPool;
        attack += level * LEVEL_ATTACK_DIFF;
        defense += level * LEVEL_DEFENSE_DIFF;
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

    private void callOnExpGainObservers(int exp) {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onExpGain((PlayerDTO)createDTO(), exp);
        }
    }

    private int getExpLeftToNextLevel() {
        return getExperienceRequiredForLevel() - experience;
    }

    private int getExperienceRequiredForLevel() {
        return level * LEVEL_EXP_DIFF;
    }

    public void useSpecialAbility() throws GameException {
        callSpecialAbilityUseObservers();

        useSpecialAbilityCore();
    }

    private void callSpecialAbilityUseObservers() {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onCastingSpecialAbility((PlayerDTO)createDTO());
        }
    }

    protected abstract void useSpecialAbilityCore() throws GameException;

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

    private void callOnPlayerEngageObservers(Enemy enemy) {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onPlayerEngage((PlayerDTO)createDTO(), (EnemyDTO)enemy.createDTO());
        }
    }

    private void callDeathObservers() throws GameException {
        for (DeathObserver observer : deathObservers) {
            observer.onDeath(this);
        }
    }

    @Override
    public boolean defend(Unit attacker, int damage) throws GameException {
        boolean died = super.defend(attacker, damage);
        if (died) {
            callDeathObservers();
            callPlayerDeathObservers();
        }

        return died;
    }

    private void callPlayerDeathObservers() {
        for (GameEventObserver observer : gameEventObservers) {
            observer.onPlayerDeath((PlayerDTO)createDTO());
        }
    }

    protected abstract String getSpecialAbilityName();

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
}
