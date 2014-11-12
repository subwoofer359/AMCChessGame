package org.amc.game.chess;

/**
 * An exception that occurs when a Player try to move a Chess piece with an
 * illegal move
 * 
 * @author Adrian McLaughlin
 */

public class InvalidMoveException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidMoveException() {
        super();
    }

    public InvalidMoveException(String message) {
        super(message);

    }

    public InvalidMoveException(Throwable cause) {
        super(cause);

    }

    public InvalidMoveException(String message, Throwable cause) {
        super(message, cause);

    }

    public InvalidMoveException(String message, Throwable cause, boolean enableSuppression,
                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
