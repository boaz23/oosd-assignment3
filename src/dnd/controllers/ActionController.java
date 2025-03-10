package dnd.controllers;

import dnd.logic.GameException;
import dnd.logic.LevelFlow;
import dnd.logic.LogicException;
import dnd.logic.MoveResult;
import dnd.logic.player.Player;

/**
 * Takes commands and requests from the player to do them and then starts the next tick.
 */
public class ActionController {
    private final Player player;
    private final LevelFlow levelFlow;

    public ActionController(Player player, LevelFlow levelFlow) {
        this.levelFlow = levelFlow;
        this.player = player;
    }

    public boolean moveUp() throws GameException {
        return move(() -> player.moveUp());
    }

    public boolean moveDown() throws GameException {
        return move(() -> player.moveDown());
    }

    public boolean moveLeft() throws GameException {
        return move(() -> player.moveLeft());
    }

    public boolean moveRight() throws GameException {
        return move(() -> player.moveRight());
    }

    public boolean castSpecialAbility() throws GameException {
        try {
            player.useSpecialAbility();
            levelFlow.onTick();
            return true;
        }
        catch (LogicException e) {
            return false;
        }
    }

    public void doNothing() throws GameException {
        levelFlow.onTick();
    }

    private boolean move(MoveAction moveAction) throws GameException {
        boolean validMove = moveAction.move() != MoveResult.Invalid;
        if (validMove) {
            levelFlow.onTick();
        }

        return validMove;
    }

    private interface MoveAction {
        MoveResult move() throws GameException;
    }
}
