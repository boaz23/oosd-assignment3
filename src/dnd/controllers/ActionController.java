package dnd.controllers;

import dnd.logic.GameFlow;
import dnd.logic.LogicException;
import dnd.logic.player.Player;

public class ActionController {
    private Player player;
    private GameFlow gameFlow;

    public ActionController(Player player, GameFlow gameFlow) {
        this.gameFlow = gameFlow;
        this.player = player;
    }

    public void moveUp() {
        player.moveUp();
        gameFlow.onTick();
    }

    public void moveDown() {
        player.moveDown();
        gameFlow.onTick();
    }

    public void moveLeft() {
        player.moveLeft();
        gameFlow.onTick();
    }

    public void moveRight() {
        player.moveRight();
        gameFlow.onTick();
    }

    public void castSpecialAbility() {
        player.useSpecialAbility();
        gameFlow.onTick();
    }

    public void doNothing() {
        gameFlow.onTick();
    }

}
