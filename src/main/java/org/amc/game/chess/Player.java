package org.amc.game.chess;

/**
 * Represents a chess Player
 * 
 * @author Adrian Mclaughlin
 *
 */
public interface Player {
    /**
     * Name of the Player
     * 
     * @return String
     */
    String getName();

    /**
     * Colour of the Player
     * 
     * @return enum Colour
     */
    Colour getColour();
    
    /**
     * Set Colour of Player
     * 
     * @param colour Colour
     */
    void setColour(Colour colour);
}
