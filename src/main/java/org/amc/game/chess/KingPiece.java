package org.amc.game.chess;

public class KingPiece extends SimplePiece
{

	public KingPiece(Colour colour){
		super(colour);
	}
	
	@Override
	public boolean isValidMove(ChessBoard board, Move move)
	{
		return false;
	}
}
