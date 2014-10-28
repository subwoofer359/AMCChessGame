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
	public enum Coordinate implements Comparable<Coordinate>{
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
	
	private final ChessPiece[][] board;
	
	public ChessBoard(){
		board=new ChessPiece[8][8];
	}
	
	/**
	 * Sets up the board in it's initial state
	 */
	public void initialise(){
		
	}
	
	public void move(Move move){
		
	}
	
	void putPieceOnBoardAt(ChessPiece piece,Location location){
		this.board[location.getLetter().getName()][location.getNumber()-1]=piece;
	}
	
	ChessPiece getPieceFromBoardAt(int letterCoordinate,int numberCoordinate){
		return board[letterCoordinate][numberCoordinate-1];
	}
}
