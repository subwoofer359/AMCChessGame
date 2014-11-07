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
	
	public Integer getDistanceX(){
		return start.getLetter().getName()-end.getLetter().getName();
	}
	
	public Integer getDistanceY(){
		return start.getNumber()-end.getNumber();
	}
	
	public Integer getAbsoluteDistanceX(){
		return Math.abs(getDistanceX());
	}
	
	public Integer getAbsoluteDistanceY(){
		return Math.abs(getDistanceY());
	}
	
	@Override
	public String toString(){
	    StringBuilder sb=new StringBuilder();
	    sb.append('[');
	    sb.append(this.start);
	    sb.append("-->");
	    sb.append(this.end);
	    sb.append(']');
	    
	    
	    return sb.toString();
	}
	
}
