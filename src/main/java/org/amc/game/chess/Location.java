package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;

public class Location
{
	private Coordinate letter;
	private Integer number;
	public Location(Coordinate letter,Integer number){
		this.letter=letter;
		this.number=number;
	}
	
	public Coordinate getLetter(){
		return this.letter;
	}
	
	public Integer getNumber(){
		return this.number;
	}
}
