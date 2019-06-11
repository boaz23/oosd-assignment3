package tests.logic.enemies;

import dnd.logic.GameException;
import dnd.logic.MoveResult;
import dnd.logic.Point;
import dnd.logic.board.Board;
import dnd.logic.enemies.Monster;
import dnd.logic.player.Player;
import dnd.logic.random_generator.RandomGenerator;

import java.util.ArrayList;

public class MonsterMock extends Monster {
    public boolean movedLeft;

    public MonsterMock(String name, int healthPool, int attack, int defense, int experienceValue, char tile, int range) {
        super(name, healthPool, attack, defense, experienceValue, tile, range);

        movedLeft = false;
        gameEventObservers = new ArrayList<>();
        deathObservers = new ArrayList<>();
    }

    public MonsterMock setPosition(Point position) {
        this.position = position;
        return this;
    }

    public MonsterMock setBoard(Board board) {
        this.board = board;
        return this;
    }

    public MonsterMock setRandomGenerator(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
        return this;
    }

    public int getCurrentHealth() {
        return currentHealth;
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
