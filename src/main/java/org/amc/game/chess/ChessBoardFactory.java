package org.amc.game.chess;

import java.text.ParseException;

public interface ChessBoardFactory {

    public ChessBoard getChessBoard(String setupNotation) throws ParseException;

}