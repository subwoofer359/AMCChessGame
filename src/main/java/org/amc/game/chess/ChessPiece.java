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
	 * 
	 * @param letterCoordinate Letter A through H
	 * @param numberCoordinate Number 1-8
	 * @return true is the Piece can successful make this move
	 */
	public boolean isValidMove(ChessBoard.Coordinate letterCoordinate,int numberCoordinate);
}
