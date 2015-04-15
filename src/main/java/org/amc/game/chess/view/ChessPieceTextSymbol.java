package org.amc.game.chess.view;

import org.amc.game.chess.BishopPiece;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Colour;
import org.amc.game.chess.KingPiece;
import org.amc.game.chess.KnightPiece;
import org.amc.game.chess.PawnPiece;
import org.amc.game.chess.QueenPiece;
import org.amc.game.chess.RookPiece;

public final class ChessPieceTextSymbol {

    private ChessPieceTextSymbol() {
        throw new RuntimeException(ChessPieceTextSymbol.class.getSimpleName()+
                        " Can't be instantiated");
    }

    /**
     * @param piece
     *            ChessPiece
     * @return a Character representing the ChessPiece
     */
    public static Character getChessPieceTextSymbol(ChessPiece piece) {
        Character[] blackSymbols = { 'K', 'Q', 'B', 'N', 'R', 'P' };
        Character[] whiteSymbols = { 'k', 'q', 'b', 'n', 'r', 'p' };
        int index = -1;

        if (piece.getClass().equals(KingPiece.class)) {
            index = 0;
        } else if (piece.getClass().equals(QueenPiece.class)) {
            index = 1;
        } else if (piece.getClass().equals(BishopPiece.class)) {
            index = 2;
        } else if (piece.getClass().equals(KnightPiece.class)) {
            index = 3;
        } else if (piece.getClass().equals(RookPiece.class)) {
            index = 4;
        } else if (piece.getClass().equals(PawnPiece.class)) {
            index = 5;
        }

        if (piece.getColour().equals(Colour.BLACK)) {
            return blackSymbols[index];
        } else {
            return whiteSymbols[index];
        }
    }
}
