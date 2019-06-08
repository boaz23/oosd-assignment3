package dnd.logic.tileOccupiers;

import dnd.logic.LogicException;
import dnd.logic.MoveResult;

public class FreeTile extends Inanimate {
    public static final char TileChar = '.';

    @Override
    public char toTileChar() {
        return TileChar;
    }

    @Override
    public MoveResult accept(TileVisitor visitor) throws LogicException {
        return visitor.visit(this);
    }

    @Override
    public boolean isFree() {
        return true;
    }

    @Override
    public FreeTile clone() {
        return new FreeTile();
    }
}
