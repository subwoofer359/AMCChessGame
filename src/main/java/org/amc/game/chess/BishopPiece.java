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
    public BishopPiece(Colour colour) {
        super(colour);
    }

    @Override
    boolean validMovement(Move move) {
        return Move.isDiagonalMove(move);
    }    
}
