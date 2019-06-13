package dnd.logic.available_units;

import dnd.logic.Tick;
import dnd.logic.enemies.Trap;

/**
 * Lists every defined trap in the game.
 */
public class AvailableTraps {
    public static final Trap BonusTrap = new Trap(
        "Bonus \"Trap\"",
        1, 1, 1,
        250, 'B', 5,
        new Tick(6), new Tick(2)
    );

    public static final Trap QueensTrap = new Trap(
        "Queen’s Trap",
        250, 50, 10,
        100, 'Q', 4,
        new Tick(10), new Tick(4)
    );

    public static final Trap DeathTrap = new Trap(
        "Death Trap",
        500, 100, 20,
        250, 'D', 6,
        new Tick(10), new Tick(3)
    );

    public static final Trap[] Traps = new Trap[]{
        BonusTrap,
        QueensTrap,
        DeathTrap
    };
}
