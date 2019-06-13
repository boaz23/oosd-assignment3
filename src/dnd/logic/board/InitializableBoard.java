package dnd.logic.board;

import dnd.logic.LevelEndObserver;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;

/**
 * The board interface which is required in order to initialize the board to a new level ready state
 */
public interface InitializableBoard extends Board {
    void addLevelEndObserver(LevelEndObserver observer);

    PositionsMatrix getBoard();
    void setBoard(PositionsMatrix positionsMatrix);

    Player getPlayer();
    void setPlayer(Player player);
    void addEnemy(Enemy enemy);
}
