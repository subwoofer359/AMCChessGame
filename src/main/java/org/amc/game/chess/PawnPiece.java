package org.amc.game.chess;

public class PawnPiece extends SimplePiece {

    public PawnPiece(Colour colour) {
        super(colour);
    }

    @Override
    boolean validMovement(Move move) {
        return false;
    }

    @Override
    boolean canMakeMove(ChessBoard board, Move move) {
        return false;
    }

}
