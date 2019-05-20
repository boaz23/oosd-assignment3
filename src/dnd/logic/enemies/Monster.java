package dnd.logic.enemies;

import dnd.RandomGenerator;
import dnd.logic.Tick;
import dnd.logic.TileProperty;
import dnd.logic.Unit;
import dnd.logic.player.Player;

import java.util.List;

public class Monster extends Enemy {
    private final int range;

    public Monster(String name,
                   int healthPool, int attack, int defense,
                   UnitsInRangeFinder unitsInRangeFinder,
                   RandomGenerator randomGenerator,
                   int experienceValue, char tile,
                   int range) {
        super(name, healthPool, attack, defense, unitsInRangeFinder, randomGenerator, experienceValue, tile);

        if (range <= 0) {
            throw new IllegalArgumentException("monster vision range must be a positive number.");
        }

        this.range = range;

        this.addProperty(TileProperty.Monster);
    }

    @Override
    public void onTick(Tick current) {
        List<Unit> playersInRange = this.unitsInRangeFinder.findUnitsInRange(this.position, this.range, UnitType.Player);
        if (playersInRange.size() > 0) {
            this.chasePlayer((Player)playersInRange.get(0));
        }
        else {
            this.actRandomly();
        }
    }

    private void chasePlayer(Player player) {
        int dx = this.position.getX() - player.getPosition().getX();
        int dy = this.position.getY() - player.getPosition().getY();

        // TODO: account for walls, not being able to move to the desired direction
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                // TODO: move left
            }
            else {
                // TODO: move right
            }
        }
        else {
            if (dy > 0) {
                // TODO: move up
            }
            else {
                // TODO: move down
            }
        }
    }

    private void actRandomly() {
        int move = this.randomGenerator.nextInt(3);
        // TODO: Perform a random move
    }
}
