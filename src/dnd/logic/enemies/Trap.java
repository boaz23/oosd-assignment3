package dnd.logic.enemies;

import dnd.GameException;
import dnd.logic.Point;
import dnd.logic.Tick;
import dnd.logic.board.Board;
import dnd.logic.player.Player;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.FreeTile;
import dnd.logic.tileOccupiers.TileOccupier;

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
        this.init(relocationRange, relocationTime, visibilityTime);
    }

    protected Trap(String name,
                   int healthPool, int attack, int defense,
                   int experienceValue, char tile,
                   int relocationRange, Tick relocationTime, Tick visibilityTime,
                   Point position,
                   RandomGenerator randomGenerator,
                   Board board) {
        super(name, healthPool, attack, defense, randomGenerator, board, experienceValue, tile);
        this.init(relocationRange, relocationTime, visibilityTime);
        this.position = position;
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
    public void onTick(Tick current) throws GameException {
        boolean relocated = this.checkRelocation();
        if (!relocated) {
            this.checkEngagePlayer();
        }

        this.updateVisibility();
    }

    private void checkEngagePlayer() throws GameException {
        Player player = this.board.getPlayerInRange(this.position, ATTACK_RANGE);
        if (player != null) {
            this.engagePlayer(player);
        }
    }

    private boolean checkRelocation() throws GameException {
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

    private void relocate() throws GameException {
        this.ticksCount = Tick.Zero;
        List<Point> freeTiles = this.board.getFreeTilesPositionsInRange(this.position, this.relocationRange);
        int tileIndex = this.randomGenerator.nextInt(freeTiles.size() - 1);
        this.move(freeTiles.get(tileIndex));
    }

    private void engagePlayer(Player player) throws GameException {
        callEngageObservers(player);
        this.meleeAttack(player);
    }

    private void updateVisibility() {
        this.visible = this.visibilityTime.isGreaterThan(this.ticksCount);
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
    public TileOccupier clone(Point position, RandomGenerator randomGenerator, Board board) {
        return new Trap(
            this.name,
            this.healthPool, this.attack, this.defense,
            this.experienceValue, this.tile,
            this.relocationRange, this.relocationTime,
            this.visibilityTime,
            position,
            randomGenerator, board
        );
    }
}
