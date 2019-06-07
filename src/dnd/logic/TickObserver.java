package dnd.logic;

import dnd.GameException;

@SuppressWarnings("unused")
public interface TickObserver {
    void onTick(Tick current) throws GameException;
}
