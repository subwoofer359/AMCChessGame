package org.amc.game.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the Rules of Chess
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ChessGame{
    private ChessBoard board;
    private Player currentPlayer;
    private Player whitePlayer;
    private Player blackPlayer;
    List<ChessMoveRule> chessRules;
    private PlayerKingInCheckCondition kingInCheck;
    private GameState gameState;
    
    public enum GameState{
        RUNNING,
        STALEMATE,
        WHITE_IN_CHECK,
        BLACK_IN_CHECK,
        WHITE_CHECKMATE,
        BLACK_CHECKMATE,
    }
    

    public ChessGame(ChessBoard board, Player playerWhite, Player playerBlack) {
        this.board = board;
        this.whitePlayer = playerWhite;
        this.blackPlayer = playerBlack;
        this.currentPlayer = this.whitePlayer;
        this.kingInCheck = new PlayerKingInCheckCondition();
        chessRules = new ArrayList<>();
        chessRules.add(new EnPassantRule());
        chessRules.add(new CastlingRule());
        chessRules.add(new PawnPromotionRule());
        this.gameState=GameState.RUNNING;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns the Player who is waiting for their turn
     * 
     * @return Player
     */
    Player getOpposingPlayer(Player player) {
        return player == whitePlayer ? blackPlayer : whitePlayer;
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
    public void move(Player player, Move move) throws IllegalMoveException {
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
        if(!getCurrentPlayer().equals(player)){
            throw new IllegalMoveException("Not Player's turn");
        }
    }
    
    
    private void checkChessPieceExistsOnSquare(ChessPiece piece, Move move)
                    throws IllegalMoveException {
        if (piece == null) {
            throw new IllegalMoveException("No piece at " + move.getStart());
        }
    }

    private void checkItsthePlayersPiece(Player player, ChessPiece piece)
                    throws IllegalMoveException {
        if (notPlayersChessPiece(player, piece)) {
            throw new IllegalMoveException("Player can only move their own pieces");
        }
    }

    private boolean notPlayersChessPiece(Player player, ChessPiece piece) {
        return player.getColour() != piece.getColour();
    }

    private void moveThePlayersChessPiece(Player player, ChessBoard board, ChessPiece piece,
                    Move move) throws IllegalMoveException {
        if (isPlayersKingInCheck(player, board)) {
            doNormalMove(player, piece, move);
        } else if (doesAGameRuleApply(board, move)) {
            thenApplyGameRule(player, move);
        } else {
            doNormalMove(player, piece, move);
        }
    }

    private void doNormalMove(Player player, ChessPiece piece, Move move)
                    throws IllegalMoveException {
        if (piece.isValidMove(board, move)) {
            thenMoveChessPiece(player, move);
        } else {
            throw new IllegalMoveException("Not a valid move");
        }
    }

    private void thenMoveChessPiece(Player player, Move move) throws IllegalMoveException {
        ChessBoard testBoard=new ChessBoard(board);
        testBoard.move(move);
        if (isPlayersKingInCheck(player,testBoard)) {
            throw new IllegalMoveException("King is checked");
        }else{
            gameState=GameState.RUNNING;
            board.move(move);
        }
    }

    private void thenApplyGameRule(Player player, Move move) throws IllegalMoveException {
        for (ChessMoveRule rule : chessRules) {
            ChessBoard testBoard=new ChessBoard(board);
            rule.applyRule(testBoard, move);
            if (isPlayersKingInCheck(player, testBoard)) {
                throw new IllegalMoveException("King is checked");
            }else{
                rule.applyRule(board, move);  
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

    boolean isCheckMate(Player player, ChessBoard board) {
        PlayersKingCheckmateCondition checkmate=new PlayersKingCheckmateCondition(player, getOpposingPlayer(player), board);
        return checkmate.isCheckMate();
    }

    boolean isPlayersKingInCheck(Player player, ChessBoard board) {
        return kingInCheck.isPlayersKingInCheck(player, getOpposingPlayer(player), board);
        
    }
    
    boolean isOpponentsKingInCheck(Player player, ChessBoard board) {
        Player opponent = getOpposingPlayer(player);
        boolean inCheck = kingInCheck.isPlayersKingInCheck(opponent, player, board);
        if (inCheck) {
            gameState = opponent.getColour().equals(Colour.WHITE) ? GameState.WHITE_IN_CHECK
                            : GameState.BLACK_IN_CHECK;
        }
        return inCheck;
    }
    
    boolean isOpponentKingInCheckMate(Player player) {
        Player opponent = getOpposingPlayer(player);
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
    boolean doesAGameRuleApply(ChessBoard board, Move move) {
        for (ChessMoveRule rule : chessRules) {
            if (rule.isRuleApplicable(board, move)) {
                return true;
            }
        }
        return false;
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
}
