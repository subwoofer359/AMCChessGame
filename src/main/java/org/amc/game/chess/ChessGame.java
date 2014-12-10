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
    private GameState gameState;
    
    public enum GameState{
        RUNNING,
        STALEMATE,
        WHITE_IN_CHECK,
        BLACK_IN_CHECK,
        WHITE_CHECKMATE,
        BLACK_CHECKMATE,
    }
    

    public ChessGame(ChessBoard board, Player playerOne, Player playerTwo) {
        this.board = board;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.currentPlayer = this.playerOne;
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
     * Returns the Player who is waiting for their tuen
     * 
     * @return
     */
    Player getOpposingPlayer(Player player) {
        return player == playerOne ? playerTwo : playerOne;
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
    public void move(Player player, Move move) throws InvalidMoveException {
        ChessPiece piece = board.getPieceFromBoardAt(move.getStart());
        checkChessPieceExistsOnSquare(piece, move);
        checkItsthePlayersPiece(player, piece);
        moveThePlayersChessPiece(player, board, piece, move);
        if(isOpponentsKingInCheck(player,board)){
           // isOpponentKingInCheckMate(player,board);
        }
    }

    private void checkChessPieceExistsOnSquare(ChessPiece piece, Move move)
                    throws InvalidMoveException {
        if (piece == null) {
            throw new InvalidMoveException("No piece at " + move.getStart());
        }
    }

    private void checkItsthePlayersPiece(Player player, ChessPiece piece)
                    throws InvalidMoveException {
        if (notPlayersChessPiece(player, piece)) {
            throw new InvalidMoveException("Player can only move their own pieces");
        }
    }

    private boolean notPlayersChessPiece(Player player, ChessPiece piece) {
        return player.getColour() != piece.getColour();
    }

    private void moveThePlayersChessPiece(Player player, ChessBoard board, ChessPiece piece,
                    Move move) throws InvalidMoveException {
        if (isPlayersKingInCheck(player, board)) {
            doNormalMove(player, piece, move);
        } else if (doesAGameRuleApply(board, move)) {
            thenApplyGameRule(player, move);
        } else {
            doNormalMove(player, piece, move);
        }
    }

    private void doNormalMove(Player player, ChessPiece piece, Move move)
                    throws InvalidMoveException {
        if (piece.isValidMove(board, move)) {
            thenMoveChessPiece(player, move);
        } else {
            throw new InvalidMoveException("Not a valid move");
        }
    }

    private void thenMoveChessPiece(Player player, Move move) throws InvalidMoveException {
        ReversibleMove reversible = new ReversibleMove(board, move);
        reversible.move();
        if (isPlayersKingInCheck(player, board)) {
            reversible.undoMove();
            throw new InvalidMoveException("King is checked");
        }
    }

    private void thenApplyGameRule(Player player, Move move) throws InvalidMoveException {
        for (ChessMoveRule rule : chessRules) {
            rule.applyRule(board, move);
            if (isPlayersKingInCheck(player, board)) {
                rule.unapplyRule(board, move);
                throw new InvalidMoveException("King is checked");
            }
        }
    }

    /**
     * Checks to see if the game has reached it's completion
     * 
     * @param playerOne
     * @param playerTwo
     * @return Boolean
     */
    public boolean isGameOver(Player playerOne, Player playerTwo) {
        boolean playerOneLostTheirKing = isThePlayersKingNotOnTheBoard(playerOne);
        boolean playerTwoLostTheirKing = isThePlayersKingNotOnTheBoard(playerTwo);
        if (playerOneLostTheirKing || isCheckMate(playerOne, board)) {
            playerTwo.isWinner(true);
            return true;
        } else if (playerTwoLostTheirKing || isCheckMate(playerTwo, board)) {
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
    boolean isThePlayersKingNotOnTheBoard(Player player) {
        List<ChessPieceLocation> allPlayersChessPieces = board
                        .getListOfPlayersPiecesOnTheBoard(player);
        for (ChessPieceLocation pieceLocation : allPlayersChessPieces) {
            if (pieceLocation.getPiece().getClass().equals(KingPiece.class)) {
                return false;
            }
        }
        return true;

    }

    boolean isCheckMate(Player player, ChessBoard board) {
        PlayersKingCheckmateCondition checkmate=new PlayersKingCheckmateCondition(player, getOpposingPlayer(player), board);
        return checkmate.isCheckMate();
    }

    boolean isPlayersKingInCheck(Player player, ChessBoard board) {
        return kingInCheck.isPlayersKingInCheck(player, getOpposingPlayer(player), board);
        
    }
    
    boolean isOpponentsKingInCheck(Player player, ChessBoard board){
        Player opponent=getOpposingPlayer(player);
        boolean inCheck = kingInCheck.isPlayersKingInCheck(opponent,player,board);
        if(inCheck)
        {
            gameState=(opponent.getColour().equals(Colour.WHITE))?GameState.WHITE_IN_CHECK:GameState.BLACK_IN_CHECK;
        }
        return inCheck;
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

    void setChessBoard(ChessBoard board) {
        this.board = board;
    }
    
    GameState getGameState(){
        return this.gameState;
    }
}
