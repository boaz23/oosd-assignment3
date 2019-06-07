package dnd.controllers;

import dnd.GameEventObserver;
import dnd.controllers.tile_occupiers_factories.TileOccupierFactory;
import dnd.logic.LevelFlow;
import dnd.logic.board.BoardImpl;
import dnd.logic.tileOccupiers.Unit;

public abstract class UnitFactory implements TileOccupierFactory {
    protected void registerEventObservers(Unit unit, BoardImpl board, LevelFlow levelFlow, GameEventObserver gameEventObserver) {
        unit.addDeathObserver(board);
        unit.addDeathObserver(levelFlow);
        unit.addGameEventObserver(gameEventObserver);
    }
}
