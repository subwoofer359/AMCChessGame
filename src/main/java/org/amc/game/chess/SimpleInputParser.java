package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.controller.InputParser;

import java.text.ParseException;

/**
 * Parses text in the form 'H1A2' to create a Move object
 * 
 * @author Adrian Mclaughlin
 *
 */
public class SimpleInputParser implements InputParser {

    private static final String errorMessage = "Move must be enter like so: A1B2";
    private static final int INPUT_LENGTH = 4;

    public SimpleInputParser() {
    }

    /**
     * @see InputParser#parseMoveString(String)
     */
    @Override
    public Move parseMoveString(String moveString) throws ParseException {
        if (inputCorrectLength(moveString)) {
            String startCoordinate = moveString.substring(0, 2);
            String endCoordinate = moveString.substring(2, 4);
            try {
                return createMove(startCoordinate, endCoordinate);
            } catch (IllegalArgumentException iae) {
                throw new ParseException(errorMessage, 0);
            }
        } else {
            throw new ParseException(errorMessage, 0);
        }
    }

    private boolean inputCorrectLength(String moveString) {
        return moveString.length() == INPUT_LENGTH;
    }

    private Move createMove(String startCoordinate, String endCoordinate) {
        Location startSquare = new Location(Coordinate.valueOf(String.valueOf(startCoordinate
                        .charAt(0))), Integer.parseInt(String.valueOf(startCoordinate.charAt(1))));
        Location endSquare = new Location(
                        Coordinate.valueOf(String.valueOf(endCoordinate.charAt(0))),
                        Integer.parseInt(String.valueOf(endCoordinate.charAt(1))));
        return new Move(startSquare, endSquare);
    }

}
