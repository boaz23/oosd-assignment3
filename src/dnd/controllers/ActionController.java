package dnd.controllers;

import dnd.logic.GameFlow;
import dnd.logic.LogicException;
import dnd.logic.MoveResult;
import dnd.logic.player.Player;

public class ActionController {
    private Player player;
    private GameFlow gameFlow;

    public ActionController(Player player, GameFlow gameFlow) {
        this.gameFlow = gameFlow;
        this.player = player;
    }

    public boolean moveUp() {
        return move(() -> player.moveUp());
    }

    public boolean moveDown() {
        return move(() -> player.moveDown());
    }

    public boolean moveLeft() {
        return move(() -> player.moveLeft());
    }

    public boolean moveRight() {
        return move(() -> player.moveRight());
    }

    public boolean castSpecialAbility() {
        try {
            player.useSpecialAbility();
            gameFlow.onTick();
            return true;
        } catch (LogicException e) {
            return false;
        }
    }

    public void doNothing() {
        gameFlow.onTick();
    }

    private boolean move(MoveAction moveAction) {
        boolean validMove = moveAction.move() != MoveResult.Invalid;
        if (validMove) {
            gameFlow.onTick();
        }

        return validMove;
    }

    private interface MoveAction {
        MoveResult move();
    }
}
