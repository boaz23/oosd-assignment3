package dnd.logic.board;

import dnd.logic.GameException;
import dnd.logic.LevelEndObserver;
import dnd.logic.Point;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;
import dnd.logic.tileOccupiers.TileOccupier;
import dnd.logic.tileOccupiers.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the game board which holds every tile occupier in a positions matrix
 */
public class BoardImpl implements InitializableBoard {
    private final TileFactory tileFactory;
    private PositionsMatrix board;
    private final List<Enemy> enemies;
    private Player player;

    private final List<LevelEndObserver> levelEndObservers;

    public BoardImpl(TileFactory tileFactory) {
        if (tileFactory == null) {
            throw new IllegalArgumentException("tile factory is null.");
        }

        this.tileFactory = tileFactory;
        levelEndObservers = new ArrayList<>();
        enemies = new ArrayList<>();
    }

    @Override
    public void move(Unit unit, Point targetPosition) throws PositionOutOfBoundsException {
        if (unit == null) {
            throw new IllegalArgumentException("unit is null.");
        }

        TileOccupier temp = board.get(targetPosition);
        board.set(targetPosition, unit);
        board.set(unit.getPosition(), temp);
    }

    @Override
    public TileOccupier getTileOccupier(Point position) throws PositionOutOfBoundsException {
        return board.get(position);
    }

    @Override
    public List<Point> getFreeTilesPositionsInRange(Point position, int range) throws PositionOutOfBoundsException {
        List<Point> freeTilesInRange = new ArrayList<>();
        int lastX = board.lastX();
        int lastY = board.lastY();
        for (int i = Math.max(position.getX() - range, 0); i <= Math.min(position.getX() + range, lastX); i++) {
            for (int j = Math.max(position.getY() - range, 0); j <= Math.min(position.getY() + range, lastY); j++) {
                Point point = new Point(i, j);
                if (Point.distance(position, point) < range) {
                    if (board.get(point).isFree()) {
                        freeTilesInRange.add(point);
                    }
                }
            }
        }
        return freeTilesInRange;
    }

    @Override
    public List<Enemy> getEnemiesInRange(Point position, int range) {
        List<Enemy> enemiesInRange = new ArrayList<>();
        for (Enemy e : enemies) {
            if (Point.distance(position, e.getPosition()) < range) {
                enemiesInRange.add(e);
            }
        }
        return enemiesInRange;
    }

    @Override
    public Player getPlayerInRange(Point position, int range) {
        Player player = null;
        if (Point.distance(position, this.player.getPosition()) < range) {
            player = this.player;
        }
        return player;
    }

    @Override
    public void onDeath(Player player) throws GameException {
        board.set(player.getPosition(), tileFactory.createDeadPlayer());
        callPlayerDeathObservers();
    }

    @Override
    public void onDeath(Enemy enemy) throws GameException {
        board.set(enemy.getPosition(), tileFactory.createFreeTile());
        enemies.remove(enemy);

        if (enemies.isEmpty()) {
            callLevelCompleteObservers();
        }
    }

    @Override
    public void addLevelEndObserver(LevelEndObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("observer is null.");
        }

        levelEndObservers.add(observer);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    @Override
    public void setBoard(PositionsMatrix board) {
        if (board == null) {
            throw new IllegalArgumentException("board is null.");
        }

        this.board = board;
    }

    @Override
    public PositionsMatrix getBoard() {
        return board;
    }

    private void callPlayerDeathObservers() {
        for (LevelEndObserver observer : levelEndObservers) {
            observer.onDeath(player);
        }
    }

    private void callLevelCompleteObservers() {
        for (LevelEndObserver observer : levelEndObservers) {
            observer.onLevelComplete();
        }
    }
}