package org.amc.game.chessserver;

import org.amc.game.GameSubject;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.SetupChessBoard;
import org.apache.log4j.Logger;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;


/**
 * Represents a ChessGame Application resident in a Spring container
 * 
 * @author Adrian Mclaughlin
 * @version 1.0
 */
@Entity
@Table(name="serverChessGames")

@NamedQueries({
    @NamedQuery(name="serverChessGameByUid", query="SELECT x FROM ServerChessGame x where x.uid = ?1"),
})
public class ServerChessGame extends GameSubject implements Serializable {
    
    private static final long serialVersionUID = 2147129152958398504L;

    public enum ServerGameStatus {
        IN_PROGRESS,
        AWAITING_PLAYER,
        FINISHED
    }

    private static final Logger logger = Logger.getLogger(ServerChessGame.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    /**
     * The unique identifier of this game
     */
    @Column(unique=true, nullable=false)
    private long uid;
    
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="chessGame", unique=true, nullable=true)
    private ChessGame chessGame = null;
    
    @Column(nullable=false)
    private ServerGameStatus currentStatus;
    
    @OneToOne(cascade=CascadeType.ALL)
    private ChessGamePlayer player;
    
    @Version
    private int version;
    
    @Transient
    private transient ChessGameFactory chessGameFactory;
    
    /**
     * Constructor
     */
    
    protected ServerChessGame() {
        super();
    }
    
    /**
     * Constructor
     * 
     * @param uid
     *            long the unique identifier for this game
     * @param player
     *            Player who created the game and will be the white player
     */
    public ServerChessGame(long uid, Player player) { 
        super();
        checkForNull(Player.class, player);
        this.uid = uid;
        this.player = new ChessGamePlayer(player, Colour.WHITE);
        this.currentStatus = ServerGameStatus.AWAITING_PLAYER;
    }
    
    private void checkForNull(Class<?> cls, Object argument) {
    	if(argument == null) {
    		throw new IllegalArgumentException(
    				cls.getSimpleName() + "is null");
    	}
    }
    
    /**
     * Constructor 
     * 
     * Use a ChessGame Instance to create ServerChessGame
     * 
     * @param uid
     * @param chessGame Already initialise chess game
     */
    public ServerChessGame(long uid, ChessGame chessGame) {
        super();
        checkForNull(ChessGame.class, chessGame);
        this.uid = uid;
        this.player = chessGame.getWhitePlayer();
        this.currentStatus = ServerGameStatus.IN_PROGRESS;
        this.chessGame = chessGame;
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
        if (ServerGameStatus.AWAITING_PLAYER.equals(this.currentStatus)) {
            if (ComparePlayers.comparePlayers(this.player, opponent)) {
                logger.debug(String.format("Player:(%s) tried to join their own game",
                                opponent.getName()));
            } else {
            	ChessBoard board = new ChessBoard();
        		SetupChessBoard.setUpChessBoardToDefault(board);
            	synchronized (this) {
            		this.chessGame = chessGameFactory.getChessGame(board, this.player, 
            		                new ChessGamePlayer(opponent, Colour.BLACK));
            		this.currentStatus = ServerGameStatus.IN_PROGRESS;
            	}
            	notifyObservers(opponent);
            }
        }
    }

    /**
     * Get current status of the ServerChessGame
     * 
     * @return status enum
     */
    public ServerGameStatus getCurrentStatus() {
        synchronized (this) {
            return currentStatus;
        }
    }

    /**
     * Get the player who created the game
     * 
     * @return Player
     */
    public ChessGamePlayer getPlayer() {
        return player;
    }

    /**
     * Return ChessGamePlayer with the same ID as Player
     * 
     * @param player
     * @return ChessGamePlayer represents Player
     */
    public ChessGamePlayer getPlayer(Player player) {
        if(ComparePlayers.comparePlayers(this.player, player)) {
            return this.player;
        } else if(this.chessGame == null) {
            return null;
        } else {
            return this.chessGame.getBlackPlayer();
        } 
    }

    /**
     * Set the ServerGame's status
     * 
     * @param currentStatus
     */
    public void setCurrentStatus(ServerGameStatus currentStatus) {
        synchronized (this) {
            this.currentStatus = currentStatus;
        }
        notifyObservers(this.currentStatus);
    }
    
    
    /**
     * Set the ChessGame game object
     * 
     * @param chessGame 
     */
    protected void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    /**
     * 
     * @return the ChessGame object
     */
    public ChessGame getChessGame() {
        return chessGame;
    }

    /**
     * @return Player opposing player
     */
    public ChessGamePlayer getOpponent() {
        if(this.chessGame == null) {
            return null;
        }
        return this.chessGame.getBlackPlayer();
    }

    /**
     * Sends a move to the ChessGame
     * If successful the next player can take their turn
     * Updates the ServerChessGame's status
     * 
     * @param player
     *            Player who's is making the move
     * @param move
     *            Move to be made
     * @throws IllegalMoveException
     *             if Move is illegal
     */
    public void move(ChessGamePlayer player, Move move) throws IllegalMoveException {
        if (chessGame != null) {
            synchronized (chessGame) {
                chessGame.move(player, move);
                chessGame.changePlayer();
            }
            notifyObservers(this.chessGame);
            checkGameStatus();
        }
    }

    /**
     * Retrieve the Unique identifier of the {@link ServerChessGame}
     * 
     * @return long Unique identifier
     */
    public long getUid() {
        return uid;
    }

    public void setChessGameFactory(ChessGameFactory chessGameFactory) {
        this.chessGameFactory = chessGameFactory;
    }
    
    ChessGameFactory getChessGameFactory() {
        return this.chessGameFactory;
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
            break;

        case BLACK_IN_CHECK:
        case WHITE_IN_CHECK:
            notifyObservers(chessGame.getGameState());
            break;

        default:
            break;
        }
    }

    /**
     * Remove Observers from receiving updates
     * Sets Status to FINISHED
     * Tidys up references
     */
    public void destroy() {
        setCurrentStatus(ServerGameStatus.FINISHED);
        removeAllObservers();
        this.chessGame = null;
    }
    
    @Override
    public String toString() {
        return "ServerChessGame[" + getPlayer() + " vs " + getOpponent() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + (int) (uid ^ (uid >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServerChessGame other = (ServerChessGame) obj;
        if (id != other.id)
            return false;
        if (uid != other.uid)
            return false;
        return true;
    }
    
    
}
