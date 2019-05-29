package dnd.logic.enemies;

import dnd.RandomGenerator;
import dnd.logic.Point;
import dnd.logic.Tick;
import dnd.logic.board.Board;
import dnd.logic.player.Player;

import java.util.List;

public class Trap extends Enemy {
    private static final int ATTACK_RANGE = 2;

    private int relocationRange;
    private Tick relocationTime;
    private Tick visibilityTime;

    private Tick ticksCount;
    private boolean visible;

    public Trap(String name,
                int healthPool, int attack, int defense,
                RandomGenerator randomGenerator,
                int experienceValue, char tile,
                int relocationRange, Tick relocationTime, Tick visibilityTime) {
        super(name, healthPool, attack, defense, randomGenerator, experienceValue, tile);
        this.init(relocationRange, relocationTime, visibilityTime);
    }

    protected Trap(String name,
                   int healthPool, int attack, int defense,
                   RandomGenerator randomGenerator,
                   Board board,
                   int experienceValue, char tile,
                   int relocationRange, Tick relocationTime, Tick visibilityTime) {
        super(name, healthPool, attack, defense, randomGenerator, board, experienceValue, tile);
        this.init(relocationRange, relocationTime, visibilityTime);
    }

    private void init(int relocationRange, Tick relocationTime, Tick visibilityTime) {
        if (relocationRange <= 0) {
            throw new IllegalArgumentException("monster vision range must be a positive number.");
        }
        if (relocationTime == null) {
            throw new IllegalArgumentException("trap relocation time is null.");
        }
        if (visibilityTime == null) {
            throw new IllegalArgumentException("trap visibility time is null.");
        }
        if (visibilityTime.isGreaterOrEqualTo(relocationTime)) {
            throw new IllegalArgumentException("visibility time must be shorter than the relocation time.");
        }

        this.relocationRange = relocationRange;
        this.relocationTime = relocationTime;
        this.visibilityTime = visibilityTime;
        this.ticksCount = Tick.Zero;
        this.visible = true;
    }

    @Override
    public void onTick(Tick current) {
        boolean relocated = this.checkRelocation();
        if (!relocated) {
            this.checkEngagePlayer();
        }

        this.updateVisibility();
    }

    private void checkEngagePlayer() {
        Player player = this.board.getPlayerInRange(this.position, ATTACK_RANGE);
        if (player != null) {
            this.engagePlayer(player);
        }
    }

    private boolean checkRelocation() {
        boolean relocated;
        if (this.ticksCount.equals(this.relocationTime)) {
            this.relocate();
            relocated = true;
        }
        else {
            this.ticksCount = ticksCount.increment();
            relocated = false;
        }

        return relocated;
    }

    private void relocate() {
        this.ticksCount = Tick.Zero;
        List<Point> freeTiles = this.board.getFreeTilesPositionsInRange(this.position, this.relocationRange);
        int tileIndex = this.randomGenerator.nextInt(freeTiles.size());
        this.move(freeTiles.get(tileIndex));
    }

    private void engagePlayer(Player player) {
        this.meeleAttack(player);
    }

    private void updateVisibility() {
        if (this.visibilityTime.isGreaterThan(this.ticksCount)) {
            this.visible = true;
        }
        else {
            this.visible = false;
        }
    }

    public boolean isVisible() {
        return visible;
    }
}
