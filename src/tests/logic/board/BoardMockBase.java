package tests.logic.board;

import dnd.logic.GameException;
import dnd.logic.Point;
import dnd.logic.board.Board;
import dnd.logic.board.PositionOutOfBoundsException;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;
import dnd.logic.tileOccupiers.TileOccupier;
import dnd.logic.tileOccupiers.Unit;

import java.util.List;

public class BoardMockBase implements Board {
    @Override
    public void move(Unit unit, Point targetPosition) throws PositionOutOfBoundsException {

    }

    @Override
    public TileOccupier getTileOccupier(Point position) throws PositionOutOfBoundsException {
        return null;
    }

    @Override
    public List<Point> getFreeTilesPositionsInRange(Point position, int range) throws PositionOutOfBoundsException {
        return null;
    }

    @Override
    public List<Enemy> getEnemiesInRange(Point position, int range) throws PositionOutOfBoundsException {
        return null;
    }

    @Override
    public Player getPlayerInRange(Point position, int range) throws PositionOutOfBoundsException {
        return null;
    }

    @Override
    public void onDeath(Player player) throws GameException {

    }

    @Override
    public void onDeath(Enemy enemy) throws GameException {

    }
}
