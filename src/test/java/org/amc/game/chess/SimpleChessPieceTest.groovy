package org.amc.game.chess

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class SimpleChessPieceTest {
	
	ChessBoard board;
	
	ChessGameFixture fixture = new ChessGameFixture();
	
	PawnPiece pawn = PawnPiece.PAWN_WHITE;
	
	PawnPiece pawnOther = PawnPiece.PAWN_WHITE;
	
	@Before
	void setUp() {
		board = new ChessBoard();
	}

	@Test
	void testOwnPieceInCaptureSquare() {
		assertFalse("Own piece is in end square",
			pawn.isEndSquareOccupiedByOpponentsPiece(board, setUpBoardAndReturnMove("d1-c2")));
	}
	
	private Move setUpBoardAndReturnMove(String moveStr) {
		Move move = new Move(moveStr);
		board.put(pawn, move.start);
		board.put(pawnOther, move.end);
		return move;
	}
	
	@Test
	void testNoChessPieceInCaptureSquare() {
		assertFalse("No piece is in end square", pawn.isEndSquareOccupiedByOpponentsPiece(board, setUpBoardAndReturnMove("d1-e2")));
	}
	
	@Test
	void testOpponentInCaptureSquare() {
		Move move = setUpBoardAndReturnMove("d1-e2");
		PawnPiece opponentPiece = PawnPiece.PAWN_BLACK;
		board.put(opponentPiece, move.end);
		
		assertTrue("Opponent piece is in end square so should be true", pawn.isEndSquareOccupiedByOpponentsPiece(board, move));
	}
}
