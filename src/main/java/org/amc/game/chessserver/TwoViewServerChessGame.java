package org.amc.game.chessserver;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.Player;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chess.SetupChessBoard;
import org.apache.log4j.Logger;

import javax.persistence.Entity;

@Entity
public class TwoViewServerChessGame extends ServerChessGame {

    private static final long serialVersionUID = 4322777387785605817L;
    private static final Logger logger = Logger.getLogger(TwoViewServerChessGame.class);
    
    public TwoViewServerChessGame() {
        super();
    }
    
    public TwoViewServerChessGame(long uid, Player player) {
        super(uid, player);
      
    }
    
    public TwoViewServerChessGame(long uid, ChessGame chessGame) {
        super(uid, chessGame);
    }
    
    /**
     * Adds player to the black side of the chess game
     * Only valid if ServerChessGame is in AWAITING_PLAYER state, no exception
     * is thrown if not in that state
     * 
     * @param opponent
     *            Player
     */
    public void addOpponent(Player opponent) {
        checkForNull(Player.class, opponent);
        if (isGameAwaitingPlayer()) {
            if (ComparePlayers.comparePlayers(getPlayer(), opponent)) {
                logger.debug(String.format("Player:(%s) tried to join their own game",
                                opponent.getName()));
            } else {
                setupChessGame(opponent);
            }
        }
    }
        
    private void setupChessGame(Player opponent) {
        ChessBoard board = new ChessBoard();
        SetupChessBoard.setUpChessBoardToDefault(board);
        synchronized (this) {
            setChessGame(getChessGameFactory().getChessGame(board, getPlayer(), 
                            new RealChessGamePlayer(opponent, Colour.BLACK)));
            setCurrentStatus(ServerGameStatus.IN_PROGRESS);
        }
        notifyObservers(opponent);
    }
}
