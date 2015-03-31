package org.amc.game.chessserver;

/**
 * Thrown when a String can't be converted to a Move object
 * @author Adrian Mclaughlin
 * @version 1.0
 *
 */
public class MalformedMoveException extends RuntimeException {

    private static final long serialVersionUID = -85542351940790396L;

    public MalformedMoveException() {
        super();
    }

    public MalformedMoveException(String message) {
        super(message);
    }

    public MalformedMoveException(Throwable cause) {
        super(cause);
    }

    public MalformedMoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedMoveException(String message, Throwable cause, boolean enableSuppression,
                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
