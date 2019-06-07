package dnd.controllers.tile_occupiers_factories;

import dnd.GameEventObserver;
import dnd.logic.LevelFlow;
import dnd.logic.Point;
import dnd.logic.board.InitializableBoard;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.TileOccupier;

public class InanimateFactory implements TileOccupierFactory {
    private final TileOccupier inanimate;

    public InanimateFactory(TileOccupier inanimate) {
        this.inanimate = inanimate;
    }


    @Override
    public TileOccupier createTileOccupier(
        Point position,
        RandomGenerator randomGenerator,
        InitializableBoard board,
        LevelFlow levelFlow,
        GameEventObserver gameEventObserver) {
        return inanimate.clone(position, randomGenerator, board);
    }
}
