package dnd.controllers.tile_occupiers_factories;

import dnd.GameEventObserver;
import dnd.logic.LevelFlow;
import dnd.logic.Point;
import dnd.logic.board.InitializableBoard;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.Unit;

public abstract class UnitFactory implements TileOccupierFactory {
    protected final RandomGenerator randomGenerator;
    protected final InitializableBoard board;
    protected final LevelFlow levelFlow;
    protected final GameEventObserver gameEventObserver;

    protected UnitFactory(
        RandomGenerator randomGenerator,
        InitializableBoard board,
        LevelFlow levelFlow,
        GameEventObserver gameEventObserver
    ) {
        this.randomGenerator = randomGenerator;
        this.board = board;
        this.levelFlow = levelFlow;
        this.gameEventObserver = gameEventObserver;
    }

    protected void prepareForNewLevel(Unit unit, Point position) {
        unit.initNewLevelState(position, randomGenerator, board);
        registerEventObservers(unit);
    }

    private void registerEventObservers(Unit unit) {
        unit.addDeathObserver(board);
        unit.addDeathObserver(levelFlow);
        unit.addGameEventObserver(gameEventObserver);
    }
}
