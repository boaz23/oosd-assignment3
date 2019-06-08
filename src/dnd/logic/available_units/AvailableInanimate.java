package dnd.logic.available_units;

import dnd.logic.tileOccupiers.DeadPlayer;
import dnd.logic.tileOccupiers.FreeTile;
import dnd.logic.tileOccupiers.Inanimate;
import dnd.logic.tileOccupiers.Wall;

public class AvailableInanimate {
    public static final FreeTile FreeTile = new FreeTile();
    public static final Wall Wall = new Wall();
    public static final DeadPlayer DeadPlayer = new DeadPlayer();

    public static final Inanimate[] Inanimates = new Inanimate[]{
        FreeTile,
        Wall,
        DeadPlayer
    };
}
