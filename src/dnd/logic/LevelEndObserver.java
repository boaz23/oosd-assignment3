package dnd.logic;

import dnd.logic.player.Player;

public interface LevelEndObserver {
    void onDeath(Player player);
    void onLevelComplete();
}
