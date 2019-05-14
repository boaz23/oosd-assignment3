package logic.enemies;

public abstract class Enemy {
    private int experienceValue;
    private char tile;

    public char getTile() {
        return tile;
    }

    public Integer getExperienceValue() {
        return experienceValue;
    }
}
