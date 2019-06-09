package dnd.controllers.tile_occupiers_factories;

import dnd.cli.view.GameEventObserver;
import dnd.logic.LevelFlow;
import dnd.logic.Point;
import dnd.logic.board.InitializableBoard;
import dnd.logic.enemies.Enemy;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.TileOccupier;

public class EnemyFactory extends UnitFactory {
    private final Enemy enemy;

    public EnemyFactory(
        Enemy enemy,
        RandomGenerator randomGenerator,
        InitializableBoard board,
        LevelFlow levelFlow,
        GameEventObserver gameEventObserver) {
        super(randomGenerator, board, levelFlow, gameEventObserver);
        this.enemy = enemy;
    }

    public TileOccupier createTileOccupier(Point position) {
        Enemy clone = enemy.clone();
        board.addEnemy(clone);
        levelFlow.addTickObserver(clone);
        prepareForNewLevel(clone, position);
        return clone;
    }
}
