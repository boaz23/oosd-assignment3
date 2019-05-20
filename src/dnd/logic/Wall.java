package dnd.logic;

public class Wall extends TileOccupierImpl {
    public Wall() {
        this.addProperty(TileProperty.Wall);
    }
}
