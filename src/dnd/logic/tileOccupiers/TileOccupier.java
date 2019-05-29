package dnd.logic.tileOccupiers;

import dnd.logic.LogicException;

public interface TileOccupier {
    char getTileChar();
    Object accept(TileVisitor visitor, Object state) throws LogicException;

    boolean isFree();
}
