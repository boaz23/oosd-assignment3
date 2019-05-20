package dnd.logic;

public abstract class BoardSquare {
    private TileOccupier tileOccupier;

    public BoardSquare(TileOccupier tileOccupier) {
        this.setTileOccupier(tileOccupier);
    }

    public TileOccupier getTileOccupier() {
        return tileOccupier;
    }

    public void setTileOccupier(TileOccupier tileOccupier) {
        if (tileOccupier == null) {
            throw new IllegalArgumentException("tileOccupier is null.");
        }

        this.tileOccupier = tileOccupier;
    }
}
