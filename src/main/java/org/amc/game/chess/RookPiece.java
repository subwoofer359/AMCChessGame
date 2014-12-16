package org.amc.game.chess;

/**
 * Represents a Rook in the game of Chess
 *
 * The rook can move any number of squares along any rank or file
 * 
 * @author Adrian Mclaughlin
 * @see <a href="http://en.wikipedia.org/wiki/Chess">Chess</a>
 */
public class RookPiece extends ComplexPiece {

    public RookPiece(Colour colour) {
        super(colour);
    }

    /**
     * @see ComplexPiece#validMovement(Move)
     */
    @Override
    boolean validMovement(Move move) {
        return Move.isFileOrRankMove(move);
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
        RookPiece piece=new RookPiece(this.getColour());
        if(this.hasMoved()){
            piece.moved();
        }
        return piece;
    }
}
