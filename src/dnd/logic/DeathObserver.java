package dnd.logic;

import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;

/**
 * An observer which listens to units deaths
 */
public interface DeathObserver {
    void onDeath(Player player) throws GameException;
    void onDeath(Enemy enemy) throws GameException;
}
