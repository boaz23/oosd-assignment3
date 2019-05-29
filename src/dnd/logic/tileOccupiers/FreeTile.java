package dnd.logic.tileOccupiers;

import dnd.logic.MoveResult;

public class FreeTile implements TileOccupier {

    @Override
    public char getTileChar() {
        return '.';
    }

    @Override
    public MoveResult accept(Unit unit) {
        return unit.visit(this);
    }

    @Override
    public boolean isFree() {
        return true;
    }
}
