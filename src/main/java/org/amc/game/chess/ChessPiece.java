package org.amc.game.chess;

/**
 * Represents a Chess piece.
 * Responsible for the piece's valid moves
 * 
 * @author Adrian McLaughlin
 *
 */
public interface ChessPiece
{
	/**
	 * Checks if the move is a valid chess move
	 * 
	 * @param board The Chessboard
	 * @param move The move to be checked
	 * @return true is the Piece can successful make this move
	 */
	public boolean isValidMove(ChessBoard board,Move move);
	
	/**
	 * 
	 * @return the colour of the ChessPiece
	 */
	public Colour getColour();
}
