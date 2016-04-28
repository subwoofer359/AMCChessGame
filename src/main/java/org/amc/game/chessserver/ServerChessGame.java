package org.amc.game.chessserver;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.PawnPromotionRule;
import org.amc.game.chess.Player;
import org.amc.game.chess.AbstractChessGame.GameState;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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
    @NamedQuery(name="getChessGamesByPlayer", query="SELECT x FROM ServerChessGame x WHERE x.chessGame.whitePlayer.player.id = (SELECT p.id FROM HumanPlayer p where p.userName =?1) "
                    + "OR x.chessGame.blackPlayer.player.id = (SELECT p.id FROM HumanPlayer p where p.userName =?1)")
})

    

public abstract class ServerChessGame extends AbstractServerChessGame {
    
    private static final long serialVersionUID = 2147129152958398504L;

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
        super(uid, player);
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
    public abstract void addOpponent(Player opponent);


    
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
    @Override
    public void move(ChessGamePlayer player, Move move) throws IllegalMoveException {
        checkForNull(Player.class, player);
        checkForNull(Move.class, move);
        if (getChessGame() != null) {
            synchronized (getChessGame()) {
                getChessGame().move(player, move);
                changePlayerIfNoPawnPromotion();
            }
            notifyObservers(getChessGame());
            checkGameStatus();
        }
    }
    
    private void changePlayerIfNoPawnPromotion() {
        if(getChessGame().getGameState() != GameState.PAWN_PROMOTION) {
            getChessGame().changePlayer();
        }
    }
    
    boolean isGameAwaitingPlayer() {
        return ServerGameStatus.AWAITING_PLAYER.equals(getCurrentStatus());
    }
    
    /**
     * Checks to see if the game is finished and sets it's status accordingly
     * No check for chessGame being null
     */
    private void checkGameStatus() {
        switch (getChessGame().getGameState()) {
        case RUNNING:
            break;

        case WHITE_CHECKMATE:
        case BLACK_CHECKMATE:
        case STALEMATE:
            setCurrentStatus(ServerGameStatus.FINISHED);
            break;

        case BLACK_IN_CHECK:
        case WHITE_IN_CHECK:
        case PAWN_PROMOTION:
            notifyObservers(getChessGame().getGameState());
            break;

        default:
            break;
        }
    }

    @Override
    public void promotePawnTo(ChessPiece piece, Location location) throws IllegalMoveException {
        PawnPromotionRule promotionRule = PawnPromotionRule.getInstance();
        promotionRule.promotePawnTo(getChessGame(), location, piece);
        notifyObservers(getChessGame());
        checkGameStatus();
    }
    
    
}
