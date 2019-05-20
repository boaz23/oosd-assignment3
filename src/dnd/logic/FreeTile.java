package dnd.logic;

public class FreeTile extends TileOccupierImpl {
    public FreeTile() {
        this.addProperty(TileProperty.Free);
    }
}
