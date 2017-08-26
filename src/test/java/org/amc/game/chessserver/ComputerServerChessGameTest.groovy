package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.AbstractChessGame
import org.amc.game.chess.ChessBoard
import org.amc.game.chess.ChessGameFactory
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.ChessGamePlayer
import org.amc.game.chess.ComparePlayers
import org.amc.game.chess.ComputerPlayer
import org.amc.game.chess.Move
import org.amc.game.chess.StandardChessGameFactory;
import org.junit.Before;
import org.junit.Test;

import groovy.transform.TypeChecked

@TypeChecked
class ComputerServerChessGameTest {
	private ChessGameFixture fixture = new ChessGameFixture();
	private ComputerServerChessGame csc;
	private static final long gameUID = 1234L;
	private static final ChessGameFactory factory = new StandardChessGameFactory();
	
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
}
