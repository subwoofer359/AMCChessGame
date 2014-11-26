package org.amc.game.chess;

abstract class PawnPieceRule implements ChessRule {
    
    boolean isPawnChessPiece(ChessPiece piece) {
        return piece != null && piece instanceof PawnPiece;
    }
}
