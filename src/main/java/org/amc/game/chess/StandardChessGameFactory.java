package org.amc.game.chess;

/**
 * Factory for creating Chess games with the standard rules
 * 
 * @author Adrian Mclaughlin
 *
 */
public class StandardChessGameFactory implements ChessGameFactory {
    
    /**
     * @see ChessGameFactory#getChessGame(ChessBoard, ChessGamePlayer, ChessGamePlayer)
     * @return {@link StandardChessGame}
     */
    public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite, ChessGamePlayer playerBlack) {
        return new StandardChessGame(board, playerWhite, playerBlack);
    }
}
