package org.amc.game.chess;

import java.io.Serializable;

abstract class PawnPieceRule implements ChessMoveRule, Serializable {
    
    boolean isPawnChessPiece(ChessPiece piece) {
        return piece instanceof PawnPiece;
    }
}
