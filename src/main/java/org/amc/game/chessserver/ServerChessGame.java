package org.amc.game.chessserver;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.SetupChessBoard;
import org.amc.util.DefaultSubject;
import org.apache.log4j.Logger;
 
/**
 * Represents a ChessGame Application resident in a Spring container
 * 
 * @author Adrian Mclaughlin
 * @version 1.0
 */
public class ServerChessGame extends DefaultSubject {
    public enum ServerGameStatus{
        IN_PROGRESS,
        AWAITING_PLAYER,
        FINISHED
    }
    
    private static final Logger logger = Logger.getLogger(ServerChessGame.class);
    
    ChessGame chessGame=null;
    private ServerGameStatus currentStatus;
    private ChessGamePlayer player;
    private ChessGamePlayer opponent;

    public ServerChessGame(Player player) {
        this.player=new ChessGamePlayer(player,Colour.WHITE);
        this.currentStatus=ServerGameStatus.AWAITING_PLAYER;
    }
    
    /**
     * Adds player to the black side of the chess game
     * Only valid if ServerChessGame is in AWAITING_PLAYER state, no exception is thrown if not in that state
     * @param opponent Player
     */
    public synchronized void addOpponent(Player opponent){
        if(this.currentStatus.equals(ServerGameStatus.AWAITING_PLAYER)){
            if(this.player.equals(opponent)){
                logger.debug(String.format("Player:(%s) tried to join their own game", opponent.getName()));;
            } else {
                
                this.opponent=new ChessGamePlayer(opponent,Colour.BLACK);
                ChessBoard board=new ChessBoard();
                SetupChessBoard.setUpChessBoardToDefault(board);
                chessGame=new ChessGame(board,this.player,this.opponent);
                this.currentStatus=ServerGameStatus.IN_PROGRESS;
            }
        }
    }

    /**
     * Get current status of the ServerChessGame
     * @return status enum
     */
    public synchronized final ServerGameStatus getCurrentStatus() {
        return currentStatus;
    }

    /**
     * Get the player who created the game
     * @return Player
     */
    public final ChessGamePlayer getPlayer() {
        return player;
    }

    
    /**
     * Set the ServerGame's status
     * @param currentStatus
     */
    public synchronized final void setCurrentStatus(ServerGameStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    /**
     * 
     * @return the ChessGame object
     */
    public final ChessGame getChessGame() {
        return chessGame;
    }
    
    /**
     * @return Player opposing player 
     */
    public final ChessGamePlayer getOpponent(){
        return opponent;
    }
    
    /**
     * Sends a move to the ChessGame
     * If successful the next player can take their turn
     * Updates the ServerChessGame's status
     * 
     * @param player Player who's is making the move
     * @param move Move to be made
     * @throws IllegalMoveException if Move is illegal
     */
    public final void move(ChessGamePlayer player,Move move) throws IllegalMoveException{
        if(chessGame!=null){
            synchronized(chessGame){
                chessGame.move(player, move);
                chessGame.changePlayer();
            }
            notifyObservers(this.chessGame);
            checkGameStatus();
        }
    }
    
    /**
     * Checks to see if the game is finished and sets it's status accordingly
     * No check for chessGame being null
     */
    private void checkGameStatus() {
        switch (chessGame.getGameState()) {
        case RUNNING:
            break;
            
        case WHITE_CHECKMATE:
        case BLACK_CHECKMATE:
        case STALEMATE:
            setCurrentStatus(ServerGameStatus.FINISHED);
            notifyObservers(chessGame.getGameState());
            break;
            
        case BLACK_IN_CHECK:
        case WHITE_IN_CHECK:
            notifyObservers(chessGame.getGameState());
            break;
            
        default:
            break;
        }
    }
}
