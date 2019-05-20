package dnd.logic.enemies;

import dnd.RandomGenerator;
import dnd.logic.Board;
import dnd.logic.TileProperty;
import dnd.logic.Unit;

import java.util.HashSet;
import java.util.Set;

public abstract class Enemy extends Unit {
    protected static final Set<TileProperty> PlayerPropertySet = new HashSet<TileProperty>() {{
        add(TileProperty.Player);
    }};

    int experienceValue;
    char tile;

    public Enemy(String name,
                 int healthPool, int attack, int defense,
                 RandomGenerator randomGenerator,
                 int experienceValue, char tile) {
        super(name, healthPool, attack, defense, randomGenerator);
        this.init(experienceValue, tile);
    }

    protected Enemy(String name,
                    int healthPool, int attack, int defense,
                    RandomGenerator randomGenerator,
                    Board board,
                    int experienceValue, char tile) {
        super(name, healthPool, attack, defense, randomGenerator, board);
        this.init(experienceValue, tile);
    }

    private void init(int experienceValue, char tile) {
        if (experienceValue < 0) {
            throw new IllegalArgumentException("experienceValue must be a non-negative number.");
        }
        if (tile == '\0') {
            throw new IllegalArgumentException("the tile must be a valid character.");
        }

        this.experienceValue = experienceValue;
        this.tile = tile;

        this.addProperty(TileProperty.Enemy);
    }

    public int getExperienceValue() {
        return experienceValue;
    }
}
