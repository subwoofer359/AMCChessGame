package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;

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
	 * @param letterCoordinateOne source coordinate Letter A through H
	 * @param numberCoordinateOne source coordinate Number 1-8
	 * @param letterCoordinateTwo destination coordinate Letter A through H
	 * @param numberCoordinateTwo destination coordinate Number 1-8
	 * @return true is the Piece can successful make this move
	 */
	public boolean isValidMove(ChessBoard board,Coordinate letterCoordinateOne,int numberCoordinateOne,
			Coordinate letterCoordinateTwo,int numberCoordinateTwo);
}
