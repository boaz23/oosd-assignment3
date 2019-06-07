package dnd.controllers.tile_occupiers_factories;

import dnd.GameEventObserver;
import dnd.controllers.UnitFactory;
import dnd.logic.LevelFlow;
import dnd.logic.board.BoardImpl;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.TileOccupier;

public class PlayerFactory extends UnitFactory {
    private final Player player;

    public PlayerFactory(Player player) {
        this.player = player;
    }

    @Override
    public TileOccupier createTileOccupier(RandomGenerator randomGenerator, BoardImpl board, LevelFlow levelFlow, GameEventObserver gameEventObserver) {
        Player player = (Player)this.player.clone(randomGenerator, board);
        board.setPlayer(player);
        super.registerEventObservers(player, board, levelFlow, gameEventObserver);
        return player;
    }
}
