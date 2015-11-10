package org.amc.game.chess;

public class StandardChessGameFactory implements ChessGameFactory {

    @Override
    public ChessGame getChessGame() {
        return new StandardChessGame();
    }
    
    public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite, ChessGamePlayer playerBlack) {
        return new StandardChessGame(board, playerWhite, playerBlack);
    }
    
}
