package org.amc.game.chess;

/**
 * Represents a Bishop in the game of chess
 * 
 * @author Adrian Mclaughlin
 *
 */
public class BishopPiece extends ComplexPiece {
    public BishopPiece(Colour colour) {
        super(colour);
    }

    @Override
    boolean validMovement(Move move) {
        return move.getAbsoluteDistanceX().equals(move.getAbsoluteDistanceY());
    }
}
