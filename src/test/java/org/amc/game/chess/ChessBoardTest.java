package org.amc.game.chess;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.TestChessGame.MockUserInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class ChessBoardTest
{

    private ChessBoard board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Location endLocation;
    private Location startLocation;
	@Before
	public void setUp() throws Exception
	{
	    board=new ChessBoard();
	    whitePlayer=new HumanPlayer("Teddy", Colour.WHITE);
	    blackPlayer=new HumanPlayer("Robin", Colour.BLACK);
	    startLocation=new Location(ChessBoard.Coordinate.A,8);
        endLocation=new Location(ChessBoard.Coordinate.B,7);
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Ignore
	public void testInitialise()
	{
		fail("Not yet implemented");
	}

	@Test(expected=InvalidMoveException.class)
	public void testPlayerCantMoveOtherPlayersPiece() throws InvalidMoveException
	{
	    BishopPiece bishop=new BishopPiece(Colour.WHITE);
	    board.putPieceOnBoardAt(bishop, new Location(ChessBoard.Coordinate.A,8));
	    
	    board.move(blackPlayer, new Move(
	                    new Location(ChessBoard.Coordinate.A,8),
	                    new Location(ChessBoard.Coordinate.B,7)));
	}
	
	@Test
    public void testPlayerCanMoveTheirOwnPiece() throws InvalidMoveException
    {
        BishopPiece bishop=new BishopPiece(Colour.WHITE);
        Location startofMove=new Location(ChessBoard.Coordinate.A,8);
        Location endOfMove=new Location(ChessBoard.Coordinate.B,7);
        
        board.putPieceOnBoardAt(bishop,startofMove);
        
        board.move(whitePlayer, new Move(startofMove,endOfMove));
        
        assertEquals(bishop, board.getPieceFromBoardAt(endOfMove));
        assertNull(board.getPieceFromBoardAt(startofMove));
    }
	
	@Test(expected=InvalidMoveException.class)
	public void testMoveWithAnEmptySquare()throws InvalidMoveException{
	    Location startofMove=new Location(ChessBoard.Coordinate.A,8);
        Location endOfMove=new Location(ChessBoard.Coordinate.B,7);
        
        
        board.move(whitePlayer, new Move(startofMove,endOfMove));
        
	}
	
	   @Test
	    public void testMovesAreSaved() throws InvalidMoveException{
	        Player playerOne=new HumanPlayer("Stephen",Colour.BLACK);
	        BishopPiece bishop=new BishopPiece(Colour.BLACK);
	        board.putPieceOnBoardAt(bishop,startLocation);
	        Move move =new Move(startLocation,endLocation);
	        board.move(playerOne, move);
	        Move lastMove=board.getTheLastMove();
	        assertEquals(lastMove.getStart(),startLocation);
	        assertEquals(lastMove.getEnd(),endLocation);
	    }
}
