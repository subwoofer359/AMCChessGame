package org.amc.game.chess;

/**
 * Shared implementation between ChessPieces
 * @author Adrian Mclaughlin
 *
 */
public abstract class SimplePiece implements ChessPiece
{
	private Colour colour;
	
	public SimplePiece(Colour colour){
		this.colour=colour;
	}
	
	public final Colour getColour(){
		return colour;
	}
}
