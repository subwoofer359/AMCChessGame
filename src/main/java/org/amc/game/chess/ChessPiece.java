package org.amc.game.chess;

import java.util.Set;

/**
 * Represents a Chess piece. Responsible for the piece's valid moves
 * 
 * @author Adrian McLaughlin
 *
 */
public interface ChessPiece {
    /**
     * Checks if the move is a valid chess move
     * 
     * @param board
     *            The Chessboard
     * @param move
     *            The move to be checked
     * @return true is the Piece can successful make this move
     */
    boolean isValidMove(ChessBoard board, Move move);

    /**
     * 
     * @return the colour of the ChessPiece
     */
    Colour getColour();

    /**
     * Sets the move state of the ChessPiece
     */
    void moved();

    /**
     * Checks if the ChessPiece has moved since the start of the game
     * 
     * @return true if the ChessPiece has moved
     */
    boolean hasMoved();
    
    
    /**
     * Resets the flag for the piece has moved
     */
    void resetMoved();
    /**
     * The Chess piece can't be lifted off the board when making a move 
     * 
     * @return Boolean false if it can be lifted over other Chess pieces on the board
     */
    boolean canSlide();
    
    /**
     * Find all possible move locations. If the piece isn't on the board the method will 
     * add the @param Location to the Set which isn't desirable
     * 
     * @param board ChessBoard on which the ChessPiece is on
     * @param location Location of the ChessPiece
     * @return HashSet of Locations
     */
    Set<Location> getPossibleMoveLocations(ChessBoard board,Location location);
    
    /**
     * Create a copy of the ChessPiece
     * 
     * @return ChessPiece a deep copy of the chessPiece
     */
    ChessPiece copy();
}
