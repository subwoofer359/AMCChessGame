package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the Rules of Chess
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ChessGame {
    private ChessBoard board;
    private Player currentPlayer;
    private Player playerOne;
    private Player playerTwo;
    List<ChessMoveRule> chessRules;
    private PlayerKingInCheckCondition kingInCheck;
    private PlayersKingCheckmated checkmate;
    
    public ChessGame(ChessBoard board, Player playerOne, Player playerTwo) {
        this.board = board;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.currentPlayer = this.playerOne;
        this.kingInCheck=new PlayerKingInCheckCondition();
        this.checkmate=new PlayersKingCheckmated();
        chessRules=new ArrayList<>();
        chessRules.add(new EnPassantRule());
        chessRules.add(new CastlingRule());
        chessRules.add(new PawnPromotionRule());
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Returns the Player who is waiting for their tuen
     * @return
     */
    Player getOpposingPlayer(Player player){
       return player==playerOne?playerTwo:playerOne;
    }
    
    /**
     * Changes the current player
     */
    public void changePlayer() {
        if (currentPlayer.equals(playerOne)) {
            currentPlayer = playerTwo;
        } else {
            currentPlayer = playerOne;
        }
    }
    
    /**
     * Move a ChessPiece from one square to another as long as the move is valid
     * 
     * @param player
     *            Player making the move
     * @param move
     *            Move
     * @throws InvalidMoveException
     *             if not a valid movement
     */
    public void move(Player player, Move move)throws InvalidMoveException{
        ChessPiece piece = board.getPieceFromBoardAt(move.getStart());
        checkChessPieceExistOnSquare(piece, move);
        checkItsthePlayersPiece(player, piece);
        moveThePlayersChessPiece(player, board, piece, move);
    }
    
    private void checkChessPieceExistOnSquare(ChessPiece piece, Move move)throws InvalidMoveException{
        if (piece == null) {
            throw new InvalidMoveException("No piece at " + move.getStart());
        }
    }
    
    private void checkItsthePlayersPiece(Player player,ChessPiece piece)throws InvalidMoveException{
        if (notPlayersChessPiece(player, piece)) {
            throw new InvalidMoveException("Player can only move their own pieces");
        } 
    }
   
    private void moveThePlayersChessPiece(Player player,ChessBoard board,ChessPiece piece,Move move) throws InvalidMoveException{
        if(isPlayersKingInCheck(player, board)){
            if(piece.isValidMove(board, move)){
                thenMoveChessPiece(player,move);
            }else{
                throw new InvalidMoveException("Not a valid move");
            }
        }else if(doesAGameRuleApply(board, move)){
            thenApplyGameRule(player, move);
        }else if(piece.isValidMove(board, move)){
                thenMoveChessPiece(player,move);
        }else{
            throw new InvalidMoveException("Not a valid move");
        }
    }
    
    private void thenApplyGameRule(Player player,Move move) throws InvalidMoveException{
        for(ChessMoveRule rule:chessRules){
            rule.applyRule(board, move);
            if(isPlayersKingInCheck(player, board)){
                rule.unapplyRule(board, move);
                throw new InvalidMoveException("King is checked");
            }
        }
    }

    private void thenMoveChessPiece(Player player,Move move) throws InvalidMoveException{
        ReversibleMove reversible=new ReversibleMove(board, move);
        reversible.move();
        if(isPlayersKingInCheck(player, board)){
            reversible.undoMove();
            throw new InvalidMoveException("King is checked");
        }
    }
    
    /**
     * Return true if the Player owns the chess piece
     * @param player
     * @param piece
     * @return boolean
     */
    private boolean notPlayersChessPiece(Player player,ChessPiece piece){
        return player.getColour() != piece.getColour();
    }
    
    /**
     * Checks to see if the game has reached it's completion
     * 
     * @param playerOne
     * @param playerTwo
     * @return Boolean
     */
    public boolean isGameOver(Player playerOne, Player playerTwo) {
        boolean playerOneHaveTheirKing = doesThePlayerStillHaveTheirKing(playerOne);
        boolean playerTwoHaveTheirKing = doesThePlayerStillHaveTheirKing(playerTwo);
        if (!playerOneHaveTheirKing || isCheckMate(playerOne, board)) {
            playerTwo.isWinner(true);
            return true;
        } else if (!playerTwoHaveTheirKing || isCheckMate(playerTwo,board)) {
            playerOne.isWinner(true);
            return true;
        } else {
            return false;
        }
    }
    


    /**
     * Checks to see if the Player still possesses their King
     * 
     * @param player
     * @return false if they lost their King ChessPiece
     */
    boolean doesThePlayerStillHaveTheirKing(Player player) {
        List<ChessPieceLocation> allPlayersChessPieces = board.getListOfPlayersPiecesOnTheBoard(player);
        for (ChessPieceLocation pieceLocation : allPlayersChessPieces) {
            if (pieceLocation.getPiece().getClass().equals(KingPiece.class)) {
                return true;
            }
        }
        return false;

    }

    boolean isCheckMate(Player player,ChessBoard board){
        return checkmate.isCheckMate(player, getOpposingPlayer(player),board);
    }
    
    boolean isPlayersKingInCheck(Player player,ChessBoard board){
        return kingInCheck.isPlayersKingInCheck(player, getOpposingPlayer(player), board);
    }
    
    /**
     * Checks to see if a game rule applies to the Player's move
     * Only applies one rule per move
     * @param board
     * @param move
     * @return Boolean true if a Game rule applies to the Player's move
     */
    boolean doesAGameRuleApply(ChessBoard board, Move move){
        for(ChessMoveRule rule:chessRules){
            if(rule.isRuleApplicable(board, move)){
                return true;
            }
        }
        return false;
    }
    
    void setGameRules(List<ChessMoveRule> rules){
        this.chessRules=rules;
    }
    
    void setChessBoard(ChessBoard board){
        this.board=board;
    }
}
