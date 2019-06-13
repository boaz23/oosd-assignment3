package dnd.logic;

/**
 * An observer which is called when it's turn has arrived and it's time for it to perform it's action
 */
public interface TickObserver {
    void onTick(Tick current) throws GameException;
}
