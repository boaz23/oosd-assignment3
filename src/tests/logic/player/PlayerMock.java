package tests.logic.player;

import dnd.dto.units.PlayerDTO;
import dnd.logic.GameException;
import dnd.logic.Point;
import dnd.logic.Tick;
import dnd.logic.board.Board;
import dnd.logic.player.Player;
import dnd.logic.random_generator.RandomGenerator;

import java.util.ArrayList;

public class PlayerMock extends Player {
    public PlayerMock() {
        super("Test Player", 100, 10, 10);

        gameEventObservers = new ArrayList<>();
        deathObservers = new ArrayList<>();
    }

    public PlayerMock(String name, int healthPool, int attack, int defense) {
        super(name, healthPool, attack, defense);

        gameEventObservers = new ArrayList<>();
        deathObservers = new ArrayList<>();
    }
    public PlayerMock setPosition(Point position) {
        this.position = position;
        return this;
    }

    public PlayerMock setBoard(Board board) {
        this.board = board;
        return this;
    }

    public PlayerMock setRandomGenerator(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
        return this;
    }

    @Override
    protected void useSpecialAbilityCore() throws GameException {

    }

    @Override
    public Player clone() {
        return null;
    }

    @Override
    protected String getSpecialAbilityName() {
        return null;
    }

    @Override
    public PlayerDTO createDTO() {
        return null;
    }

    @Override
    public void onTick(Tick current) throws GameException {

    }
}
