package dnd.controllers.tile_occupiers_factories;

import dnd.cli.view.GameEventObserver;
import dnd.logic.LevelFlow;
import dnd.logic.board.InitializableBoard;
import dnd.logic.enemies.Enemy;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.Inanimate;

import java.util.HashMap;
import java.util.Map;

public class FactoriesMapBuilder {
    private final Map<Character, TileOccupierFactory> tileOccupierFactoryMap;

    public FactoriesMapBuilder() {
        tileOccupierFactoryMap = new HashMap<>();
    }

    public <T extends Enemy> FactoriesMapBuilder addEnemyFactories(
        T[] enemies,
        RandomGenerator randomGenerator,
        InitializableBoard board,
        LevelFlow levelFlow,
        GameEventObserver gameEventObserver) {
        for (Enemy enemy : enemies) {
            tileOccupierFactoryMap.put(enemy.getTileChar(), new EnemyFactory(enemy, randomGenerator, board, levelFlow, gameEventObserver));
        }

        return this;
    }

    public FactoriesMapBuilder addInanimatesFactories(Inanimate[] inanimates) {
        for (Inanimate inanimate : inanimates) {
            tileOccupierFactoryMap.put(inanimate.toTileChar(), new InanimateFactory(inanimate));
        }

        return this;
    }

    public Map<Character, TileOccupierFactory> build() {
        return tileOccupierFactoryMap;
    }
}
