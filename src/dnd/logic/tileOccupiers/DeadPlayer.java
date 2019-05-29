package dnd.logic.tileOccupiers;

import dnd.logic.LogicException;
import dnd.logic.MoveResult;

public class DeadPlayer implements TileOccupier {
    @Override
    public char getTileChar() {
        return 'X';
    }

    @Override
    public MoveResult accept(Unit unit) throws LogicException {
        throw new LogicException("Player is dead, cannot accept unit.");
    }

    @Override
    public boolean isFree() {
        return false;
    }
}
