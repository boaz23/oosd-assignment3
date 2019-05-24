package dnd.logic.tileOccupiers;

import dnd.logic.MoveResult;

public class FreeTile implements TileOccupier {

    @Override
    public char getTileChar() {
        return '.';
    }

    @Override
    public MoveResult accept(Unit unit, Object state) {
        unit.visit(this, state);
        return MoveResult.Allowed;
    }
}
