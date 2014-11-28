package org.amc.game.chess;

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
     * The Chess piece can't be lifted off the board when making a move 
     * 
     * @return Boolean false if it can be lifted over other Chess pieces on the board
     */
    boolean canSlide();
}
