package dnd.logic;

import dnd.logic.player.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerInRangeFinder extends AllUnitsInRangeFinder {
    public PlayerInRangeFinder(Level level) {
        super(level);
    }

    @Override
    public List<Unit> findUnitsInRange(Point position, int range, UnitType unitType) {
        if (unitType != UnitType.Player) {
            return super.findUnitsInRange(position, range, unitType);
        }

        Player player = this.level.getPlayerInRange(position, range);
        List<Unit> playerList;
        if (player != null) {
            playerList = new ArrayList<>(1);
            playerList.add(player);
        }
        else {
            playerList = new ArrayList<>(0);
        }

        return playerList;
    }
}
