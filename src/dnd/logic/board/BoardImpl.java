package dnd.logic.board;

import dnd.logic.Point;
import dnd.logic.PositionOutOfBoundsException;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;
import dnd.logic.tileOccupiers.TileFactory;
import dnd.logic.tileOccupiers.TileOccupier;
import dnd.logic.tileOccupiers.Unit;

import java.util.ArrayList;
import java.util.List;

public class BoardImpl implements Board {
    private final TileFactory tileFactory;
    BoardSquare[][] board;
    List<Enemy> enemies;
    Player player;

    public BoardImpl(TileFactory tileFactory) {
        if (tileFactory == null) {
            throw new IllegalArgumentException("tile factory is null.");
        }

        this.tileFactory = tileFactory;
    }

    @Override
    public void move(Unit unit, Point targetPosition) throws PositionOutOfBoundsException {
        if (unit == null) {
            throw new IllegalArgumentException("unit is null.");
        }
        validatePosition(targetPosition);

        TileOccupier temp =  board[targetPosition.getX()][targetPosition.getX()].getTileOccupier();
        board[targetPosition.getX()][targetPosition.getX()].setTileOccupier(unit);
        board[unit.getPosition().getX()][unit.getPosition().getY()].setTileOccupier(temp);
    }

    @Override
    public TileOccupier getTileOccupier(Point position) throws PositionOutOfBoundsException {
        validatePosition(position);

        return board[position.getX()][position.getY()].getTileOccupier();
    }

    @Override
    public List<Point> getFreeTilesPositionsInRange(Point position, int range) {
//        validatePosition(position);

        List<Point> freeTilesInRange = new ArrayList<Point>();
        for (int i = Math.max(0, position.getX() - range); i <= Math.min(position.getX() + range, board.length - 1); i++) {
            for(int j = Math.max(0, position.getY() - range); j <= Math.min(position.getY() + range, board[i].length - 1); j++) {
                Point point = new Point(i, j);
                if (Point.distance(position, point) < range) {
                    if (board[i][j].getTileOccupier().isFree())
                        freeTilesInRange.add(point);
                }
            }
        }
        return freeTilesInRange;
    }

    @Override
    public List<Enemy> getEnemiesInRange(Point position, int range) {
//        validatePosition(position);
        List<Enemy> enemiesInRange = new ArrayList<Enemy>();
        for (Enemy e : enemies) {
            if (Point.distance(position, e.getPosition()) < range)
               enemiesInRange.add(e);
        }
        return enemiesInRange;
    }

    @Override
    public Player getPlayerInRange(Point position, int range) {
//        validatePosition(position);
        Player player = null;
        if (Point.distance(position, this.player.getPosition()) < range)
            player = this.player;
        return player;
    }

    @Override
    public void reportDeath(Player player) {
        board[player.getPosition().getX()][player.getPosition().getY()].setTileOccupier(tileFactory.createDeadPlayer());
    }

    @Override
    public void reportDeath(Enemy enemy) {
        board[enemy.getPosition().getX()][enemy.getPosition().getY()].setTileOccupier(tileFactory.createFreeTile());
    }

    @Override
    public BoardSquare[][] getBoard() {
        return board;
    }

    private void validatePosition(Point position) throws PositionOutOfBoundsException {
        if (position == null)
            throw new IllegalArgumentException("position is null.");
        if (position.getX() > board.length | position.getY() > board[0].length
                | position.getX() < 0 | position.getY() < 0)
            throw new PositionOutOfBoundsException();
    }
}