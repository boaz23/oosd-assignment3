package dnd.logic.tileOccupiers;

import dnd.logic.LogicException;

public class DeadPlayer implements TileOccupier {
    @Override
    public char getTileChar() {
        return 'X';
    }

    @Override
    public Object accept(TileVisitor visitor, Object state) throws LogicException {
        throw new LogicException("Player is dead, cannot accept unit.");
    }

    @Override
    public boolean isFree() {
        return false;
    }
}
