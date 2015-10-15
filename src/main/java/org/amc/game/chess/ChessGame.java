package org.amc.game.chess;

import org.apache.openjpa.persistence.Externalizer;
import org.apache.openjpa.persistence.Factory;
import org.apache.openjpa.persistence.Persistent;
import org.apache.openjpa.persistence.PersistentCollection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * Contains the Rules of Chess
 * 
 * @author Adrian Mclaughlin
 *
 */
@Entity
@Table(name="chessGames")
public class ChessGame implements Serializable{
    private static final long serialVersionUID = 5323277982974698086L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Persistent
    @Externalizer("org.amc.dao.ChessBoardExternalizer.getChessBoardString")
    @Factory("org.amc.dao.ChessBoardExternalizer.getChessBoard")
    @Column(length=96)
    private ChessBoard board;
    
    @OneToOne(cascade=CascadeType.ALL)
    private ChessGamePlayer currentPlayer;
    
    @OneToOne(cascade=CascadeType.ALL)
    private ChessGamePlayer whitePlayer;
    
    @OneToOne(cascade=CascadeType.ALL)
    private ChessGamePlayer blackPlayer;
   
    @Transient
    List<ChessMoveRule> chessRules;
    
    @Transient
    private final PlayerKingInCheckCondition kingInCheck;
    
    @Column(nullable=false)
    private GameState gameState;
    
    @PersistentCollection(elementType=Move.class)
    @Externalizer("org.amc.dao.MoveListExternalizer.stringOfAllMoves")
    @Factory("org.amc.dao.MoveListExternalizer.listOfMovesFromString")
    @Column(length=1000)
    List<Move> allGameMoves;
    
    public enum GameState{
        RUNNING,
        STALEMATE,
        WHITE_IN_CHECK,
        BLACK_IN_CHECK,
        WHITE_CHECKMATE,
        BLACK_CHECKMATE
    }
    

    public ChessGame() {
        this.board = null;
        this.whitePlayer = null;
        this.blackPlayer = null;
        this.currentPlayer = this.whitePlayer;
        this.kingInCheck = new PlayerKingInCheckCondition();
        chessRules = new ArrayList<>();
        chessRules.add(new EnPassantRule());
        chessRules.add(new CastlingRule());
        chessRules.add(new PawnPromotionRule());
        allGameMoves = new ArrayList<>();
    }
    
    public ChessGame(ChessBoard board, ChessGamePlayer playerWhite, ChessGamePlayer playerBlack) {
        this();
        this.board = board;
        this.whitePlayer = playerWhite;
        this.blackPlayer = playerBlack;
        this.currentPlayer = this.whitePlayer;
        this.gameState=GameState.RUNNING;
    }
    
