package dnd.logic.tileOccupiers;

import dnd.logic.MoveResult;

public class Wall implements TileOccupier {
    @Override
    public char getTileChar() {
        return '#';
    }

    @Override
    public MoveResult accept(Unit unit) {
        unit.visit(this);
        return MoveResult.Invalid;
    }
}
