package dnd.logic;

import dnd.GameException;

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
