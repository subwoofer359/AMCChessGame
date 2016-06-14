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

    private static final RookPiece ROOK_WHITE = new RookPiece(Colour.WHITE);
    private static final RookPiece ROOK_BLACK  = new RookPiece(Colour.BLACK);
    private static final RookPiece ROOK_WHITE_MOVED  = new RookPiece(Colour.WHITE, true);
    private static final RookPiece ROOK_BLACK_MOVED  = new RookPiece(Colour.BLACK, true);


    public static RookPiece getRookPiece(Colour colour) {
        if(Colour.WHITE == colour) {
            return ROOK_WHITE;
        } else {
            return ROOK_BLACK;
        }
    }

    private RookPiece(Colour colour) {
        super(colour);
    }

    private RookPiece(Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
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
        return this;
    }

    @Override
    public ChessPiece moved() {
        if(Colour.WHITE == getColour()) {
            return ROOK_WHITE_MOVED;
        } else {
            return ROOK_BLACK_MOVED;
        }
    }
}
