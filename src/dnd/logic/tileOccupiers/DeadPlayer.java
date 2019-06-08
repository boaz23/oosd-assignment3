package dnd.logic.tileOccupiers;

import dnd.logic.LogicException;
import dnd.logic.MoveResult;

public class DeadPlayer extends Inanimate {
    @Override
    public char toTileChar() {
        return 'X';
    }

    @Override
    public MoveResult accept(TileVisitor visitor) throws LogicException {
        throw new LogicException("Player is dead, cannot accept unit.");
    }

    @Override
    public boolean isFree() {
        return false;
    }

    @Override
    public DeadPlayer clone() {
        return new DeadPlayer();
    }
}
