package dnd.logic;

import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LevelFlow implements DeathObserver, LevelEndObserver {
    private Tick tick;
    private Set<TickObserver> tickObservers;

    private boolean stopTurns;

    public LevelFlow() {
        tickObservers = new HashSet<>();
        tick = Tick.Zero;
        this.stopTurns = false;
    }

    public void addTickObserver(TickObserver observer) {
        tickObservers.add(observer);
    }

    public void onTick() {
        tick = tick.increment();

        TickObserver[] tickObservers = this.tickObservers.toArray(new TickObserver[] {});
        for (int i = 0; i < tickObservers.length & !this.stopTurns; i++) {
            TickObserver observer = tickObservers[i];
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
