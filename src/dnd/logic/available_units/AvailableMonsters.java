package dnd.logic.available_units;
import dnd.logic.enemies.Monster;

public class AvailableMonsters {
    public static final Monster LannisterSolider = new Monster(
            "Lannister Solider",
            80, 8, 3,
            25, 's', 3
    );

    public static final Monster LannisterKnight = new Monster(
            "Lannister Knight",
            200, 14, 8,
            50, 'k', 4
    );

    public static final Monster QueensGuard = new Monster(
            "Queen’s Guard",
            400, 20, 15,
            100, 'q', 5
    );

    public static final Monster Wright = new Monster(
            "Wright",
            600, 30, 15,
            100, 'z', 3
    );

    public static final Monster BearWright = new Monster(
            "Bear-Wright",
            1000, 75, 30,
            250, 'b', 4
    );

    public static final Monster GiantWright = new Monster(
            "Giant-Wright",
            1500, 100, 40,
            500, 'g', 5
    );

    public static final Monster WhiteWalker = new Monster(
            "White Walker",
            2000, 150, 50,
            1000, 'w', 6
    );

    public static final Monster TheMountain = new Monster(
            "The Mountain",
            1000, 60, 25,
            500, 'M', 6
    );

    public static final Monster QueenCersei = new Monster(
            "Queen Cersei",
            100, 10, 10,
            1000, 'C', 1
    );

    public static final Monster NightsKing = new Monster(
            "NightsKing",
            5000, 300, 150,
            5000, 'K', 8
    );

    public static final Monster[] Monsters = new Monster[] {
            LannisterSolider,
            LannisterKnight,
            QueensGuard,
            Wright,
            BearWright,
            GiantWright,
            WhiteWalker,
            TheMountain,
            QueenCersei,
            NightsKing
    };
}
