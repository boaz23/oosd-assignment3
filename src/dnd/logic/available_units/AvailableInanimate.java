package dnd.logic.available_units;

import dnd.logic.tileOccupiers.DeadPlayer;
import dnd.logic.tileOccupiers.FreeTile;
import dnd.logic.tileOccupiers.TileOccupier;
import dnd.logic.tileOccupiers.Wall;

public class AvailableInanimate {
    public static final FreeTile FreeTile = new FreeTile();
    public static final Wall Wall = new Wall();
    public static final DeadPlayer DeadPlayer = new DeadPlayer();

    public static final TileOccupier[] Inanimates = new TileOccupier[] {
            FreeTile,
            Wall,
            DeadPlayer
    };
}
