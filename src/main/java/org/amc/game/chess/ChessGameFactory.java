package org.amc.game.chess;

/**
 * ChessGame Factory for ChessGame Object creation
 * @author Adrian Mclaughlin
 *
 */
public interface ChessGameFactory {
    /**
     * Creates a new instance of a Chess Game
     * 
     * @param board ChessBoard
     * @param playerWhite ChessGamePlayer
     * @param playerBlack ChessGamePlayer
     * @return ChessGame
     */
    AbstractChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite, ChessGamePlayer playerBlack);
}
