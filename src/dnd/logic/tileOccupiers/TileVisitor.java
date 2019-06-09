package dnd.logic.tileOccupiers;

import dnd.logic.GameException;
import dnd.logic.LogicException;
import dnd.logic.MoveResult;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;

public interface TileVisitor {
    MoveResult visit(Wall wall) throws LogicException;
    MoveResult visit(FreeTile freeTile) throws LogicException;
    MoveResult visit(Enemy enemy) throws GameException;
    MoveResult visit(Player player) throws GameException;
}
