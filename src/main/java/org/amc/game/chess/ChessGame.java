package org.amc.game.chess;

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
    
    public ChessGame(ChessBoard board, Player playerOne, Player playerTwo) {
        this.board = board;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.currentPlayer = this.playerOne;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
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
        } else if (player.getColour() != piece.getColour()) {
            throw new InvalidMoveException("Player can only move their own pieces");
        } else {
            if (piece.isValidMove(board, move)) {
                if(isMoveEnPassantCapture(move)){
                    Location endSquare=move.getEnd();
                    if(piece.getColour().equals(Colour.WHITE)){
                        Location capturedPawn=new Location(endSquare.getLetter(),endSquare.getNumber()-1);
                        board.removePieceOnBoardAt(null, capturedPawn);
                    }
                    else{
                        Location capturedPawn=new Location(endSquare.getLetter(),endSquare.getNumber()+1);
                        board.removePieceOnBoardAt(null, capturedPawn);
                    }    
                }
                board.move(player, move);
            } else {
                throw new InvalidMoveException("Not a valid move");
            }
        }
    }
    
    boolean isMoveEnPassantCapture(Move move){
        ChessPiece piece=board.getPieceFromBoardAt(move.getStart());
        if(piece instanceof PawnPiece){
            return ((PawnPiece)piece).isEnPassantCapture(board, move);
        }else{
            return false;
        }
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
}
