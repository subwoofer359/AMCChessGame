package org.amc.game.chessserver;

/**
 * STOMP message related constants 
 * @author Adrian Mclaughlin
 *
 */
public final class StompConstants {
    public static final String MESSAGE_HEADER_TYPE="TYPE";
    
    private StompConstants() {
        throw new RuntimeException("Can't be instantiated");
    }
}
