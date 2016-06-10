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
       return board.isEndSquareEmpty(move.getEnd()) ||
               this.isEndSquareOccupiedByOpponentsPiece(board, move);
    }
    
    boolean isMoveOneSquareInAnyDirection(Move move){
        return move.getAbsoluteDistanceX() <= 1 && 
                        move.getAbsoluteDistanceY() <= 1;
    }
    
    /**
     * @see ChessPiece#canSlide()
     */
    @Override
    public boolean canSlide() {
        return true;
    }
    
    /**
     * @see ChessPiece#copy()
     */
    @Override
    public ChessPiece copy() {
        KingPiece piece=new KingPiece(this.getColour());
        if(this.hasMoved()){
            piece.moved();
        }
        return piece;
    }
}
