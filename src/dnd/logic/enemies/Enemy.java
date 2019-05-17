package dnd.logic.enemies;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class Enemy {
    int experienceValue;
    char tile;

    public void takeDamage(int damage) {
        throw new NotImplementedException();
    }
}
