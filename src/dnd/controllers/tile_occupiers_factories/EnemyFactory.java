package dnd.controllers.tile_occupiers_factories;

import dnd.GameEventObserver;
import dnd.controllers.UnitFactory;
import dnd.logic.LevelFlow;
import dnd.logic.Point;
import dnd.logic.board.BoardImpl;
import dnd.logic.enemies.Enemy;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.TileOccupier;

public class EnemyFactory extends UnitFactory {
    private final Enemy enemy;

    public EnemyFactory(Enemy enemy) {
        this.enemy = enemy;
    }

    public TileOccupier createTileOccupier(
            Point position,
            RandomGenerator randomGenerator,
            BoardImpl board,
            LevelFlow levelFlow,
            GameEventObserver gameEventObserver) {
        Enemy enemy = (Enemy)this.enemy.clone(position, randomGenerator, board);
        board.addEnemy(enemy);
        levelFlow.addTickObserver(enemy);
        super.registerEventObservers(enemy, board, levelFlow, gameEventObserver);
        return enemy;
    }
}
