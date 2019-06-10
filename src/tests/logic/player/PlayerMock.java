package tests.logic.player;

import dnd.dto.units.PlayerDTO;
import dnd.logic.GameException;
import dnd.logic.Point;
import dnd.logic.Tick;
import dnd.logic.player.Player;

public class PlayerMock extends Player {
    public PlayerMock() {
        super("Test Player", 100, 10, 10);
    }

    public PlayerMock(String name, int healthPool, int attack, int defense) {
        super(name, healthPool, attack, defense);
    }

    public void setPosition(Point position) {
        this.position = position;
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
