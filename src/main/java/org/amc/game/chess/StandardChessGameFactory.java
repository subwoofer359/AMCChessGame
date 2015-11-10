package org.amc.game.chess;

public class StandardChessGameFactory implements ChessGameFactory {
    
    public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite, ChessGamePlayer playerBlack) {
        ChessGame chessGame = new ChessGame(board, playerWhite, playerBlack);
        
        return addRules(chessGame);
    }
    
    private ChessGame addRules(ChessGame chessGame) {
        chessGame.addChessMoveRule(EnPassantRule.getInstance());
        chessGame.addChessMoveRule(CastlingRule.getInstance());
        chessGame.addChessMoveRule(PawnPromotionRule.getInstance());
        return chessGame;
    }
}
