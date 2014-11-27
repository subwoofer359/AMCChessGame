package org.amc.game.chess;

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
    
    @Override
    public boolean canSlide() {
        return true;
    }
}
