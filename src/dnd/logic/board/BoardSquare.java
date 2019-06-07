package dnd.logic.board;

import dnd.logic.tileOccupiers.TileOccupier;

public class BoardSquare {
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
