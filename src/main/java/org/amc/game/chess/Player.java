package org.amc.game.chess;

public interface Player
{
	public String getName();
	public Colour getColour();
	public boolean isWinner();
	public void isWinner(boolean isWinner);
}
