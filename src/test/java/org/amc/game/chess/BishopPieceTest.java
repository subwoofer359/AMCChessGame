package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class BishopPieceTest implements ChessPieceTest
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

	/* (non-Javadoc)
     * @see org.amc.game.chess.ChessPieceTest#testOnEmptyBoardIsValidMove()
     */
	@Override
    @Test
	public void testOnEmptyBoardIsValidMove()
	{
		BishopPiece bishop = new BishopPiece(Colour.BLACK);
		board.putPieceOnBoardAt(bishop,new Location(ChessBoard.Coordinate.F, 8));
		
		boolean isValid = bishop.isValidMove(this.board,new Move(
						new Location(ChessBoard.Coordinate.F, 8),
						new Location(ChessBoard.Coordinate.G,7)));
		isValid =isValid & bishop.isValidMove(this.board,new Move(
				new Location(ChessBoard.Coordinate.F, 8),
				new Location(ChessBoard.Coordinate.D,6)));
		isValid =isValid & bishop.isValidMove(this.board,new Move(
				new Location(ChessBoard.Coordinate.F,8), 
				new Location(ChessBoard.Coordinate.A,3)));
		isValid =isValid & bishop.isValidMove(this.board,new Move(
				new Location(ChessBoard.Coordinate.H, 1),
				new Location(ChessBoard.Coordinate.A,8)));
		
		assertTrue(isValid);
	}
	
	/* (non-Javadoc)
     * @see org.amc.game.chess.ChessPieceTest#testOnEmptyBoardIsNotValidMove()
     */
	@Override
    @Test
	public void testOnEmptyBoardIsNotValidMove()
	{
		BishopPiece bishop = new BishopPiece(Colour.BLACK);
		board.putPieceOnBoardAt(bishop, new Location(ChessBoard.Coordinate.F, 8));
		
		boolean isValid = bishop.isValidMove(this.board,new Move(
				new Location(ChessBoard.Coordinate.D, 4),
				new Location(ChessBoard.Coordinate.G,7)));
		isValid =isValid & bishop.isValidMove(this.board,new Move(
				new Location(ChessBoard.Coordinate.D, 4),
				new Location(ChessBoard.Coordinate.D,6)));
		isValid =isValid & bishop.isValidMove(this.board,new Move(
				new Location(ChessBoard.Coordinate.D, 4),
				new Location(ChessBoard.Coordinate.A,3)));
		isValid =isValid & bishop.isValidMove(this.board,new Move(
				new Location(ChessBoard.Coordinate.D, 4),
				new Location(ChessBoard.Coordinate.A,8)));
		//A non move
		isValid =isValid & bishop.isValidMove(this.board,new Move(
				new Location(ChessBoard.Coordinate.D, 4),
				new Location(ChessBoard.Coordinate.D,4)));
		assertFalse(isValid);
	}
	
	/* (non-Javadoc)
     * @see org.amc.game.chess.ChessPieceTest#testOnBoardIsValidCapture()
     */
	@Override
    @Test
	public void testOnBoardIsValidCapture(){
		BishopPiece bishop = new BishopPiece(Colour.BLACK);
		BishopPiece bishopWhite = new BishopPiece(Colour.WHITE);
		board.putPieceOnBoardAt(bishop, new Location(ChessBoard.Coordinate.F, 8));
		board.putPieceOnBoardAt(bishopWhite, new Location(ChessBoard.Coordinate.D, 6));
		
		boolean isValid=bishop.isValidMove(this.board,new Move(
				new Location(ChessBoard.Coordinate.F,8),
				new Location(ChessBoard.Coordinate.D,6)));
		
		assertTrue(isValid);
	}
	
	/* (non-Javadoc)
     * @see org.amc.game.chess.ChessPieceTest#testOnBoardInvalidCapture()
     */
	@Override
    @Test
	public void testOnBoardInvalidCapture(){
		BishopPiece bishop = new BishopPiece(Colour.BLACK);
		BishopPiece bishopWhite = new BishopPiece(Colour.BLACK);
		board.putPieceOnBoardAt(bishop, new Location(ChessBoard.Coordinate.F, 8));
		board.putPieceOnBoardAt(bishopWhite, new Location(ChessBoard.Coordinate.D, 6));
		
		boolean isValid=bishop.isValidMove(this.board,new Move(
				new Location(ChessBoard.Coordinate.F,8),
				new Location(ChessBoard.Coordinate.D,6)));
		
		assertFalse(isValid);
	}
	
	/* (non-Javadoc)
     * @see org.amc.game.chess.ChessPieceTest#testOnBoardIsNotValidMove()
     */
	@Override
    @Test
	public void testOnBoardIsNotValidMove(){
		BishopPiece bishop = new BishopPiece(Colour.BLACK);
		BishopPiece bishopWhite = new BishopPiece(Colour.WHITE);
		board.putPieceOnBoardAt(bishop, new Location(ChessBoard.Coordinate.F, 8));
		board.putPieceOnBoardAt(bishopWhite, new Location(ChessBoard.Coordinate.D, 6));
		
		boolean isValid=bishop.isValidMove(this.board,new Move(
				new Location(ChessBoard.Coordinate.F,8),
				new Location(ChessBoard.Coordinate.C,5)));
		
		assertFalse(isValid);
	}
	
	

}
