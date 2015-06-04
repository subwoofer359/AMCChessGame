package org.amc.game.chessserver;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.Player;
import org.amc.game.chess.SetupChessBoard;


public final class OneViewServerChessGame extends ServerChessGame {

	public OneViewServerChessGame(long uid, Player player) {
        super(uid, player);
        
	}

	@Override
	public void addOpponent(Player opponent) {
		this.opponent = new ChessGamePlayer(opponent, Colour.BLACK);
        ChessBoard board = new ChessBoard();
        SetupChessBoard.setUpChessBoardToDefault(board);
        chessGame = new ChessGame(board, getPlayer(), this.opponent);
        setCurrentStatus(ServerGameStatus.IN_PROGRESS);
	}
	
	
}
