package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;
/**
 * Represents a Bishop in the game of chess
 * @author Adrian Mclaughlin
 *
 */
public class BishopPiece implements ChessPiece
{
	private Colour colour;
	
	public BishopPiece(Colour colour){
		this.colour=colour;
	}
	
	@Override
	public boolean isValidMove(ChessBoard board,Coordinate letterCoordinateOne,int numberCoordinateOne,
			Coordinate letterCoordinateTwo,int numberCoordinateTwo)
	{
		return false;
	}
	
	public Colour getColour(){
		return this.colour;
	}
}
