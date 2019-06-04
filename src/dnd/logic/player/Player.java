package dnd.logic.player;

import dnd.GameEventObserver;
import dnd.dto.levelup.LevelUpDTO;
import dnd.dto.units.EnemyDTO;
import dnd.dto.units.PlayerDTO;
import dnd.logic.DeathObserver;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.LogicException;
import dnd.logic.MoveResult;
import dnd.logic.board.Board;
import dnd.logic.enemies.Enemy;
import dnd.logic.tileOccupiers.TileVisitor;
import dnd.logic.tileOccupiers.Unit;

public abstract class Player extends Unit {
    private static final int LEVEL_EXP_DIFF = 50;
    private static final int LEVEL_HEALTH_DIFF = 10;
    private static final int LEVEL_ATTACK_DIFF = 5;
    private static final int LEVEL_DEFENSE_DIFF = 2;

    protected int experience;
    protected int level;

    public Player(String name,
                  int healthPool, int attack, int defense,
                  RandomGenerator randomGenerator) {
        super(name, healthPool, attack, defense, randomGenerator);
    }

    protected Player(String name,
                     int healthPool, int attack, int defense,
                     RandomGenerator randomGenerator,
                     Board board) {
        super(name, healthPool, attack, defense, randomGenerator, board);
    }

    @Override
    public char getTileChar() {
        return '@';
    }

    protected void callLevelUpObservers(LevelUpDTO levelUpDTO) {
        for (GameEventObserver observer : this.gameEventObservers) {
            observer.onLevelUp(levelUpDTO.clone());
        }
    }

    protected void levelUp() {
        this.experience -= getExperienceRequiredForLevel();
        this.level++;

        this.healthPool += this.level * LEVEL_HEALTH_DIFF;
        this.currentHealth = this.healthPool;
        this.attack += this.level * LEVEL_ATTACK_DIFF;
        this.defense += this.level * LEVEL_DEFENSE_DIFF;
    }

    public void gainExp(int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("exp gained must be a non-negative number.");
        }

        this.callOnExpGainObservers(exp);

        int expLeftToNextLevel = getExpLeftToNextLevel();
        while (exp > expLeftToNextLevel) {
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

    public void useSpecialAbility() throws LogicException {
        this.callSpecialAbilityUseObservers();

        this.useSpecialAbilityCore();
    }

    private void callSpecialAbilityUseObservers() {
        for (GameEventObserver observer : this.gameEventObservers) {
            observer.onCastingSpecialAbility((PlayerDTO)this.createDTO());
        }
    }

    protected abstract void useSpecialAbilityCore() throws LogicException;

    @Override
    public Object accept(TileVisitor visitor, Object state) throws LogicException {
        return visitor.visit(this, state);
    }

    @Override
    public MoveResult visit(Player player, Object state) throws LogicException {
        throw new LogicException("player fights another player");
    }

    @Override
    public MoveResult visit(Enemy enemy, Object state) throws LogicException {
        return this.moveMeleeAttack(enemy);
    }

    protected boolean attack(Enemy enemy, int damage) {
        boolean died = super.attackCore(enemy, damage);
        if (died) {
            this.gainExp(enemy.getExperienceValue());
        }

        return died;
    }

    protected MoveResult moveMeleeAttack(Enemy enemy) {
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

    @Override
    protected void callDeathObservers() {
        for (DeathObserver observer : this.deathObservers) {
            observer.onDeath(this);
        }
    }

    @Override
    public boolean defend(Unit attacker, int damage) {
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

    protected void fillPlayerDtoFields(PlayerDTO playerDTO) {
        this.fillUnitDtoFields(playerDTO);
        playerDTO.level = this.level;
        playerDTO.experience = this.experience;
        playerDTO.totalExperienceToNextLevel = this.getExperienceRequiredForLevel();
        playerDTO.specialAbilityName = this.getSpecialAbilityName();
    }

    protected  <T extends LevelUpDTO> T initLevelUpDto(T levelUpDTO) {
        levelUpDTO.healthBonus = this.healthPool;
        levelUpDTO.attackBonus = this.attack;
        levelUpDTO.defenseBonus = this.defense;
        return levelUpDTO;
    }

    protected void calculateLevelUpStatsDiffs(LevelUpDTO levelUpDTO) {
        levelUpDTO.healthBonus = this.healthPool - levelUpDTO.healthBonus;
        levelUpDTO.attackBonus = this.attack - levelUpDTO.attackBonus;
        levelUpDTO.defenseBonus = this.defense - levelUpDTO.defenseBonus;
    }
}
