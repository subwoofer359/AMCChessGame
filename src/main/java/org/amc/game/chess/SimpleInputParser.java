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

    private static final String errorMoveMessage = "Move must be enter like so: A1B2";
    private static final String errorLocationMessage = "Move must be enter like so: A1";
    private static final int INPUT_MOVE_LENGTH = 4;
    private static final int INPUT_LOCATION_LENGTH = 2;

    /**
     * @see InputParser#parseMoveString(String)
     */
    @Override
    public Move parseMoveString(String moveString) throws ParseException {
        if (inputCorrectMoveLength(moveString)) {
            String startCoordinate = moveString.substring(0, 2);
            String endCoordinate = moveString.substring(2, 4);
            try {
                return createMove(startCoordinate, endCoordinate);
            } catch (IllegalArgumentException iae) {
                throw new ParseException(errorMoveMessage, 0);
            }
        } else {
            throw new ParseException(errorMoveMessage, 0);
        }
    }

    private boolean inputCorrectMoveLength(String moveString) {
        return moveString.length() == INPUT_MOVE_LENGTH;
    }
    
    private boolean inputCorrectLocationLength(String locationString) {
        return locationString.length() == INPUT_LOCATION_LENGTH;
    }

    private Move createMove(String startCoordinate, String endCoordinate) throws ParseException{
        Location startSquare = parseLocationString(startCoordinate);
        Location endSquare = parseLocationString(endCoordinate);
        return new Move(startSquare, endSquare);
    }
    
    public Location parseLocationString(String locationString) throws ParseException {
        if(inputCorrectLocationLength(locationString)) {
            Location location = new Location(Coordinate.valueOf(String.valueOf(locationString
                            .charAt(0))), Integer.parseInt(String.valueOf(locationString.charAt(1))));
            return location;
        } else {
            throw new ParseException(errorLocationMessage,0);
        }
    }

}
