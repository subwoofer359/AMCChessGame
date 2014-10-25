package org.amc.game.chess;

/**
 * Represents a Human player in a game of chess
 * @author adrian
 *
 */
public class HumanPlayer implements Player
{
	private String name;
	
	public HumanPlayer(String name){
		this.name=name;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

}
