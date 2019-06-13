package dnd.logic.board;

import dnd.logic.GameException;

/**
 * An exception which occurs when an attempt was made to access the board game matrix on a non-existing position
 */
public class PositionOutOfBoundsException extends GameException {
    public PositionOutOfBoundsException() {
    }

    public PositionOutOfBoundsException(String message) {
        super(message);
    }

    public PositionOutOfBoundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public PositionOutOfBoundsException(Throwable cause) {
        super(cause);
    }

    protected PositionOutOfBoundsException(String message,
                                           Throwable cause,
                                           boolean enableSuppression,
                                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
