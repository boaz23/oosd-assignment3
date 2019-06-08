package dnd.logic.tileOccupiers;

import dnd.logic.LogicException;
import dnd.logic.MoveResult;

public class Wall extends Inanimate {
    @Override
    public char toTileChar() {
        return '#';
    }

    @Override
    public MoveResult accept(TileVisitor visitor) throws LogicException {
        return visitor.visit(this);
    }

    @Override
    public boolean isFree() {
        return false;
    }

    @Override
    public Wall clone() {
        return new Wall();
    }
}
