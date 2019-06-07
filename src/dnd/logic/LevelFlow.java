package dnd.logic;

import dnd.GameException;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;

import java.util.ArrayList;
import java.util.List;

public class LevelFlow implements DeathObserver, LevelEndObserver {
    private Tick tick;
    private List<TickObserver> tickObservers;

    private boolean stopTurns;

    public LevelFlow() {
        tickObservers = new ArrayList<>();
        tick = Tick.Zero;
        this.stopTurns = false;
    }

    public void addTickObserver(TickObserver observer) {
        this.tickObservers.add(observer);
    }

    public void onTick() throws GameException {
        tick = tick.increment();

        TickObserver[] tickObservers = this.tickObservers.toArray(new TickObserver[] {});
        for (int i = 0; i < tickObservers.length & !this.stopTurns; i++) {
            TickObserver observer = tickObservers[i];

            // may have been removed
            if (this.tickObservers.contains(observer)) {
                observer.onTick(tick);
            }
        }
    }

    @Override
    public void onDeath(Player player) {
        this.tickObservers.remove(player);
        this.stopTurns = true;
    }

    @Override
    public void onLevelComplete() {
        this.stopTurns = true;
    }

    @Override
    public void onDeath(Enemy enemy) {
        this.tickObservers.remove(enemy);
    }
}
