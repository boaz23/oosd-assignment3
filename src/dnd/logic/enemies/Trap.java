package dnd.logic.enemies;

import dnd.logic.GameException;
import dnd.logic.Point;
import dnd.logic.Tick;
import dnd.logic.player.Player;
import dnd.logic.tileOccupiers.FreeTile;

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
                int experienceValue, char tile,
                int relocationRange, Tick relocationTime, Tick visibilityTime) {
        super(name, healthPool, attack, defense, experienceValue, tile);
        init(relocationRange, relocationTime, visibilityTime);
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
        ticksCount = Tick.Zero;
        visible = true;
    }

    @Override
    public void onTick(Tick current) throws GameException {
        boolean relocated = checkRelocation();
        if (!relocated) {
            checkEngagePlayer();
        }
        updateVisibility();
    }

    private boolean checkRelocation() throws GameException {
        boolean relocated;
        if (ticksCount.equals(relocationTime)) {
            relocate();
            relocated = true;
        }
        else {
            ticksCount = ticksCount.increment();
            relocated = false;
        }

        return relocated;
    }

    private void relocate() throws GameException {
        ticksCount = Tick.Zero;
        List<Point> freeTiles = board.getFreeTilesPositionsInRange(position, relocationRange);
        int tileIndex = randomGenerator.nextInt(freeTiles.size() - 1);
        move(freeTiles.get(tileIndex));
    }

    private void checkEngagePlayer() throws GameException {
        Player player = board.getPlayerInRange(position, ATTACK_RANGE);
        if (player != null) {
            engagePlayer(player);
        }
    }

    private void engagePlayer(Player player) throws GameException {
        callEngageObservers(player);
        meleeAttack(player);
    }

    private void updateVisibility() {
        visible = visibilityTime.isGreaterThan(ticksCount);
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public char toTileChar() {
        if (isVisible()) {
            return super.toTileChar();
        }

        return FreeTile.TileChar;
    }

    @Override
    public Trap clone() {
        return new Trap(
            name,
            healthPool, attack, defense,
            experienceValue, tile,
            relocationRange, relocationTime, visibilityTime
        );
    }
}
