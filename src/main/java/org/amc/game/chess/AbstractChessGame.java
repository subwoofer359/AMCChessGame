package org.amc.game.chess;

import org.apache.openjpa.persistence.Externalizer;
import org.apache.openjpa.persistence.Factory;
import org.apache.openjpa.persistence.Persistent;
import org.apache.openjpa.persistence.PersistentCollection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.Version;

@MappedSuperclass
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class AbstractChessGame  implements Serializable {

    private static final long serialVersionUID = -3648559410569232983L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Persistent(cascade=CascadeType.ALL)
    @Externalizer("org.amc.dao.ChessBoardExternalizer.getChessBoardString")
    @Factory("org.amc.dao.ChessBoardExternalizer.getChessBoard")
    @Column(length=128)
    private ChessBoard board;
    
    @OneToOne(cascade=CascadeType.ALL)
    private ChessGamePlayer currentPlayer;
    
    @OneToOne(cascade=CascadeType.ALL)
    private ChessGamePlayer whitePlayer;
    
    @OneToOne(cascade=CascadeType.ALL)
    private ChessGamePlayer blackPlayer;
   
    @Transient
    private List<ChessMoveRule> chessRules;
    
    @Column(nullable=false)
    private GameState gameState;
    
    @Version
    private int version;
    
    @PersistentCollection(elementCascade=CascadeType.ALL, elementType=Move.class, fetch = FetchType.EAGER)
    @Externalizer("org.amc.dao.MoveListExternalizer.stringOfAllMoves")
    @Factory("org.amc.dao.MoveListExternalizer.listOfMovesFromString")
    @Column(length=1000)
    List<Move> allGameMoves;
    
    public enum GameState{
        NEW,
        RUNNING,
        STALEMATE,
        WHITE_IN_CHECK,
        BLACK_IN_CHECK,
        WHITE_CHECKMATE,
        BLACK_CHECKMATE,
        PAWN_PROMOTION
    }
    
    protected AbstractChessGame() {
        this.board = ChessBoard.EMPTY_CHESSBOARD;
        this.whitePlayer = null;
        this.blackPlayer = null;
        this.currentPlayer = null;
        chessRules = new ArrayList<>();
        allGameMoves = new ArrayList<>();
        this.gameState = GameState.NEW;
    }
    
    public AbstractChessGame(ChessBoard board, ChessGamePlayer playerWhite, ChessGamePlayer playerBlack) {
        this();
        this.board = new ChessBoard(board);
        this.whitePlayer = playerWhite;
        this.blackPlayer = playerBlack;
        this.currentPlayer = this.whitePlayer;
        this.gameState = GameState.RUNNING;
    }
    
    public AbstractChessGame(AbstractChessGame chessGame) {
        this.board = new ChessBoard(chessGame.getChessBoard());
        this.whitePlayer = chessGame.getWhitePlayer();
        this.blackPlayer = chessGame.getBlackPlayer();
        this.currentPlayer = chessGame.getCurrentPlayer();
        this.chessRules = new ArrayList<>(chessGame.chessRules);
        this.allGameMoves = copyOfChessMoves(chessGame);
        this.gameState = chessGame.getGameState();
    }
    
    /**
     * Unique Integer used for identification and persistence
     * @return unique Integer
     */
    public int getId() {
        return id;
    }
    
    /**
     * Returns the Player who is waiting for their turn
     * 
     * @return Player
     */
    public ChessGamePlayer getOpposingPlayer(ChessGamePlayer player) {
        return ComparePlayers.comparePlayers(player, whitePlayer)? blackPlayer : whitePlayer;
    }

    /**
     * Changes the current player
     */
    public void changePlayer() {
        if (currentPlayer.equals(whitePlayer)) {
            currentPlayer = blackPlayer;
        } else {
            currentPlayer = whitePlayer;
        }
    }

    /**
     * Move a ChessPiece from one square to another as long as the move is valid
     * 
     * @param player
     *            Player making the move
     * @param move
     *            Move
     * @throws IllegalMoveException
     *             if not a valid movement
     */
    public abstract void move(ChessGamePlayer player, Move move) throws IllegalMoveException;

    /**
     * Checks to see if the game has reached it's completion
     * 
     * @return Boolean
     */
    public boolean isGameOver() {
        return gameState == GameState.STALEMATE || gameState == GameState.BLACK_CHECKMATE
                        || gameState == GameState.WHITE_CHECKMATE;
    }
    
    /**
     * Checks to see if a game rule applies to the Player's move Only applies
     * one rule per move
     * 
     * @param game {@link ChessGame}
     * @param move {@link Move}
     * @return Boolean true if a Game rule applies to the Player's move
     */
    boolean doesAGameRuleApply(ChessGame game, Move move) {
        for (ChessMoveRule rule : chessRules) {
            if (rule.isRuleApplicable(game, move)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Return the last Move make or null if no move has yet to be made
     * 
     * @return Move
     */
    public Move getTheLastMove() {
        if (allGameMoves.isEmpty()) {
            return Move.EMPTY_MOVE;
        } else {
            return allGameMoves.get(allGameMoves.size() - 1);
        }
    }
    
    void setGameRules(List<ChessMoveRule> rules) {
        this.chessRules = rules;
    }

    /**
     * Sets the ChessBoard the chess game uses
     * @param board ChessBoard a configured chess board
     */
    public void setChessBoard(ChessBoard board) {
        this.board = board;
    }
    
    /**
     * Returns the ChessBoard
     * @return ChessBoard
     */
    public ChessBoard getChessBoard(){
        return this.board;
    }
    
    public GameState getGameState(){
        return this.gameState;
    }
    
    void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    
    void setPromotionState() {
        this.gameState = GameState.PAWN_PROMOTION;
    }
    
    void setRunningState() {
        this.gameState = GameState.RUNNING;
    }
    
    public ChessGamePlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessGamePlayer getWhitePlayer() {
        return whitePlayer;
    }

    public ChessGamePlayer getBlackPlayer() {
        return blackPlayer;
    }
 
    private List<Move> copyOfChessMoves(AbstractChessGame chessGame) {
        return new ArrayList<>(chessGame.allGameMoves);
    }
    
    List<ChessMoveRule> getChessMoveRules() {
        return Collections.unmodifiableList(this.chessRules);
    }
    
    void addChessMoveRule(ChessMoveRule rule) {
        this.chessRules.add(rule);
    }
}
