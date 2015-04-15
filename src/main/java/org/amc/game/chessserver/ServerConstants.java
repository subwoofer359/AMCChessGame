package org.amc.game.chessserver;

/**
 * Constants used between Controllers
 * 
 * @author Adrian Mclaughlin
 *
 */
public final class ServerConstants {
    public static final String GAMEMAP = "GAMEMAP";
    public static final String PLAYER = "PLAYER";
    public static final String GAME_UUID = "GAME_UUID";
    public static final String GAME = "GAME";
    public static final String CHESSPLAYER = "CHESSPLAYER";
    public static final String ERRORS = "ERRORS";
    
    
    private ServerConstants() {
        throw new RuntimeException("Can't be instantiated");
    }
}
