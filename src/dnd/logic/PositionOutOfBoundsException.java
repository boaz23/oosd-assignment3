package dnd.logic;

public class PositionOutOfBoundsException extends Exception {
    public PositionOutOfBoundsException() {
        super();
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
