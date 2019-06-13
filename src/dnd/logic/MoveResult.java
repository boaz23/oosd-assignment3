package dnd.logic;

/**
 * Enumeration of possible move results performed by units
 */
public enum MoveResult {
    /**
     * The move attempt to the position resulted in a melee combat and the defender stayed alive
     */
    Engaged,

    /**
     * The move attempt to the position resulted in a melee combat and the defender died as a result
     */
    Dead,

    /**
     * The movement to the position is allowed
     */
    Allowed,

    /**
     * The movement to the position is illegal
     */
    Invalid
}