    public ChessGame(ChessGame chessGame) {
        this.board = new ChessBoard(chessGame.getChessBoard());
        this.whitePlayer = chessGame.getWhitePlayer();
        this.blackPlayer = chessGame.getBlackPlayer();
        this.currentPlayer = chessGame.getCurrentPlayer();
        this.chessRules = chessGame.chessRules;
        this.allGameMoves = copyOfChessMoves(chessGame);
        this.kingInCheck = new PlayerKingInCheckCondition();
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
        //return player.equals(whitePlayer) ? blackPlayer : whitePlayer;
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
    public void move(ChessGamePlayer player, Move move) throws IllegalMoveException {
        isPlayersTurn(player);
        ChessPiece piece = board.getPieceFromBoardAt(move.getStart());
        checkChessPieceExistsOnSquare(piece, move);
        checkItsthePlayersPiece(player, piece);
        moveThePlayersChessPiece(player, board, piece, move);
        if(isOpponentsKingInCheck(player,board)){
            isOpponentKingInCheckMate(player);
        }else{
            PlayerInStalement stalemate=new PlayerInStalement(getOpposingPlayer(player),player, board);
            if(stalemate.isStalemate()){
                gameState=GameState.STALEMATE;
            }
        }
    }

    private void isPlayersTurn(Player player) throws IllegalMoveException{
        if(!ComparePlayers.comparePlayers(getCurrentPlayer(),player)) {
            throw new IllegalMoveException("Not Player's turn");
        }
    }
    
    
    private void checkChessPieceExistsOnSquare(ChessPiece piece, Move move)
                    throws IllegalMoveException {
        if (piece == null) {
            throw new IllegalMoveException("No piece at " + move.getStart());
        }
    }

    private void checkItsthePlayersPiece(ChessGamePlayer player, ChessPiece piece)
                    throws IllegalMoveException {
        if (notPlayersChessPiece(player, piece)) {
            throw new IllegalMoveException("Player can only move their own pieces");
        }
    }

    private boolean notPlayersChessPiece(ChessGamePlayer player, ChessPiece piece) {
        return player.getColour() != piece.getColour();
    }

    private void moveThePlayersChessPiece(ChessGamePlayer player, ChessBoard board, ChessPiece piece,
                    Move move) throws IllegalMoveException {
        if (isPlayersKingInCheck(player, board)) {
            doNormalMove(player, piece, move);
        } else if (doesAGameRuleApply(this, move)) {
            thenApplyGameRule(player, move);
        } else {
            doNormalMove(player, piece, move);
        }
    }

    private void doNormalMove(ChessGamePlayer player, ChessPiece piece, Move move)
                    throws IllegalMoveException {
        if (piece.isValidMove(board, move)) {
            thenMoveChessPiece(player, move);
        } else {
            throw new IllegalMoveException("Not a valid move");
        }
    }

    private void thenMoveChessPiece(ChessGamePlayer player, Move move) throws IllegalMoveException {
        ChessBoard testBoard=new ChessBoard(board);
        testBoard.move(move);
        if (isPlayersKingInCheck(player,testBoard)) {
            throw new IllegalMoveException("King is checked");
        }else{
            gameState=GameState.RUNNING;
            board.move(move);
            this.allGameMoves.add(move);
        }
    }

    private void thenApplyGameRule(ChessGamePlayer player, Move move) throws IllegalMoveException {
        for (ChessMoveRule rule : chessRules) {
            ChessGame testGame = new ChessGame(this);
            rule.applyRule(testGame, move);
            if (isPlayersKingInCheck(player, testGame.getChessBoard())) {
                throw new IllegalMoveException("King is checked");
            }else{
                rule.applyRule(this, move);  
            }
        }
    }

    /**
     * Checks to see if the game has reached it's completion
     * 
     * @return Boolean
     */
    public boolean isGameOver() {
        return gameState == GameState.STALEMATE || gameState == GameState.BLACK_CHECKMATE
                        || gameState == GameState.WHITE_CHECKMATE;
    }

    boolean isCheckMate(ChessGamePlayer player, ChessBoard board) {
        PlayersKingCheckmateCondition checkmate=new PlayersKingCheckmateCondition(player, getOpposingPlayer(player), board);
        return checkmate.isCheckMate();
    }

    boolean isPlayersKingInCheck(ChessGamePlayer player, ChessBoard board) {
        return kingInCheck.isPlayersKingInCheck(player, getOpposingPlayer(player), board);
        
    }
    
    boolean isOpponentsKingInCheck(ChessGamePlayer player, ChessBoard board) {
        ChessGamePlayer opponent = getOpposingPlayer(player);
        boolean inCheck = kingInCheck.isPlayersKingInCheck(opponent, player, board);
        if (inCheck) {
            gameState = opponent.getColour().equals(Colour.WHITE) ? GameState.WHITE_IN_CHECK
                            : GameState.BLACK_IN_CHECK;
        }
        return inCheck;
    }
    
    boolean isOpponentKingInCheckMate(ChessGamePlayer player) {
        ChessGamePlayer opponent = getOpposingPlayer(player);
        PlayersKingCheckmateCondition okcc = new PlayersKingCheckmateCondition(opponent, player,
                        board);
        if (okcc.isCheckMate()) {
            gameState = opponent.getColour().equals(Colour.WHITE) ? GameState.WHITE_CHECKMATE
                            : GameState.BLACK_CHECKMATE;
            return true;
        } else {
            return false;
        }
    }
    

    /**
     * Checks to see if a game rule applies to the Player's move Only applies
     * one rule per move
     * 
     * @param board
     * @param move
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
    
    public final ChessGamePlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public final ChessGamePlayer getWhitePlayer() {
        return whitePlayer;
    }

    public final ChessGamePlayer getBlackPlayer() {
        return blackPlayer;
    }
 
    private List<Move> copyOfChessMoves(ChessGame chessGame) {
        return new ArrayList<Move>(chessGame.allGameMoves);
    }
}
