package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class BishopPieceTest
{

	private ChessBoard board;
	
	@Before
	public void setUp() throws Exception
	{
		board=new ChessBoard();
		board.initialise();
	}

	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * Simple valid move on an empty chess board
	 */
	@Test
	public void testOnEmptyBoardIsValidMove()
	{
		BishopPiece bishop = new BishopPiece(Colour.BLACK);
		board.putPieceOnBoardAt(bishop, ChessBoard.Coordinate.F, 8);
		
		boolean isValid = bishop.isValidMove(this.board,ChessBoard.Coordinate.F, 8,ChessBoard.Coordinate.G,7);
		isValid =isValid & bishop.isValidMove(this.board,ChessBoard.Coordinate.F, 8,ChessBoard.Coordinate.D,6);
		isValid =isValid & bishop.isValidMove(this.board,ChessBoard.Coordinate.F, 8,ChessBoard.Coordinate.A,3);
		isValid =isValid & bishop.isValidMove(this.board,ChessBoard.Coordinate.H, 1,ChessBoard.Coordinate.A,8);
		
		assertTrue(isValid);
	}
	
	/**
	 * Simple invalid move on an empty chess board
	 */
	@Test
	public void testOnEmptyBoardIsNotValidMove()
	{
		BishopPiece bishop = new BishopPiece(Colour.BLACK);
		board.putPieceOnBoardAt(bishop, ChessBoard.Coordinate.F, 8);
		
		boolean isValid = bishop.isValidMove(this.board,ChessBoard.Coordinate.D, 4,ChessBoard.Coordinate.G,7);
		isValid =isValid & bishop.isValidMove(this.board,ChessBoard.Coordinate.D, 4,ChessBoard.Coordinate.D,6);
		isValid =isValid & bishop.isValidMove(this.board,ChessBoard.Coordinate.D, 4,ChessBoard.Coordinate.A,3);
		isValid =isValid & bishop.isValidMove(this.board,ChessBoard.Coordinate.D, 4,ChessBoard.Coordinate.A,8);
		//A non move
		isValid =isValid & bishop.isValidMove(this.board,ChessBoard.Coordinate.D, 4,ChessBoard.Coordinate.D,4);
		assertFalse(isValid);
	}
	
	/**
	 * A move to capture an enemy piece
	 */
	@Test
	public void testOnBoardIsValidCapture(){
		BishopPiece bishop = new BishopPiece(Colour.BLACK);
		BishopPiece bishopWhite = new BishopPiece(Colour.WHITE);
		board.putPieceOnBoardAt(bishop, ChessBoard.Coordinate.F, 8);
		board.putPieceOnBoardAt(bishopWhite, ChessBoard.Coordinate.D, 6);
		
		boolean isValid=bishop.isValidMove(this.board,ChessBoard.Coordinate.F,8,ChessBoard.Coordinate.D,6);
		
		assertTrue(isValid);
	}
	
	/**
	 * A move to a square occupied by a player's own piece
	 */
	@Test
	public void testOnBoardInvalidCapture(){
		BishopPiece bishop = new BishopPiece(Colour.BLACK);
		BishopPiece bishopWhite = new BishopPiece(Colour.BLACK);
		board.putPieceOnBoardAt(bishop, ChessBoard.Coordinate.F, 8);
		board.putPieceOnBoardAt(bishopWhite, ChessBoard.Coordinate.D, 6);
		
		boolean isValid=bishop.isValidMove(this.board,ChessBoard.Coordinate.F,8,ChessBoard.Coordinate.D,6);
		
		assertFalse(isValid);
	}
	
	/**
	 * A move through an enemy piece
	 */
	@Test
	public void testOnBoardIsNotValidMove(){
		BishopPiece bishop = new BishopPiece(Colour.BLACK);
		BishopPiece bishopWhite = new BishopPiece(Colour.WHITE);
		board.putPieceOnBoardAt(bishop, ChessBoard.Coordinate.F, 8);
		board.putPieceOnBoardAt(bishopWhite, ChessBoard.Coordinate.D, 6);
		
		boolean isValid=bishop.isValidMove(this.board,ChessBoard.Coordinate.F,8,ChessBoard.Coordinate.C,5);
		
		assertFalse(isValid);
	}
	
	

}
