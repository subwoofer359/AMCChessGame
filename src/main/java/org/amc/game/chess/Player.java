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
    int getId();
    
    /**
     * set player's unique identifying number
     * 
     */
    void setId(int id);
    
    /**
     * Name of the Player
     * 
     * @return String
     */
    String getName();
    
    /**
     * UserName of Player
     * @return String
     */
    String getUserName();
    
    /**
     * Set the Player's username
     * @param userName String
     */
    void setUserName(String userName);
    
    Class<?> getType();
    
}
