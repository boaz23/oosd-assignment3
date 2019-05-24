package dnd.logic.player;

import dnd.RandomGenerator;
import dnd.logic.LogicException;
import dnd.logic.MoveResult;
import dnd.logic.Tick;
import dnd.logic.board.Board;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;
import dnd.logic.tileOccupiers.TileOccupier;
import dnd.logic.tileOccupiers.Unit;

import java.util.ArrayList;
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
                RandomGenerator randomGenerator,
                int spellPower, int manaPool, int cost,
                int hitTimes, int range) {
        super(name, healthPool, attack, defense, randomGenerator);
        this.init(spellPower, manaPool, cost, hitTimes, range);
    }

    protected Mage(String name,
                   int healthPool, int attack, int defense,
                   RandomGenerator randomGenerator,
                   Board board,
                   int spellPower, int manaPool, int cost,
                   int hitTimes, int range) {
        super(name, healthPool, attack, defense, randomGenerator, board);
        this.init(spellPower, manaPool, cost, hitTimes, range);
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
        super.levelUp();
        this.manaPool += this.level * LEVEL_MANA_DIFF;
        this.currentMana = Math.min(this.currentMana + this.getManaAddition(), this.manaPool);
        this.spellPower += this.level * LEVEL_SPELL_POWER_DIFF;
    }

    private int getManaAddition() {
        return this.manaPool / MANA_ADDITION_DIV;
    }

    @Override
    public void useSpecialAbility() throws LogicException {
        if (this.currentMana < cost) {
            throw new LogicException("Cannot use special ability due insufficient mana.");
        }

        this.currentMana -= this.cost;
        List<TileOccupier> tileOccupiersInRange = this.board.findTileOccupiers(this.position, this.range);
        MageSpecialAbilityAttackState
        for ( : ) {

        }

        List<TileOccupier> enemiesInRange = this.board.findTileOccupiers(this.position, this.range, EnemyPropertySet);
        if (enemiesInRange.size() > 0) {
            for (int hits = 0; hits < this.hitTimes; hits++) {
                int enemyIndex = randomGenerator.nextInt(enemiesInRange.size());
                Unit enemy = (Unit) enemiesInRange.get(enemyIndex);
                this.attackCore(enemy, this.spellPower);
            }
        }
    }

    @Override
    public void onTick(Tick current) {
        this.currentMana = Math.min(this.manaPool, this.currentMana + MANA_REGEN);
    }

    @Override
    protected Object getMoveState() {
        return this.new MoveAttackState();
    }

    @Override
    public MoveResult attack(Enemy enemy, Object state) throws LogicException {
        AttackState attackState = (AttackState)state;
        return attackState.visit(enemy);
    }

    private static class MageSpecialAbilityAttackState implements AttackState {
        private List<Enemy> enemiesInRange;

        public MageSpecialAbilityAttackState() {
            this.enemiesInRange = new ArrayList<>();
        }

        @Override
        public Object visit(Enemy enemy) {
            this.enemiesInRange.add(enemy);
            return null;
        }

        public List<Enemy> getEnemiesInRange() {
            return enemiesInRange;
        }
    }
}
