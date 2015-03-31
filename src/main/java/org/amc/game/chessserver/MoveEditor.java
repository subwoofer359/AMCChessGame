package org.amc.game.chessserver;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.apache.log4j.Logger;

import java.beans.PropertyEditorSupport;
import java.util.regex.Pattern;

/**
 * Converts a String of the form "A1-B2" into a Move object
 * 
 * @author Adrian Mclaughlin
 * @version 1.0
 *
 */
public class MoveEditor extends PropertyEditorSupport {
    
    private static final Logger logger = Logger.getLogger(MoveEditor.class);
    
    String regex = "\\w\\d-\\w\\d";

    String errorMsg="Malformed move received( %s )";
    
    /**
     * @see PropertyEditorSupport#setAsText(String)
     * @throws MalformedMoveException when the given string can't be converted to a Move object
     */
    @Override
    public void setAsText(String text) {
        if(Pattern.matches(regex, text)){
            String[] locations = text.split("-");
            setValue(new Move(new Location(Coordinate.valueOf(locations[0].substring(0, 1)),
                        Integer.parseInt(locations[0].substring(1, 2))), new Location(
                        Coordinate.valueOf(locations[1].substring(0, 1)),
                        Integer.parseInt(locations[1].substring(1, 2)))));
        } else {
            logger.error(String.format(errorMsg, text));
            throw new MalformedMoveException(String.format(errorMsg, text));
        }
    }

}
