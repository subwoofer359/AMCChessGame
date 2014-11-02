package org.amc.game.chess;

import java.text.ParseException;

/**
 * Responsible for parsing User input into a form the game can understand
 * @author Adrian Mclaughlin
 *
 */
public interface InputParser {

    /**
     * Given a String in the Parser's associated format
     * @param moveString
     * @return Move move
     * 
     * @throws ParseException
     */
    public Move parseMoveString(String moveString) throws ParseException;
}
