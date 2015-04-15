package org.amc.game.chess;

abstract class PawnPieceRule implements ChessMoveRule {
    
    boolean isPawnChessPiece(ChessPiece piece) {
        return piece instanceof PawnPiece;
    }
}
