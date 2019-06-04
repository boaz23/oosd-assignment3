package dnd.logic;

import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;

public interface DeathObserver {
    void onDeath(Player player);
    void onDeath(Enemy enemy);
}
