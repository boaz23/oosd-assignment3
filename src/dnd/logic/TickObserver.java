package dnd.logic;

public interface TickObserver {
    void onTick(Tick current) throws GameException;
}
