package dnd.logic.board;

import dnd.logic.Point;
import dnd.logic.PositionOutOfBoundsException;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;
import dnd.logic.tileOccupiers.TileOccupier;
import dnd.logic.tileOccupiers.Unit;

import java.util.List;

public interface Board {
    /**
     * Requests to move the specified unit to the specified location
     */
    void move(Unit unit, Point targetPosition) throws PositionOutOfBoundsException;

    TileOccupier getTileOccupier(Point position) throws PositionOutOfBoundsException;

    List<Point> getFreeTilesPositionsInRange(Point position, int range) throws PositionOutOfBoundsException;

    List<Enemy> getEnemiesInRange(Point position, int range) throws PositionOutOfBoundsException;

    Player getPlayerInRange(Point position, int range) throws PositionOutOfBoundsException;

    void reportDeath(Player player);

    void reportDeath(Enemy enemy);

    BoardSquare[][] getBoard();
}
