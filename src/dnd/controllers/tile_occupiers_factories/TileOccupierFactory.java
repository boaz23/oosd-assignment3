package dnd.controllers.tile_occupiers_factories;

import dnd.GameEventObserver;
import dnd.logic.LevelFlow;
import dnd.logic.board.BoardImpl;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.TileOccupier;

public interface TileOccupierFactory {
    TileOccupier createTileOccupier(RandomGenerator randomGenerator, BoardImpl board, LevelFlow levelFlow, GameEventObserver gameEventObserver);
}
