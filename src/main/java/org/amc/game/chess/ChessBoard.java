package org.amc.game.chess;

import org.amc.util.DefaultSubject;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a Chess Board
 * Responsibility is to know the position of all the pieces
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ChessBoard extends DefaultSubject
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
		putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location(Coordinate.C,1));
		putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location(Coordinate.F,1));
		putPieceOnBoardAt(new KingPiece(Colour.WHITE), new Location(Coordinate.E, 1));
		
		putPieceOnBoardAt(new BishopPiece(Colour.BLACK), new Location(Coordinate.C,8));
		putPieceOnBoardAt(new BishopPiece(Colour.BLACK), new Location(Coordinate.F,8));
		putPieceOnBoardAt(new KingPiece(Colour.BLACK), new Location(Coordinate.E, 8));
	}
	
	public void move(Player player,Move move)throws InvalidMoveException{
		ChessPiece piece=getPieceFromBoardAt(move.getStart());
		if(piece==null){
			throw new InvalidMoveException("No piece at "+move.getStart());
		}else if (player.getColour()!=piece.getColour()){
			throw new InvalidMoveException("Player can only move their own pieces");
		}else{
			if(piece.isValidMove(this, move)){
			    removePieceOnBoardAt(piece, move.getStart());
				putPieceOnBoardAt(piece, move.getEnd());
				this.notifyObservers(null);
			}
			else
			{
			    throw new InvalidMoveException("Not a valid move");
			}
		}
	}
	
	void removePieceOnBoardAt(ChessPiece piece,Location location){
        this.board[location.getLetter().getName()][location.getNumber()-1]=null;
    }
	
	void putPieceOnBoardAt(ChessPiece piece,Location location){
		this.board[location.getLetter().getName()][location.getNumber()-1]=piece;
	}
	
	ChessPiece getPieceFromBoardAt(int letterCoordinate,int numberCoordinate){
		return board[letterCoordinate][numberCoordinate-1];
	}
	
	ChessPiece getPieceFromBoardAt(Location location){
		return getPieceFromBoardAt(location.getLetter().getName(), location.getNumber());
	}
}
