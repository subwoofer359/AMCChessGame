package org.amc.game.chess;

import java.util.ArrayList;
import java.util.List;

public class KnightPiece extends SimplePiece {

    public KnightPiece(Colour colour) {
        super(colour);
    }

    
    boolean canMakeMove(ChessBoard board,Move move){
        Location endSquare=move.getEnd();
        return isEndSquareOccupiedByOpponentsPiece(board, endSquare);      
    }
    
    private boolean isEndSquareOccupiedByOpponentsPiece(ChessBoard board,Location endSquare){
        ChessPiece piece=board.getPieceFromBoardAt(endSquare.getLetter().getName(),endSquare.getNumber());
        if(piece==null){
            return true;
        }
        else if(piece.getColour().equals(getColour())){
            return false;
        }
        else{
            return true;
        }
    }
    
    boolean validMovement(Move move){
        return move.getAbsoluteDistanceX()==2 && move.getAbsoluteDistanceY()==1 ||
               move.getAbsoluteDistanceX()==1 && move.getAbsoluteDistanceY()==2;
    }

    /*
     * possible moves
     * (x-2,y-1),(x-2,y+1),(x-1,y+2),(x+1,y+2),
     * (x+2,y-1),(x+2,y+1),(x-1,y-2),(x+1,y-2)
     */
    List<Location> getValidMoveLocations(){
        List<Location> validLocations=new ArrayList<Location>();
        return validLocations;
    }

}
