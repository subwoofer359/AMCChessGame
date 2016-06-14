package org.amc.game.chess;

/**
 * Represents a Bishop in the game of chess
 * 
 * The bishop can move any number of squares diagonally.
 * 
 * @author Adrian Mclaughlin
 * @see <a href="http://en.wikipedia.org/wiki/Chess">Chess</a>
 *
 */
public class BishopPiece extends ComplexPiece {

    private static final BishopPiece BISHOP_WHITE = new BishopPiece(Colour.WHITE);
    private static final BishopPiece BISHOP_BLACK = new BishopPiece(Colour.BLACK);
    private static final BishopPiece BISHOP_WHITE_MOVED = new BishopPiece(Colour.WHITE, true);
    private static final BishopPiece BISHOP_BLACK_MOVED = new BishopPiece(Colour.BLACK, true);

    private BishopPiece(Colour colour) {
        super(colour);
    }

    private BishopPiece(Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }

    public static BishopPiece getBishopPiece(Colour colour) {
        if (colour == Colour.WHITE) {
            return BISHOP_WHITE;
        } else {
            return BISHOP_BLACK;
        }
    }

    /**
     * @see SimplePiece#validMovement(Move)
     */
    @Override
    boolean validMovement(Move move) {
        return Move.isDiagonalMove(move);
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
            return BISHOP_WHITE_MOVED;
        } else {
            return BISHOP_BLACK_MOVED;
        }
    }
}
