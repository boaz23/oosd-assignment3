package dnd.logic.available_units;

import dnd.logic.Tick;
import dnd.logic.player.Mage;
import dnd.logic.player.Player;
import dnd.logic.player.Rogue;
import dnd.logic.player.Warrior;

public class AvailablePlayers {
    public static final Warrior JonSnow = new Warrior(
            "Jon Snow",
            300, 30, 4,
            new Tick(6));
    public static final Warrior TheHound = new Warrior(
            "The Hound",
            400, 20, 6,
            new Tick(4));

    // TODO: change spellpower back to 40
    public static final Mage Melisandre = new Mage(
            "Melisandre",
            160, 10, 1,
            15, 300, 30,
            5, 6
    );
    public static final Mage ThorosOfMyr = new Mage(
            "Thoros of Myr",
            250, 25, 3,
            15, 150, 50,
            3, 3
    );

    public static final Rogue AryaStark = new Rogue(
            "Arya Stark",
            150, 40, 2,
            20
    );
    public static final Rogue Bronn = new Rogue(
            "Bronn",
            250, 35, 3,
            60
    );

    public static final Player[] Players = {
            JonSnow,
            TheHound,
            Melisandre,
            ThorosOfMyr,
            AryaStark,
            Bronn
    };
}
