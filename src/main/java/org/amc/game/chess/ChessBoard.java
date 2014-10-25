package org.amc.game.chess;


/**
 * Represents a Chess Board
 * Responsibility is to know the position of all the pieces
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ChessBoard
{
	public enum Coordinate{
		A(0),
		B(1),
		C(2),
		D(3),
		E(4),
		F(5),
		G(6),
		H(7);
		
		private int name;
		
		private Coordinate(int name){
			this.name=name;
		}
		
		public int getName(){
			return this.name;
		}
		
	}
	
	private final ChessPiece[][] board=new ChessPiece[8][8];
}
