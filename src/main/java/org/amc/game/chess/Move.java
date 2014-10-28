package org.amc.game.chess;

public class Move
{
	private Location start;
	private Location end;
	
	public Move(Location start,Location end){
		this.start=start;
		this.end=end;
	}

	public final Location getStart()
	{
		return start;
	}

	public final Location getEnd()
	{
		return end;
	}
	
	
}
