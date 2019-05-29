package dnd.logic;

import java.util.ArrayList;
import java.util.List;

public class GameFlow {
    private Tick tick;
    private List<TickObserver> tickObservers;

    public GameFlow() {
        tickObservers = new ArrayList<TickObserver>();
        tick = Tick.Zero;
    }

    public void addObserver(TickObserver observer) {
        tickObservers.add(observer);
    }

    public void onTick() {
        tick = tick.increment();
        for (TickObserver t : tickObservers) {
            t.onTick(tick);
        }
    }
}
