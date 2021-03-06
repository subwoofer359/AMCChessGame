package org.amc.game.chessserver;

import static org.amc.game.chess.NoChessGame.NO_CHESSGAME;
import static org.amc.game.chess.NoPlayer.NO_PLAYER;
import org.amc.game.GameSubject;
import org.amc.game.chess.AbstractChessGame;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.Version;

@MappedSuperclass
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class AbstractServerChessGame extends GameSubject implements Serializable {

    private static final long serialVersionUID = 2147129152958398504L;

    public enum ServerGameStatus {
        NEW,
        IN_PROGRESS,
        AWAITING_PLAYER,
        FINISHED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    /**
     * The unique identifier of this game
     */
    @Column(unique=true, nullable=false)
    private long uid;
    
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="chessGame", nullable=true)
    private AbstractChessGame chessGame = null;
    
    @Column(nullable=false)
    private ServerGameStatus currentStatus;
    
    @OneToOne(cascade=CascadeType.ALL)
    private ChessGamePlayer player;
    
    @Version
    private int version;
    
    @Transient
    transient ChessGameFactory chessGameFactory;
    
    /**
     * Constructor
     */
    
    protected AbstractServerChessGame() {
        super();
        this.currentStatus = ServerGameStatus.NEW;
    }
    
    /**
     * Constructor TODO
     * 
     * @param uid
     *            long the unique identifier for this game
     * @param player
     *            Player who created the game and will be the white player
     */
    public AbstractServerChessGame(long uid, ChessGamePlayer player) {
        super();
        checkForNull(Player.class, player);
        this.uid = uid;
        this.player = player;
        this.currentStatus = ServerGameStatus.AWAITING_PLAYER;
    }
    
    void checkForNull(Class<?> cls, Object argument) {
        if(argument == null) {
            throw new IllegalArgumentException(
                    cls.getSimpleName() + " argument is null");
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
    public AbstractServerChessGame(long uid, AbstractChessGame chessGame) {
        super();
        checkForNull(AbstractChessGame.class, chessGame);
        this.uid = uid;
        this.player = chessGame.getWhitePlayer();
        this.currentStatus = ServerGameStatus.IN_PROGRESS;
        this.chessGame = new ChessGame(chessGame);
    }

    /**
     * Adds player to the black side of the chess game
     * Only valid if ServerChessGame is in AWAITING_PLAYER state, no exception
     * is thrown if not in that state
     * 
     * @param opponent
     *            Player
     */
    public abstract void addOpponent(Player opponent);

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
        return player == null ? NO_PLAYER : player;
    }

    /**
     * Return ChessGamePlayer with the same ID as Player
     * 
     * @param player
     * @return ChessGamePlayer represents Player
     */
    public ChessGamePlayer getPlayer(Player player) {
        checkForNull(Player.class, player);
        if(ComparePlayers.isSamePlayer(this.player, player)) {
            return this.player;
        } else if (ComparePlayers.isSamePlayer(getOpponent(), player)) {
            return getChessGame().getBlackPlayer();
        } else if (getChessGame() == NO_CHESSGAME) {
        	return NO_PLAYER;	
        } else {
        	throw new IllegalArgumentException("Player not part of the game");
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
            notifyObservers(this.currentStatus);
        }
    }
    
    
    /**
     * Set the ChessGame game object
     * 
     * @param chessGame 
     */
    protected void setChessGame(AbstractChessGame chessGame) {
        this.chessGame = chessGame;
    }

    /**
     * 
     * @return the ChessGame object
     */
    public AbstractChessGame getChessGame() {
        return chessGame == null ? NO_CHESSGAME : chessGame;
    }

    public int getVersion() {
        return version;
    }
    
    /**
     * @return Player opposing player
     */
    public ChessGamePlayer getOpponent() {
        return getChessGame().getBlackPlayer();
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
    public abstract void move(ChessGamePlayer player, Move move) throws IllegalMoveException;
    
    /**
     * Called when a pawn needs to be promoted to another piece
     * @param piece ChessPiece to promote pawn to
     * @param location Location the location of the pawn
     * @throws IllegalMoveException when the promotion is illegal
     */
    public abstract void promotePawnTo(ChessPiece piece, Location location) throws IllegalMoveException ;
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
    
    /*
     * To be used by Classes in the same package
     * TODO remove and correct constructor
     */
    void setPlayer(ChessGamePlayer player) {
    	this.player = player;
    }
    
    /**
     * Remove Observers from receiving updates
     * Sets Status to FINISHED after Observers removed to stop
     * Observers being updated on State change
     * Tidies up references
     */
    public void destroy() {
        removeAllObservers();
        setCurrentStatus(ServerGameStatus.FINISHED);
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
        AbstractServerChessGame other = (AbstractServerChessGame) obj;
        if (id != other.id)
            return false;
        return uid == other.uid;
    }
}
