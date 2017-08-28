package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.AbstractChessGame
import org.amc.game.chess.AbstractChessGame.GameState
import org.amc.game.chess.ChessBoard
import org.amc.game.chess.ChessBoardFactory
import org.amc.game.chess.ChessBoardFactoryImpl
import org.amc.game.chess.ChessGameFactory
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.ChessGamePlayer
import org.amc.game.chess.ComparePlayers
import org.amc.game.chess.ComputerPlayer
import org.amc.game.chess.Location
import org.amc.game.chess.LocationTest
import org.amc.game.chess.Move
import org.amc.game.chess.QueenPiece
import org.amc.game.chess.SimpleChessBoardSetupNotation
import org.amc.game.chess.StandardChessGameFactory;
import org.junit.Before;
import org.junit.Test;

import groovy.transform.TypeChecked

@TypeChecked
class ComputerServerChessGameTest {
	private static final long gameUID = 1234L;
	private static final ChessGameFactory factory = new StandardChessGameFactory();
	private static final ChessBoardFactory boardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
	
	private ChessGameFixture fixture = new ChessGameFixture();
	private ComputerServerChessGame csc;
	
	@Before
	void setup() { 
		csc = new ComputerServerChessGame(gameUID, fixture.whitePlayer);
		csc.chessGameFactory = factory;	
	}
	
	@Test
	void test() {
		assertTrue(true);	
	}
	
	@Test
	void testAddOpponentIgnoredAndLogged() {
		csc.addOpponent(fixture.blackPlayer);
		assert csc.opponent != fixture.blackPlayer;
	}
	
	@Test
	void testAddOpponent() {
		csc.addOpponent();
		
		ChessGamePlayer player = csc.opponent;
		
		assert player.name == ComputerPlayer.NAME;
	}
	
	@Test
	void testComputerPlayerCalledToMove() {
		csc.addOpponent();
		
		List<String> moves = ["A2-A4", "B2-B3", "C2-C3", "D2-D3", "E2-E4", "F2-F3", "G2-G3"];
		moves.each({moveStr -> 
			csc.move(fixture.whitePlayer, new Move(moveStr));
		});
		
		assert csc.chessGame.allGameMoves.size() == moves.size() * 2;
	}
	
	@Test
	void testPromotionOfWhite() {
		csc.addOpponent();
		csc.chessGame.setChessBoard(boardFactory.getChessBoard("ke1:Ke8:pg7"));
		csc.move(fixture.whitePlayer, new Move("g7-g8"));
		csc.promotePawnTo(QueenPiece.QUEEN_WHITE, new Location("g8"));
		
		assert csc.chessGame.gameState == GameState.RUNNING;
		
		assert ComparePlayers.isSameName(csc.chessGame.whitePlayer, csc.chessGame.currentPlayer);
	}
	
	@Test
	void testPromotionOfBlack() {
		csc.addOpponent();
		csc.chessGame.setChessBoard(boardFactory.getChessBoard("ke1:Kh8:Pa2:rg6:rg1"));
		
		csc.move(fixture.whitePlayer, new Move("g6-g7"));
		
		assert csc.chessGame.gameState == GameState.WHITE_IN_CHECK;
		
		assert ComparePlayers.isSameName(csc.chessGame.whitePlayer, csc.chessGame.currentPlayer);
	}
}
