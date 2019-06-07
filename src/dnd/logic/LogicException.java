package dnd.logic;

import dnd.GameException;

@SuppressWarnings("unused")
public class LogicException extends GameException {
    public LogicException() {
    }

    public LogicException(String message) {
        super(message);
    }

    public LogicException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogicException(Throwable cause) {
        super(cause);
    }

    protected LogicException(String message,
                             Throwable cause,
                             boolean enableSuppression,
                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
