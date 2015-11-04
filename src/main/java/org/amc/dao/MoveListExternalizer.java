package org.amc.dao;

import org.amc.game.chess.Move;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Utility class used by OpenJPA to convert a move into a String
 * 
 * @author Adrian Mclaughlin
 * 
 */
public class MoveListExternalizer {

    private MoveListExternalizer() {
    }
    
    public static String stringOfAllMoves(List<Move> allMoves) {
        StringBuilder sb = new StringBuilder();
        
        Iterator<Move> i=allMoves.iterator();
        while(i.hasNext()) {
            Move move = i.next();
            sb.append(move.asString());
            if(i.hasNext()) {
                sb.append(Move.MOVE_SEPARATOR);
            }
        }
        return sb.toString();
    }
    
    public static List<Move> listOfMovesFromString(String movesString) {
        
        List<Move> allMovesList = new ArrayList<Move>();
        Scanner scanner = new Scanner(movesString);
        scanner.useDelimiter(":");
        while(scanner.hasNext()) {
            allMovesList.add(new Move(scanner.next()));
        }
        scanner.close();
        return allMovesList;
    }

}
