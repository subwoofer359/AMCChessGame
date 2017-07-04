package org.amc.game.chess;


import static org.amc.game.chess.NoChessPiece.NO_CHESSPIECE;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UnmodifibleChessBoardTest {
	
	private ChessBoard board = EmptyChessBoard.EMPTY_CHESSBOARD;
	private ChessBoard chessBoard;
	
	@Before
	public void setUp() throws Exception {
		chessBoard = new ChessBoard();
	}

	@Test
	public void testMove() {
		board.move(new Move("A2-A3"));
		ChessBoardUtil.compareBoards(board, chessBoard);
	}
	
	@Test
	public void testPutPieceOnBoardAt() {
		board.put(PawnPiece.getPiece(Colour.BLACK),  new Location("A4"));
		ChessBoardUtil.compareBoards(board, chessBoard);
	}
	
	@Test
	public void testInitialise() {
		board.initialise();
		assertEquals(NO_CHESSPIECE, 
				board.get(new Location("A1")));
	}
}
