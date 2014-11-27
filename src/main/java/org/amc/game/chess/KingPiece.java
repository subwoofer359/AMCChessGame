package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a King in the game of Chess
 * 
 * The king moves one square in any direction
 * 
 * @author Adrian Mclaughlin
 * @see <a href="http://en.wikipedia.org/wiki/Chess">Chess</a>
 * 
 */
public class KingPiece extends SimplePiece {

    public KingPiece(Colour colour) {
        super(colour);
    }

    /**
     * @see SimplePiece#validMovement(Move)
     */
    @Override
    boolean validMovement(Move move) {
        return isMoveOneSquareInAnyDirection(move);
    }
    
    /**
     * @see SimplePiece#canMakeMove(ChessBoard, Move)
     */
    @Override
    boolean canMakeMove(ChessBoard board, Move move) {
        if (board.isEndSquareEmpty(move.getEnd())) {
            return true;
        } else {
            return this.isEndSquareOccupiedByOpponentsPiece(board, move);
        }
    }
    
    boolean isMoveOneSquareInAnyDirection(Move move){
        return move.getAbsoluteDistanceX() <= 1 && 
                        move.getAbsoluteDistanceY() <= 1;
    }
    
    /**
     * Find all possible move locations. If the piece isn't on the board the method will 
     * add the @param Location to the Set which isn't desirable
     * 
     * @param board ChessBoard on which the ChessPiece is on
     * @param location Location of the ChessPiece
     * @return HashSet of Locations
     */
    public Set<Location> getPossibleMoveLocations(ChessBoard board,Location location){
        Set<Location> locations=new HashSet<>();
        for(Coordinate coord:Coordinate.values()){
            for(int i=1;i<=board.BOARD_WIDTH;i++){
                Location moveLocation=new Location(coord,i);
                Move move=new Move(location,moveLocation);
                if(isValidMove(board, move)){
                    locations.add(moveLocation);
                }
            }
        }
        return locations;
    }
}
