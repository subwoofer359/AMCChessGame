package org.amc.game.chess;

public interface ChessGameFactory {
    ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite, ChessGamePlayer playerBlack);
}
