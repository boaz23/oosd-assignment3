package dnd.logic.tileOccupiers;

import dnd.logic.GameException;
import dnd.logic.MoveResult;

/**
 * The base class for every game object on the board.
 */
public abstract class TileOccupier {
    /**
     * Returns the char representation of the game object
     */
    public abstract char toTileChar();

    /**
     * Visitor pattern for resolving this tile occupier type in order to behave according to it's type
     * @return The result of the move performed by the input tile occupier
     */
    protected abstract MoveResult accept(TileVisitor visitor) throws GameException;

    /**
     * Returns whether this tile occupier is a free tild
     */
    public abstract boolean isFree();

    @Override
    public String toString() {
        return toTileChar() + "";
    }

    public abstract TileOccupier clone();
}
