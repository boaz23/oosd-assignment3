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
        this.init();
    }

    Player(String name,
           int healthPool, int attack, int defense,
           int experience, int level,
           RandomGenerator randomGenerator,
           Board board) {
        super(name, healthPool, attack, defense, randomGenerator, board);
        this.init(experience, level);
    }

    private void init() {
        this.level = 1;
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
        for (GameEventObserver observer : this.gameEventObservers) {
            observer.onLevelUp(levelUpDTO.clone());
        }
    }

    void levelUp() {
        this.experience -= getExperienceRequiredForLevel();
        this.level++;

        this.healthPool += this.level * LEVEL_HEALTH_DIFF;
        this.currentHealth = this.healthPool;
        this.attack += this.level * LEVEL_ATTACK_DIFF;
        this.defense += this.level * LEVEL_DEFENSE_DIFF;
    }

    private void gainExp(int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("exp gained must be a non-negative number.");
        }

        this.callOnExpGainObservers(exp);

        int expLeftToNextLevel = getExpLeftToNextLevel();
        while (exp >= expLeftToNextLevel) {
            exp -= expLeftToNextLevel;
            this.experience += expLeftToNextLevel;
            this.levelUp();
            expLeftToNextLevel = getExpLeftToNextLevel();
        }

        this.experience += exp;
    }

    private void callOnExpGainObservers(int exp) {
        for (GameEventObserver observer : this.gameEventObservers) {
            observer.onExpGain((PlayerDTO)this.createDTO(), exp);
        }
    }

    private int getExpLeftToNextLevel() {
        return this.getExperienceRequiredForLevel() - this.experience;
    }

    private int getExperienceRequiredForLevel() {
        return this.level * LEVEL_EXP_DIFF;
    }

    public void useSpecialAbility() throws GameException {
        this.callSpecialAbilityUseObservers();

        this.useSpecialAbilityCore();
    }

    private void callSpecialAbilityUseObservers() {
        for (GameEventObserver observer : this.gameEventObservers) {
            observer.onCastingSpecialAbility((PlayerDTO)this.createDTO());
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
        return this.moveMeleeAttack(enemy);
    }

    boolean attack(Enemy enemy, int damage) throws GameException {
        boolean died = super.attackCore(enemy, damage);
        if (died) {
            this.gainExp(enemy.getExperienceValue());
        }

        return died;
    }

    private MoveResult moveMeleeAttack(Enemy enemy) throws GameException {
        this.callOnPlayerEngageObservers(enemy);

        boolean died = super.meleeAttack(enemy);
        if (died) {
            this.gainExp(enemy.getExperienceValue());
        }

        return died ? MoveResult.Dead : MoveResult.Engaged;
    }

    private void callOnPlayerEngageObservers(Enemy enemy) {
        for (GameEventObserver observer : this.gameEventObservers) {
            observer.onPlayerEngage((PlayerDTO)this.createDTO(), (EnemyDTO)enemy.createDTO());
        }
    }

    private void callDeathObservers() throws GameException {
        for (DeathObserver observer : this.deathObservers) {
            observer.onDeath(this);
        }
    }

    @Override
    public boolean defend(Unit attacker, int damage) throws GameException {
        boolean died = super.defend(attacker, damage);
        if (died) {
            this.callDeathObservers();
            this.callPlayerDeathObservers();
        }

        return died;
    }

    private void callPlayerDeathObservers() {
        for (GameEventObserver observer : this.gameEventObservers) {
            observer.onPlayerDeath((PlayerDTO)this.createDTO());
        }
    }

    protected abstract String getSpecialAbilityName();

    void fillPlayerDtoFields(PlayerDTO playerDTO) {
        this.fillUnitDtoFields(playerDTO);
        playerDTO.level = this.level;
        playerDTO.experience = this.experience;
        playerDTO.totalExperienceToNextLevel = this.getExperienceRequiredForLevel();
        playerDTO.specialAbilityName = this.getSpecialAbilityName();
    }

    <T extends LevelUpDTO> T initLevelUpDto(T levelUpDTO) {
        levelUpDTO.healthBonus = this.healthPool;
        levelUpDTO.attackBonus = this.attack;
        levelUpDTO.defenseBonus = this.defense;
        return levelUpDTO;
    }

    void calculateLevelUpStatsDiffs(LevelUpDTO levelUpDTO) {
        levelUpDTO.healthBonus = this.healthPool - levelUpDTO.healthBonus;
        levelUpDTO.attackBonus = this.attack - levelUpDTO.attackBonus;
        levelUpDTO.defenseBonus = this.defense - levelUpDTO.defenseBonus;
    }
}
