package org.amc.game.chessserver;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.Colour;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.Player;
import org.amc.game.chess.SetupChessBoard;
import org.amc.game.chess.VirtualChessGamePlayer;
import org.apache.log4j.Logger;

import javax.persistence.Entity;



@Entity
public class OneViewServerChessGame extends ServerChessGame {
    
    private static final long serialVersionUID = -8769100253729854597L;
    
    private static final Logger logger = Logger.getLogger(OneViewServerChessGame.class);
    
    public OneViewServerChessGame() {
        super();
    }
    
	public OneViewServerChessGame(long uid, Player player) {
        super(uid, player);
        
	}

	@Override
	public void addOpponent(Player opponent) {
	    checkForNull(Player.class, opponent);
	    if(ComparePlayers.comparePlayers(opponent, getPlayer())) {
	        logger.error("Player can't join their own game");
	    } else if(isGameAwaitingPlayer()) {
	        ChessBoard board = new ChessBoard();
	        SetupChessBoard.setUpChessBoardToDefault(board);
	        setChessGame(getChessGameFactory().getChessGame(board, getPlayer(), 
                        new VirtualChessGamePlayer(opponent, Colour.BLACK)));
	        setCurrentStatus(ServerGameStatus.IN_PROGRESS);
	    } else 
	    {
	        logger.error("Player can't chess game already in process");
	    }
	}
}
