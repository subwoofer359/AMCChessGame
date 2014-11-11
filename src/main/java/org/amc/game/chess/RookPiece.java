package org.amc.game.chess;

public class RookPiece extends ComplexPiece {

    public RookPiece(Colour colour) {
        super(colour);
        // TODO Auto-generated constructor stub
    }

    @Override
    boolean validMovement(Move move) {
        return Move.isUpOrDownMove(move);
    }
}
