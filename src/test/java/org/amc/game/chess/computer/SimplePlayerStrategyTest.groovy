package org.amc.game.chess.computer

import static org.junit.Assert.*

import org.amc.game.chess.AbstractChessGame
import org.amc.game.chess.ChessBoard
import org.amc.game.chess.ChessBoardFactory
import org.amc.game.chess.ChessBoardFactoryImpl
import org.amc.game.chess.ChessBoardSetupNotation
import org.amc.game.chess.ChessGameFactory
import org.amc.game.chess.ChessGameFixture
import org.amc.game.chess.EmptyMove
import org.amc.game.chess.IllegalMoveException
import org.amc.game.chess.Location
import org.amc.game.chess.LocationTest
import org.amc.game.chess.Move
import org.amc.game.chess.PawnPromotionRule
import org.amc.game.chess.QueenPiece
import org.amc.game.chess.SimpleChessBoardSetupNotation
import org.junit.Before
import org.junit.Test
import org.springframework.test.annotation.Repeat

import groovy.transform.TypeChecked

@TypeChecked
class SimplePlayerStrategyTest {
	
	private static final int REPEAT = 50;

	private ChessGameFixture fixture;
	
	private ComputerPlayerStrategy strategy;
	
	private static final ChessBoardFactory chessBoardFactory = 
		new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
	
	@Before
	void setUp() {
		fixture = new ChessGameFixture();
		
		strategy = new SimplePlayerStrategy();
	}

	@Test
	public void testMove() {
		(1 .. REPEAT).each({
			fixture.board = new ChessBoard();
			final AbstractChessGame game = fixture.chessGame;
		
			game.chessBoard.initialise();
		
			game.move(fixture.whitePlayer, new Move("A2-A3"));
		
			game.changePlayer();
		
			Move move = strategy.getNextMove(game);
		
			assertNotNull("Move should not be null", move);
		});
	}
	
	@Test
	public void testOneMove() {
		fixture.board = chessBoardFactory.getChessBoard("Kd8:kd1");
		
		final AbstractChessGame game = fixture.chessGame;
		
		game.changePlayer();
		
		Move move = strategy.getNextMove(game);
		
		assertEquals(move.start, new Location("d8"));
	}
	
	@Test
	public void testNoMove() {
		final AbstractChessGame game = fixture.chessGame;
		
		game.changePlayer();
		
		Move move = strategy.getNextMove(game);
		
		assertEquals(move, EmptyMove.EMPTY_MOVE);
	}
	
	@Test
	public void testNoMove2() {
		fixture.board = chessBoardFactory.getChessBoard("Pa1:Pb1:Pc1:Pa2:Pa3:Pb3:Pc3:Pc2:Kb2:kd7");
		final AbstractChessGame game = fixture.chessGame;
		
		game.changePlayer();
		
		try {
			Move move = strategy.getNextMove(game);
			fail("Should throw an exception");
		} catch(AssertionError ae) {
			assertTrue("Exception Not Thrown", true);
		}
	}
	
	@Test
	public void testMakeMove() {
		
		final AbstractChessGame game = fixture.chessGame;
	
		game.chessBoard.initialise();
	
		game.move(fixture.whitePlayer, new Move("A2-A3"));
	
		game.changePlayer();
		try {
			strategy.makeMove(game);
		} catch(IllegalMoveException im) {
			fail("Should be a valid move");
		}
	}
	
	@Test
	public void testMoveOutOfCheck() {
		(1 .. REPEAT).each({
			fixture.board = chessBoardFactory.getChessBoard("kd1:Nc4:Ka8:rb5:qg7");
		
			final AbstractChessGame game = fixture.chessGame;
		
			game.move(fixture.whitePlayer, new Move("b5-b6"));
		
			game.changePlayer();
		
			Move move = strategy.getNextMove(game);
		
			game.move(game.currentPlayer, move);
		});
	}
	
	@Test
	public void testPromotionMoveOutOfCheck() {
		final PawnPromotionRule rule = PawnPromotionRule.getInstance();
		(1 .. REPEAT).each({
			fixture.board = chessBoardFactory.getChessBoard("ke1:Ke8:pg7");
		
			final AbstractChessGame game = fixture.chessGame;
		
			game.move(fixture.whitePlayer, new Move("g7-g8"));
			
			rule.promotePawnTo(game, new Location("g8"), QueenPiece.QUEEN_WHITE);
		
			Move move = strategy.getNextMove(game);
		
			game.move(game.currentPlayer, move);
		});
	}

}
