package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;

import java.text.ParseException;
import java.util.List;

public interface ChessBoardSetupNotation {

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
    public List<ChessPieceLocation> getChessPieceLocations(String setupNotation)
                    throws ParseException;

}