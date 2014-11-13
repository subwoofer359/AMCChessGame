package org.amc.game.chess;

/**
 * Represents a Knight Chess Piece
 * 
 * The knight moves to any of the closest squares that are not on the same rank,
 * file, or diagonal, thus the move forms an "L"-shape: two squares vertically
 * and one square horizontally, or two squares horizontally and one square
 * vertically
 * 
 * possible moves (x-2,y-1),(x-2,y+1),(x-1,y+2),(x+1,y+2),
 * (x+2,y-1),(x+2,y+1),(x-1,y-2),(x+1,y-2)
 *
 * @author Adrian Mclaughlin
 * @see <a href="http://en.wikipedia.org/wiki/Chess">Chess</a>
 *
 */
public class KnightPiece extends SimplePiece {

    public KnightPiece(Colour colour) {
        super(colour);
    }

    /**
     * @see SimplePiece#isValidMove(ChessBoard, Move)
     */
    @Override
    boolean validMovement(Move move) {
        return move.getAbsoluteDistanceX() == 2 && move.getAbsoluteDistanceY() == 1
                        || move.getAbsoluteDistanceX() == 1 && move.getAbsoluteDistanceY() == 2;
    }

    /**
     * @see SimplePiece#canMakeMove(ChessBoard, Move)
     */
    @Override
    boolean canMakeMove(ChessBoard board, Move move) {
        if(isEndSquareEmpty(board, move)){
            return true;
        }else{        
            return isEndSquareOccupiedByOpponentsPiece(board, move);
        }
    }
}
