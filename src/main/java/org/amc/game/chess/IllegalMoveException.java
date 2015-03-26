package org.amc.game.chess;

/**
 * An exception that occurs when a Player try to move a Chess piece with an
 * illegal move
 * 
 * @author Adrian McLaughlin
 */

public class IllegalMoveException extends Exception {
    private static final long serialVersionUID = 1L;

    public IllegalMoveException(String message) {
        super(message);

    }

    public IllegalMoveException(Throwable cause) {
        super(cause);

    }

}
