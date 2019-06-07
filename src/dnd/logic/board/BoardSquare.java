package dnd.logic.board;

import dnd.logic.tileOccupiers.TileOccupier;

class BoardSquare {
    private TileOccupier tileOccupier;

    public BoardSquare(TileOccupier tileOccupier) {
        setTileOccupier(tileOccupier);
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

    @Override
    public String toString() {
        return getTileOccupier().toString();
    }
}
