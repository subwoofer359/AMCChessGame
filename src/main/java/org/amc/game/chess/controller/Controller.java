package org.amc.game.chess.controller;

import org.amc.game.chess.IllegalMoveException;

/**
 * Defines a Controller for the Chess game
 * 
 * @author Adrian Mclaughlin
 *
 */
public interface Controller {

    /**
     * @return current inputParser
     */
    InputParser getInputParser();

    /**
     * 
     * @param parser
     *            InputParser
     */
    void setInputParser(InputParser parser);

    /**
     * Take a turn in the game
     * 
     * @throws IllegalMoveException
     */
    void takeTurn() throws IllegalMoveException;
}
