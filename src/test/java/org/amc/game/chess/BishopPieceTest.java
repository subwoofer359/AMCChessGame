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
	 * Simple move on an empty chess board
	 */
	@Test
	public void testIsValidMove()
	{
		BishopPiece bishop = new BishopPiece(Colour.BLACK);
		
		boolean isValid = bishop.isValidMove(this.board,ChessBoard.Coordinate.F, 8,ChessBoard.Coordinate.G,7);
		
		assertTrue(isValid);
		
	}

}
