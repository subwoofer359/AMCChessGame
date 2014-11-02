package org.amc.game.chess;
/**
 * Defines a Controller for the Chess game
 * 
 * @author Adrian Mclaughlin
 *
 */
public interface Controller {

    public Player getCurrentPlayer();
    
    /**
     * Change the current player
     */
    public void changePlayer();
    
    /**
     * @return current inputParser
     */
    public InputParser getInputParser();
}
