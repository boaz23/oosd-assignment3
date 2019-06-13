package dnd.logic;

import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains tick logic. Responsible for calling each unit to perform it's turn
 */
public class LevelFlow implements DeathObserver, LevelEndObserver {
    private Tick tick;
    private final List<TickObserver> tickObservers;

    private boolean stopTurns;

    public LevelFlow() {
        tickObservers = new ArrayList<>();
        tick = Tick.Zero;
        stopTurns = false;
    }

    public void addTickObserver(TickObserver observer) {
        tickObservers.add(observer);
    }

    public void onTick() throws GameException {
        tick = tick.increment();

        TickObserver[] tickObservers = this.tickObservers.toArray(new TickObserver[]{});
        for (int i = 0; i < tickObservers.length & !stopTurns; i++) {
            TickObserver observer = tickObservers[i];

            // may have been removed by previous onTick calls
            if (this.tickObservers.contains(observer)) {
                observer.onTick(tick);
            }
        }
    }

    @Override
    public void onDeath(Player player) {
        tickObservers.remove(player);
        stopTurns = true;
    }

    @Override
    public void onLevelComplete() {
        stopTurns = true;
    }

    @Override
    public void onDeath(Enemy enemy) {
        tickObservers.remove(enemy);
    }
}
