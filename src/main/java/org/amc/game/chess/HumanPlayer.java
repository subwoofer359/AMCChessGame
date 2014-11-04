package org.amc.game.chess;

/**
 * Represents a Human player in a game of chess
 * @author adrian
 *
 */
public class HumanPlayer implements Player
{
	private String name;
	private Colour colour;
	private boolean winner=false;
	
	public HumanPlayer(String name,Colour colour){
		this.name=name;
		this.colour=colour;
	}

	@Override
	public String getName()
	{
		return this.name;
	}
	
	@Override
	public Colour getColour(){
		return this.colour;
	}
	
	@Override
	public boolean isWinner(){
	    return winner;
	}
	
	public void isWinner(boolean isWinner){
	    this.winner=isWinner;
	}

}
