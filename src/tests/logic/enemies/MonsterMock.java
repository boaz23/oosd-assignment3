package tests.logic.enemies;

import dnd.logic.GameException;
import dnd.logic.MoveResult;
import dnd.logic.Point;
import dnd.logic.enemies.Monster;
import dnd.logic.player.Player;

public class MonsterMock extends Monster {
    public boolean movedLeft;

    public MonsterMock(String name, int healthPool, int attack, int defense, int experienceValue, char tile, int range) {
        super(name, healthPool, attack, defense, experienceValue, tile, range);

        movedLeft = false;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public void chasePlayer(Player player) throws GameException {
        super.chasePlayer(player);
    }

    @Override
    public MoveResult moveLeft() {
        movedLeft = true;
        return null;
    }
}
