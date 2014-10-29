package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KingPieceTest
{
	private ChessBoard board;
	
	@Before
	public void setUp() throws Exception
	{
		board=new ChessBoard();
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testIsValidMoveOnEmptyBoard()
	{
		KingPiece king=new KingPiece(Colour.BLACK);
		board.putPieceOnBoardAt(king,new Location(ChessBoard.Coordinate.F, 7));
		assertTrue(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.F,8)
				)));
		assertTrue(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.F,6)
				)));
		assertTrue(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.G,7)
				)));
		assertTrue(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.E,7)
				)));
		
		assertTrue(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.E,8)
				)));
		assertTrue(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.G,8)
				)));
		assertTrue(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.G,6)
				)));
		assertTrue(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.E,8)
				)));
	}
	
	@Test
	public void testIsNotValidMoveOnEmptyBoard()
	{
		KingPiece king=new KingPiece(Colour.BLACK);
		board.putPieceOnBoardAt(king,new Location(ChessBoard.Coordinate.F, 7));
		assertFalse(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.A,8)
				)));
		assertFalse(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.B,6)
				)));
		assertFalse(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.G,3)
				)));
		assertFalse(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.E,1)
				)));
		
		assertFalse(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.D,2)
				)));
		assertFalse(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.C,3)
				)));
		assertFalse(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.G,3)
				)));
		assertFalse(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.D,8)
				)));
		
		assertFalse(king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.F,7)
				)));
		
		
	}
	
	@Test
	public void testIsValidMoveOnBoard()
	{
		KingPiece king=new KingPiece(Colour.BLACK);
		KingPiece kingWhite=new KingPiece(Colour.WHITE);
		KingPiece anotherBlackKing=new KingPiece(Colour.BLACK);
		board.putPieceOnBoardAt(king,new Location(ChessBoard.Coordinate.F, 7));
		board.putPieceOnBoardAt(kingWhite,new Location(ChessBoard.Coordinate.E, 7));
		board.putPieceOnBoardAt(anotherBlackKing,new Location(ChessBoard.Coordinate.G, 7));
		
		//Move to square occupied by the white king should be valid
		boolean isValid =king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.E,7)
				));
		//Move to square occupied by the another king should not be valid		
		boolean notValid=king.isValidMove(board, new Move(
				new Location(ChessBoard.Coordinate.F,7),
				new Location(ChessBoard.Coordinate.G,7)
				));

		assertTrue(isValid);
		assertFalse(notValid);
	}
}
