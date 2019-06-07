package dnd.logic.tileOccupiers;

import dnd.GameException;
import dnd.logic.LogicException;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;

public interface TileVisitor {
    Object visit(Wall wall, Object state) throws LogicException;
    Object visit(FreeTile freeTile, Object state) throws LogicException;
    Object visit(Enemy enemy, Object state) throws GameException;
    Object visit(Player player, Object state) throws GameException;
}
