package org.amc.game.chess;

/**
 * Represents a Queen Chess piece The queen combines the power of the rook and
 * bishop and can move any number of squares along rank, file, or diagonal.
 * 
 * @author Adrian Mclaughlin
 * @see <a href="http://en.wikipedia.org/wiki/Chess">Chess</a>
 *
 */
public final class QueenPiece extends ComplexPiece {

    private static final QueenPiece QUEEN_WHITE = new QueenPiece(Colour.WHITE);
    private static final QueenPiece QUEEN_BLACK = new QueenPiece(Colour.BLACK);
    private static final QueenPiece QUEEN_WHITE_MOVED = new QueenPiece(Colour.WHITE, true);
    private static final QueenPiece QUEEN_BLACK_MOVED = new QueenPiece(Colour.BLACK, true);

    public static QueenPiece getPiece(Colour colour) {
        if(Colour.WHITE == colour) {
            return QUEEN_WHITE;
        } else {
            return QUEEN_BLACK;
        }
    }

    private QueenPiece(Colour colour) {
        super(colour);
    }

    private QueenPiece(Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }

    /**
     * @see SimplePiece#validMovement(Move)
     */
    @Override
    boolean validMovement(Move move) {
        return Move.isDiagonalMove(move) || Move.isFileOrRankMove(move);
    }

    @Override
    public ChessPiece moved() {
        if(Colour.WHITE == getColour()) {
            return QUEEN_WHITE_MOVED;
        } else {
            return QUEEN_BLACK_MOVED;
        }
    }
}
