package org.amc.game.chess;

/**
 * Represents a chess Player
 * 
 * @author Adrian Mclaughlin
 *
 */
public interface Player {
    
    /**
     * get player's unique identifying number
     * 
     * @return uid Integer
     */
    int getUid();
    
    /**
     * set player's unique identifying number
     * 
     */
    void setUid(int uid);
    
    /**
     * Name of the Player
     * 
     * @return String
     */
    String getName();
    
}
