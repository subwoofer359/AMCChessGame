package org.amc.dao;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardUtilities;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Colour;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.PawnPiece;
import org.junit.Test;

import java.text.ParseException;
import static org.amc.game.chess.SimpleChessBoardSetupNotation.MOVE_TOKEN;

public class ChessBoardExternalizerTest {
	private ChessBoard board;
	private static final String BOARD_SETUP = "Ra8Nb8Bc8Qd8Ke8Bf8Ng8Rh8Pa7Pb7Pc7Pd7Pe7Pf7Pg7Ph7"
			+ "ra1nb1bc1qd1ke1bf1ng1rh1pa2pb2pc2pd2pe2pf2pg2ph2";

	@Test
	public void testGetChessBoardString() throws ParseException {
		board = ChessBoardExternalizer.getChessBoard(BOARD_SETUP);
		ChessBoard secondBoard = new ChessBoard();
		secondBoard.initialise();
		ChessBoardUtilities.compareBoards(board, secondBoard);
	}

	@Test
	public void testGetChessBoard() throws ParseException {
		board = new ChessBoard();
		board.initialise();
		String boardStr = ChessBoardExternalizer.getChessBoardString(board);
		ChessBoard secondChessBoard = ChessBoardExternalizer
				.getChessBoard(boardStr);
		ChessBoardUtilities.compareBoards(board, secondChessBoard);
	}

	@Test
	public void testDifferentConfigurationChessBoard() {
		board = new ChessBoard();
		board.initialise();
		board.move(new Move("A2-A3"));
		String boardStr = ChessBoardExternalizer.getChessBoardString(board);
		assertTrue(boardStr.contains("pa3"));
		assertFalse(boardStr.contains("pa2"));

	}
	
	@Test
	public void testExternalisingPieceMoved() {
		final String locationStr = "a1";
		board = new ChessBoard();
		ChessPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
		pawn = pawn.moved();
		Location location = new Location(locationStr);
		board.put(pawn, location);
		
		String chessBoardStr = ChessBoardExternalizer.getChessBoardString(board);
		
		assertEquals("p" + locationStr + MOVE_TOKEN, chessBoardStr);
	}
}
