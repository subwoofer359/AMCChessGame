package org.amc.game.chess;

import static org.junit.Assert.*;
import static org.amc.game.chess.StartingSquare.*;

import org.amc.game.chess.view.ChessBoardView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ChessGameCastlingKingInCheckTest {
	private static ChessGamePlayer whitePlayer = new RealChessGamePlayer(new HumanPlayer("Teddy"), Colour.WHITE);
	private static ChessGamePlayer blackPlayer = new RealChessGamePlayer(new HumanPlayer("Robin"), Colour.BLACK);
	private ChessGamePlayer currentPlayer;
	private Move defendingChessPieceMove;
	private ChessGame chessGame;
	private ChessPiece kingPiece;
	private ChessPiece rookPiece;
	private ChessBoardUtil cbUtils;

	public ChessGameCastlingKingInCheckTest(ChessGamePlayer currentPlayer, Move defendingChessPieceMove) {
		this.currentPlayer = currentPlayer;
		this.defendingChessPieceMove = defendingChessPieceMove;
	}

	@Before
	public void setUp() throws Exception {
		ChessBoard board = new ChessBoard();
		new ChessBoardView(board);
		cbUtils = new ChessBoardUtil(board);

		chessGame = new ChessGame(board, whitePlayer, blackPlayer);
		ChessPiece attackingPiece = BishopPiece.getPiece(Colour.BLACK);
		cbUtils.add(attackingPiece, "E3");
		kingPiece = KingPiece.getPiece(Colour.WHITE);
		rookPiece = RookPiece.getPiece(Colour.WHITE);
		cbUtils.add(kingPiece, WHITE_KING.getLocationStr());
		cbUtils.add(rookPiece, WHITE_ROOK_RIGHT.getLocationStr());
		cbUtils.add(KingPiece.getPiece(Colour.BLACK), BLACK_KING.getLocationStr());
	}

	@Parameters
	public static Collection<?> addedChessPieces() {

		return Arrays
				.asList(new Object[][] { { whitePlayer, new Move(WHITE_KING.getLocation(), new Location("G1")) } });

	}

	@Test(expected = IllegalMoveException.class)
	public void testInvalidMoveExceptionThrown() throws IllegalMoveException {
		chessGame.move(currentPlayer, defendingChessPieceMove);
		assertEquals(kingPiece, cbUtils.getPiece(WHITE_KING.getLocationStr()));
		assertEquals(rookPiece, cbUtils.getPiece(WHITE_ROOK_RIGHT.getLocationStr()));
	}

	@Test
	public void testPiecesAreReturnedToStartPositions() {
		try {
			chessGame.move(currentPlayer, defendingChessPieceMove);
			fail("call to move should thrown an IllegalMoveException");
		} catch (IllegalMoveException ime) {
			assertEquals(kingPiece, cbUtils.getPiece(WHITE_KING.getLocationStr()));
			assertEquals(rookPiece, cbUtils.getPiece(WHITE_ROOK_RIGHT.getLocationStr()));
		}

	}
}
