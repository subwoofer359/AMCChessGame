package org.amc.game.chessserver;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Colour;
import org.amc.game.chess.InvalidMoveException;
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
    public enum status{
        IN_PROGRESS,
        AWAITING_PLAYER,
        FINISHED
    }

    private static final Logger logger = Logger.getLogger(ServerChessGame.class);
    
    ChessGame chessGame=null;
    private status currentStatus;
    private Player player;
    private Player opponent;

    public ServerChessGame(Player player) {
        this.player=player;
        this.player.setColour(Colour.WHITE);
        this.currentStatus=status.AWAITING_PLAYER;
    }
    
    /**
     * Adds player to the black side of the chess game
     * Only valid if ServerChessGame is in AWAITING_PLAYER state, no exception is thrown if not in that state
     * @param player Player
     */
    public synchronized void addOpponent(Player player){
        if(this.currentStatus.equals(status.AWAITING_PLAYER)){
            player.setColour(Colour.BLACK);
            this.opponent=player;
            ChessBoard board=new ChessBoard();
            SetupChessBoard.setUpChessBoardToDefault(board);
            chessGame=new ChessGame(board,this.player,player);
            this.currentStatus=status.IN_PROGRESS;
        }
    }

    /**
     * Get current status of the ServerChessGame
     * @return status enum
     */
    public final status getCurrentStatus() {
        return currentStatus;
    }

    /**
     * Get the player who created the game
     * @return Player
     */
    public final Player getPlayer() {
        return player;
    }

    
    /**
     * Set the ServerGame's status
     * @param currentStatus
     */
    public synchronized final void setCurrentStatus(status currentStatus) {
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
    public final Player getOpponent(){
        return opponent;
    }
    
    /**
     * Sends a move to the ChessGame
     * If successful the next player can take their turn
     * Updates the ServerChessGame's status
     * 
     * @param player Player who's is making the move
     * @param move Move to be made
     * @throws InvalidMoveException if Move is illegal
     */
    public final void move(Player player,Move move) throws InvalidMoveException{
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
        case STALEMATE:
            logger.info("Game has ended in a stalemate");
            break;
            
        case WHITE_CHECKMATE:
            logger.info(opponent.getName() + " has won!");
            setCurrentStatus(status.FINISHED);
            break;
            
        case BLACK_CHECKMATE:
            logger.info(player.getName() + " has won!");
            setCurrentStatus(status.FINISHED);
            break;
            
        default:
        }
    }
}
