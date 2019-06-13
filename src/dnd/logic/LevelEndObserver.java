package dnd.logic;

import dnd.logic.player.Player;

/**
 * An observer which listens to events occuring when a level should end immediately
 */
public interface LevelEndObserver {
    void onDeath(Player player);
    void onLevelComplete();
}
