package org.amc.game.chess;

public class StandardChessGameFactory implements ChessGameFactory {
    
    public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite, ChessGamePlayer playerBlack) {
        return new StandardChessGame(board, playerWhite, playerBlack);
    }
}
