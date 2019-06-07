package dnd.logic.board;

import dnd.GameException;
import dnd.logic.LevelEndObserver;
import dnd.logic.Point;
import dnd.logic.PositionOutOfBoundsException;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;
import dnd.logic.tileOccupiers.TileFactory;
import dnd.logic.tileOccupiers.TileOccupier;
import dnd.logic.tileOccupiers.Unit;

import java.util.ArrayList;
import java.util.List;

// TODO: maybe throw PositionOutOfBoundsException in methods
public class BoardImpl implements Board {
    private final TileFactory tileFactory;
    private PositionsMatrix board;
    private List<Enemy> enemies;
    private Player player;

    private List<LevelEndObserver> levelEndObservers;

    public BoardImpl(TileFactory tileFactory) {
        if (tileFactory == null) {
            throw new IllegalArgumentException("tile factory is null.");
        }

        this.tileFactory = tileFactory;
        this.levelEndObservers = new ArrayList<>();
        this.enemies = new ArrayList<>();
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
            for(int j = Math.max(position.getY() - range, 0); j <= Math.min(position.getY() + range, lastY); j++) {
                Point point = new Point(i, j);
                if (Point.distance(position, point) < range) {
                    if (board.get(point).isFree())
                        freeTilesInRange.add(point);
                }
            }
        }
        return freeTilesInRange;
    }

    @Override
    public List<Enemy> getEnemiesInRange(Point position, int range) {
        List<Enemy> enemiesInRange = new ArrayList<Enemy>();
        for (Enemy e : enemies) {
            if (Point.distance(position, e.getPosition()) < range)
               enemiesInRange.add(e);
        }
        return enemiesInRange;
    }

    @Override
    public Player getPlayerInRange(Point position, int range) {
        Player player = null;
        if (Point.distance(position, this.player.getPosition()) < range)
            player = this.player;
        return player;
    }

    @Override
    public void onDeath(Player player) throws GameException {
        board.set(player.getPosition(), tileFactory.createDeadPlayer());
        this.callPlayerDeathObservers();

    }

    @Override
    public void onDeath(Enemy enemy) throws GameException {
        board.set(enemy.getPosition(), tileFactory.createFreeTile());
        enemies.remove(enemy);

        if (enemies.size() == 0) {
            this.callLevelCompleteObservers();
        }
    }

    @Override
    public PositionsMatrix getBoard() {
        return board;
    }

    @Override
    public void addLevelEndObserver(LevelEndObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("observer is null.");
        }

        this.levelEndObservers.add(observer);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }

    public void setBoard(PositionsMatrix board) {
        if (board == null) {
            throw new IllegalArgumentException("board is null.");
        }

        this.board = board;
    }

    private void callPlayerDeathObservers() {
        for (LevelEndObserver observer : this.levelEndObservers) {
            observer.onDeath(player);
        }
    }

    private void callLevelCompleteObservers() {
        for (LevelEndObserver observer : this.levelEndObservers) {
            observer.onLevelComplete();
        }
    }
}