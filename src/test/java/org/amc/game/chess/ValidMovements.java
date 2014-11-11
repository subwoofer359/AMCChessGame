package org.amc.game.chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidMovements {

    private static final Location[] arrayOfDiagonalLocationsFromD5={
                    //moving diagonal to the top right
                    new Location(ChessBoard.Coordinate.E,6),
                    new Location(ChessBoard.Coordinate.F,7),
                    new Location(ChessBoard.Coordinate.G,8),
                    
                    //moving to the top left
                    new Location(ChessBoard.Coordinate.C,6),
                    new Location(ChessBoard.Coordinate.B,7),
                    new Location(ChessBoard.Coordinate.A,8),
                    
                    //moving to the bottom left
                    new Location(ChessBoard.Coordinate.C,4),
                    new Location(ChessBoard.Coordinate.B,3),
                    new Location(ChessBoard.Coordinate.A,2),
                    
                    //moving to the bottom right
                    new Location(ChessBoard.Coordinate.E,4),
                    new Location(ChessBoard.Coordinate.F,3),
                    new Location(ChessBoard.Coordinate.G,2),
                    new Location(ChessBoard.Coordinate.H,1),
                    
                
    };
    
    private static final Location[] arrayOfDUpAndDownLocationsFromD5={
                    //moving to the top of the board
                    new Location(ChessBoard.Coordinate.D,6),
                    new Location(ChessBoard.Coordinate.D,7),
                    new Location(ChessBoard.Coordinate.D,8),
                    
                    //moving to the bottom of the board
                    new Location(ChessBoard.Coordinate.D,4),
                    new Location(ChessBoard.Coordinate.D,3),
                    new Location(ChessBoard.Coordinate.D,2),
                    new Location(ChessBoard.Coordinate.D,1),
                    
                    //moving to the right of the board
                    new Location(ChessBoard.Coordinate.E,5),
                    new Location(ChessBoard.Coordinate.F,5),
                    new Location(ChessBoard.Coordinate.G,5),
                    new Location(ChessBoard.Coordinate.H,5),
                    
                    //moving to the left of the board
                    new Location(ChessBoard.Coordinate.C,5),
                    new Location(ChessBoard.Coordinate.B,5),
                    new Location(ChessBoard.Coordinate.A,5)
                    
    };
    
    private final static List<Location> diagonalLocationsFromD5;
    private final static List<Location> upAndDownLocationsFromD5;
    
    static{
        diagonalLocationsFromD5=new ArrayList<>();
        upAndDownLocationsFromD5=new ArrayList<>();
        
        for(Location l:arrayOfDiagonalLocationsFromD5){
            diagonalLocationsFromD5.add(l);
        }
        for(Location l:arrayOfDUpAndDownLocationsFromD5){
            upAndDownLocationsFromD5.add(l);
        }
    }
    
    private ValidMovements() {
       throw new RuntimeException();
    }
    
    public static List<Location> getListOfDiagonalLocationsFromD5(){
        return Collections.unmodifiableList(diagonalLocationsFromD5);
    }
    
    public static List<Location> getListOfUpAndDownLocationsFromD5(){
        return Collections.unmodifiableList(upAndDownLocationsFromD5);
    }
    
    
}
