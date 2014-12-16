package org.amc.game.chess;

/**
 * Represents a Queen Chess piece The queen combines the power of the rook and
 * bishop and can move any number of squares along rank, file, or diagonal.
 * 
 * @author Adrian Mclaughlin
 * @see <a href="http://en.wikipedia.org/wiki/Chess">Chess</a>
 *
 */
public class QueenPiece extends ComplexPiece {

    public QueenPiece(Colour colour) {
        super(colour);
        // TODO Auto-generated constructor stub
    }

    /**
     * @see SimplePiece#validMovement(Move)
     */
    @Override
    boolean validMovement(Move move) {
        return Move.isDiagonalMove(move) || Move.isFileOrRankMove(move);
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
        QueenPiece piece = new QueenPiece(this.getColour());
        if (this.hasMoved()) {
            piece.moved();
        }
        return piece;
    }
}
