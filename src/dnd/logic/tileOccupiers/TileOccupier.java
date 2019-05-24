package dnd.logic.tileOccupiers;

import dnd.logic.LogicException;
import dnd.logic.MoveResult;

public interface TileOccupier {
    char getTileChar();
    MoveResult accept(Unit unit) throws LogicException;
}
