package dnd.logic;

import dnd.GameException;

public interface TickObserver {
    void onTick(Tick current) throws GameException;
}
