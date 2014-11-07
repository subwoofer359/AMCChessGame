package org.amc.game.chess;

public class KnightPiece extends SimplePiece {

    public KnightPiece(Colour colour) {
        super(colour);
    }

    @Override
    public boolean isValidMove(ChessBoard board, Move move) {
        return false;
    }



}
