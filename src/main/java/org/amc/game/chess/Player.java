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
     * The player is the winner
     * 
     * @return boolean true if they are
     */
    boolean isWinner();

    /**
     * Sets the Player's win state
     * 
     * @param isWinner
     */
    void isWinner(boolean isWinner);
}
