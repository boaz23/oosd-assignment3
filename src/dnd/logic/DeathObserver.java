package dnd.logic;

import dnd.GameException;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;

public interface DeathObserver {
    void onDeath(Player player) throws GameException;
    void onDeath(Enemy enemy) throws GameException;
}
