package org.amc.game.chessserver;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;

import java.beans.PropertyEditorSupport;
import java.util.regex.Pattern;

public class MoveEditor extends PropertyEditorSupport{
    Pattern pattern=Pattern.compile("\\w\\d->\\w\\d");
    
    public void setAsText(String text){
        String[] locations=text.split("-");
        setValue(new Move(new Location(Coordinate.valueOf(locations[0].substring(0,1)),
                            Integer.parseInt(locations[0].substring(1,2))), 
                        new Location(Coordinate.valueOf(locations[1].substring(0,1)),
                            Integer.parseInt(locations[1].substring(1,2)))));
    }

}
