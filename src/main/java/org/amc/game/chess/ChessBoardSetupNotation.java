package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;

import java.text.ParseException;
import java.util.List;

public interface ChessBoardSetupNotation {

    enum ChessPieceNotation {
        /** black king */
        K,
        /** black queen */
        Q,
        /** black bishop */
        B,
        /** black knight */
        N,
        /** black rook */
        R,
        /** black pawn */
        P,
        /** white king */
        k,
        /** white queen */
        q,
        /** white bishop */
        b,
        /** white rook */
        r,
        /** white knight */
        n,
        /** white pawn */
        p
    }
    
    /**
     * Creates a new Chessboard which is populated with the ChessPieces at
     * Locations specified in the input String
     * 
     * @param setupNotation
     *            input string which is parsed
     * @return ChessBoard
     * @throws ParseException
     *             if the input string can't be parsed
     */
    List<ChessPieceLocation> getChessPieceLocations(String setupNotation)
                    throws ParseException;
    
    
    /**
     * @param setupNotation
     * @return true if the String is in a valid notation
     */
    boolean isInputValid(String setupNotation);

}