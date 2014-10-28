package org.amc.game.chess;

public class KingPiece implements ChessPiece
{

	private Colour colour;
	
	public KingPiece(Colour colour){
		this.colour=colour;
	}
	
	@Override
	public boolean isValidMove(ChessBoard board, Move move)
	{
		return false;
	}

	@Override
	public Colour getColour()
	{
		return this.colour;
	}

}
