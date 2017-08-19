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
    public static final String ERRORS_MODEL_ATTR = "errors";
    /* Passed to the View */
    public static final String GAME_TYPE = "gameType";
    public static final String ONE_VIEW = "ONEVIEW";
    public static final String TWO_VIEW = "TWOVIEW";    
    
    private ServerConstants() {
        throw new AssertionError("Can't be instantiated");
    }
}
