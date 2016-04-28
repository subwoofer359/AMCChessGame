package org.amc.game.chessserver.observers;

import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.AbstractChessGame.GameState;
import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chessserver.AbstractServerChessGame;

public class OneViewGameStateListener extends GameStateListener {

    @Override
    void handleGameStatePromotion(AbstractServerChessGame serverChessGame, GameState gameState) {
        ChessGamePlayer player = serverChessGame.getChessGame().getCurrentPlayer();
        ChessPieceLocation cpl = serverChessGame.getChessGame().getChessBoard().getPawnToBePromoted(player.getColour());
        sendMessage(serverChessGame, gameState.toString() + " " + cpl.getLocation()); 
    }
 
    
}
