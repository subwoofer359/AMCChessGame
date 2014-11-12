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
    public String getName();

    /**
     * Colour of the Player
     * 
     * @return enum Colour
     */
    public Colour getColour();

    /**
     * The player is the winner
     * 
     * @return boolean true if they are
     */
    public boolean isWinner();

    /**
     * Sets the Player's win state
     * 
     * @param isWinner
     */
    public void isWinner(boolean isWinner);
}
