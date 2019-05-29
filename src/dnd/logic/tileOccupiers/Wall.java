package dnd.logic.tileOccupiers;

import dnd.logic.LogicException;

public class Wall implements TileOccupier {
    @Override
    public char getTileChar() {
        return '#';
    }

    @Override
    public Object accept(TileVisitor visitor, Object state) throws LogicException {
        return visitor.visit(this, state);
    }

    @Override
    public boolean isFree() {
        return false;
    }
}
