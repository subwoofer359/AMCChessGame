package org.amc.game.chessserver

import static org.junit.Assert.*

import org.amc.game.chess.AbstractChessGame.GameState
import org.amc.game.chess.ChessGameFactory
import org.amc.game.chess.ChessGameFixture
import org.amc.game.chess.ComparePlayers
import org.amc.game.chess.ComputerPlayer
import org.amc.game.chess.Move
import org.amc.game.chess.RealChessGamePlayer
import org.amc.game.chess.StandardChessGameFactory
import org.amc.game.chess.VirtualChessGamePlayer
import org.junit.Before
import org.junit.Test

import groovy.transform.TypeChecked

@TypeChecked
class ComputerServerCGCompWhiteTest {

	private static final long GAME_UID = 1234L;
	private static final int PLAYER_UID = 12;
	
	private static final ChessGameFactory chessGameFactory = new StandardChessGameFactory();
	
	private ChessGameFixture fixture = new ChessGameFixture();
	
	private ComputerPlayer computer = new ComputerPlayer();
	
	private ComputerServerChessGame serverChessGame;
	
	@Before
	void setUp() {
		computer.id = PLAYER_UID;
	}
	
	@Test
	void testComputerVsPlayer() {
		serverChessGame = new ComputerServerChessGame(GAME_UID, computer);
		serverChessGame.chessGameFactory = chessGameFactory;
		
		serverChessGame.addOpponent(fixture.blackPlayer);
		
		assert ComparePlayers.isSamePlayer(serverChessGame.opponent, fixture.blackPlayer);
		assert GameState.RUNNING == serverChessGame.chessGame.gameState;
		assert serverChessGame.chessGame.allGameMoves.size() > 0;
		
		assert serverChessGame.chessGame.whitePlayer instanceof VirtualChessGamePlayer;
		assert serverChessGame.chessGame.blackPlayer instanceof RealChessGamePlayer;
	}

	@Test
	void testPlayerVsComputer() {
		serverChessGame = new ComputerServerChessGame(GAME_UID, fixture.whitePlayer);
		serverChessGame.chessGameFactory = chessGameFactory;
		
		serverChessGame.addOpponent();
		
		serverChessGame.move(fixture.whitePlayer, new Move("A2-A3"));
		
		assert ComparePlayers.isSamePlayer(serverChessGame.player, fixture.whitePlayer);
		assert GameState.RUNNING == serverChessGame.chessGame.gameState;
		assert serverChessGame.chessGame.allGameMoves.size() == 2;
		
		assert serverChessGame.chessGame.whitePlayer instanceof RealChessGamePlayer;
		assert serverChessGame.chessGame.blackPlayer instanceof VirtualChessGamePlayer;
	}
	
	void testComputerVsComputer() {
		serverChessGame = new ComputerServerChessGame(GAME_UID, computer);
		serverChessGame.chessGameFactory = chessGameFactory;
		
		serverChessGame.addOpponent();

		assert GameState.RUNNING == serverChessGame.chessGame.gameState;
		assert serverChessGame.chessGame.allGameMoves.size() == 0;
	}
	
}
