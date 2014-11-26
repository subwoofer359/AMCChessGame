package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chess.ChessBoard.Coordinate;

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
    List<ChessRule> chessRules;
    
    public ChessGame(ChessBoard board, Player playerOne, Player playerTwo) {
        this.board = board;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.currentPlayer = this.playerOne;
        chessRules=new ArrayList<>();
        chessRules.add(new EnPassantRule());
        chessRules.add(new CastlingRule());
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
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
        if (piece == null) {
            throw new InvalidMoveException("No piece at " + move.getStart());
        } else if (notPlayersChessPiece(player, piece)) {
            throw new InvalidMoveException("Player can only move their own pieces");
        } else if(isPlayersKingInCheck(player, board)){
            if(piece.isValidMove(board, move)){
                ReversibleMove reversible=new ReversibleMove(board, move);
                reversible.move();
                if(isPlayersKingInCheck(player, board)){
                    reversible.undoMove();
                    throw new InvalidMoveException("King is checked");
                }
            }else{
                throw new InvalidMoveException("Not a valid move");
            }
        }else if(doesAGameRuleApply(board, move)){
            //Todo add isPlayersKingInCheck check
            for(ChessRule rule:chessRules){
              rule.applyRule(board, move);
          }
        }else if(piece.isValidMove(board, move)){
                ReversibleMove reversible=new ReversibleMove(board, move);
                reversible.move();
                if(isPlayersKingInCheck(player, board)){
                    reversible.undoMove();
                    throw new InvalidMoveException("King is checked");
                }
        }else{
            throw new InvalidMoveException("Not a valid move");
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
    boolean isGameOver(Player playerOne, Player playerTwo) {
        boolean playerOneHaveTheirKing = doesThePlayerStillHaveTheirKing(playerOne);
        boolean playerTwoHaveTheirKing = doesThePlayerStillHaveTheirKing(playerTwo);
        if (!playerOneHaveTheirKing) {
            playerTwo.isWinner(true);
            return true;
        } else if (!playerTwoHaveTheirKing) {
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
        List<ChessPiece> allPlayersChessPieces = getAllPlayersChessPiecesOnTheBoard(player);
        for (ChessPiece piece : allPlayersChessPieces) {
            if (piece.getClass().equals(KingPiece.class)) {
                return true;
            }
        }
        return false;

    }

    /**
     * creates a List of all the Player's pieces still on the board
     * 
     * @param player
     * @return List of ChessPieces
     */
    List<ChessPiece> getAllPlayersChessPiecesOnTheBoard(Player player) {
        List<ChessPiece> pieceList = new ArrayList<ChessPiece>();
        for (Coordinate letter : Coordinate.values()) {
            for (int i = 1; i <= 8; i++) {
                ChessPiece piece = board.getPieceFromBoardAt(letter.getName(), i);
                if (piece == null) {
                    continue;
                } else {
                    if (piece.getColour().equals(player.getColour())) {
                        pieceList.add(piece);
                    }
                }
            }
        }
        return pieceList;
    }
    
    /**
     * Checks to see if the opponent's ChessPieces are attacking the Player's king
     * @param player Player who King might be under attack
     * @param board ChessBoard current ChessBoard
     * @return Boolean true if the opponent can capture the Player's king on the next turn
     */
    boolean isPlayersKingInCheck(Player player,ChessBoard board){
        Location playersKingLocation=board.getPlayersKingLocation(player);
        List<ChessPieceLocation> listOfEnemysPieces=board.getListOfPlayersPiecesOnTheBoard(player==playerOne?playerTwo:playerOne);
        for(ChessPieceLocation pieceLocation:listOfEnemysPieces){
            Move move=new Move(pieceLocation.getLocation(),playersKingLocation);
            if(pieceLocation.getPiece().isValidMove(board, move)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks to see if a game rule applies to the Player's move
     * Only applies one rule per move
     * @param board
     * @param move
     * @return Boolean true if a Game rule applies to the Player's move
     */
    boolean doesAGameRuleApply(ChessBoard board, Move move){
        for(ChessRule rule:chessRules){
            if(rule.isRuleApplicable(board, move)){
                return true;
            }
        }
        return false;
    }
    
    void setGameRules(List<ChessRule> rules){
        this.chessRules=rules;
    }
    
    void setChessBoard(ChessBoard board){
        this.board=board;
    }
}
