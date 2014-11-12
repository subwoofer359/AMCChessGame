package org.amc.game.chess;

/**
 * Represents a Queen Chess piece
 * 
 * @author Adrian Mclaughlin
 *
 */
public class QueenPiece extends ComplexPiece {

    public QueenPiece(Colour colour) {
        super(colour);
        // TODO Auto-generated constructor stub
    }

    @Override
    boolean validMovement(Move move) {
        return Move.isDiagonalMove(move) || Move.isUpOrDownMove(move);
    }
}
