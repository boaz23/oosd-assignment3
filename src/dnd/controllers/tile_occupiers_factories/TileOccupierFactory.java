package dnd.controllers.tile_occupiers_factories;

import dnd.GameEventObserver;
import dnd.logic.LevelFlow;
import dnd.logic.Point;
import dnd.logic.board.InitializableBoard;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.TileOccupier;

public interface TileOccupierFactory {
    TileOccupier createTileOccupier(
        Point position,
        RandomGenerator randomGenerator,
        InitializableBoard board,
        LevelFlow levelFlow,
        GameEventObserver gameEventObserver);
}
