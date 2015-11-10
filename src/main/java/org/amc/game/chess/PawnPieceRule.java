package org.amc.game.chess;

abstract class PawnPieceRule implements ChessMoveRule {
    
    final boolean isPawnChessPiece(ChessPiece piece) {
        return piece instanceof PawnPiece;
    }
}
