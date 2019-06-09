package dnd.logic.tileOccupiers;

import dnd.logic.GameException;
import dnd.logic.MoveResult;

public abstract class TileOccupier {
    public abstract char toTileChar();
    protected abstract MoveResult accept(TileVisitor visitor) throws GameException;

    public abstract boolean isFree();

    @Override
    public String toString() {
        return toTileChar() + "";
    }

    public abstract TileOccupier clone();
}
