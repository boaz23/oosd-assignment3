package dnd.logic.enemies;

import dnd.RandomGenerator;
import dnd.logic.*;
import dnd.logic.player.Player;

import java.util.List;

public class Trap extends Enemy {
    private static final int ATTACK_RANGE = 1;

    private final int relocationRange;
    private final Tick relocationTime;
    private final Tick visibilityTime;
    private Tick ticksCount;
    private boolean visible;

    public Trap(String name,
                int healthPool, int attack, int defense,
                UnitsInRangeFinder unitsInRangeFinder,
                RandomGenerator randomGenerator,
                int experienceValue, char tile,
                int relocationRange, Tick relocationTime, Tick visibilityTime) {
        super(name, healthPool, attack, defense, unitsInRangeFinder, randomGenerator, experienceValue, tile);

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

        this.addProperty(TileProperty.Trap);
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
        List<Unit> playersInRange = this.unitsInRangeFinder.findUnitsInRange(this.position, ATTACK_RANGE, UnitType.Player);
        if (playersInRange.size() > 0) {
            this.engagePlayer((Player) playersInRange.get(0));
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
        // TODO: find all free tiles
        List<BoardSquare> freeTiles = null;
        int tileIndex = this.randomGenerator.nextInt(freeTiles.size());
        // TODO: move to free tile
    }

    private void engagePlayer(Player player) {
        // TODO: engage player
    }

    private void updateVisibility() {
        if (this.visibilityTime.isGreaterThan(this.ticksCount)) {
            this.visible = true;
        }
        else {
            this.visible = false;
        }
    }
}
