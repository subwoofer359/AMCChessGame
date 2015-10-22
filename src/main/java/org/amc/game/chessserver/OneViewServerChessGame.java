package org.amc.game.chessserver;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.Player;
import org.amc.game.chess.SetupChessBoard;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Version;


@Entity
public class OneViewServerChessGame extends ServerChessGame {
    
    private static final long serialVersionUID = -8769100253729854597L;
    
    public OneViewServerChessGame() {
        super();
    }
    
	public OneViewServerChessGame(long uid, Player player) {
        super(uid, player);
        
	}

	@Override
	public void addOpponent(Player opponent) {
        ChessBoard board = new ChessBoard();
        SetupChessBoard.setUpChessBoardToDefault(board);
        this.chessGame = new ChessGame(board, getPlayer(), new ChessGamePlayer(opponent, Colour.BLACK));
        setCurrentStatus(ServerGameStatus.IN_PROGRESS);
	}
}
