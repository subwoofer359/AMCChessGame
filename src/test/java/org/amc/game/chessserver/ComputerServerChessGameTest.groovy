package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.AbstractChessGame;
import org.amc.game.chess.AbstractChessGame.GameState;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactory;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.ComputerPlayer;
import org.amc.game.chess.Location;
import org.amc.game.chess.LocationTest;
import org.amc.game.chess.Move;
import org.amc.game.chess.QueenPiece;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
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
		final String board = "ke1:Ke8:pa2:pb2:pc2:pd2:pe2:pf2:pg2:ph2";
		List<String> moves = ["A2-A4", "B2-B3", "C2-C3", "D2-D3", "E2-E4", "F2-F3", "G2-G3"];
		
		csc.addOpponent();
		csc.chessGame.setChessBoard(boardFactory.getChessBoard(board));
		
		moves.each({moveStr -> 
			csc.move(fixture.whitePlayer, new Move(moveStr));
		});
		
		assert csc.chessGame.allGameMoves.size() == moves.size() * 2;
	}
	
	@Test
	void testComputerPlayerCalledToCheckMove() {
		final String board = "ke1:Ke8:rd6:pc6:qg1";
		List<String> moves = ["D6-D7", "G1-G2", "G2-G3", "G3-G4", "G4-G5", "G5-G6"];
		
		csc.addOpponent();
		csc.chessGame.setChessBoard(boardFactory.getChessBoard(board));
		
		moves.each({moveStr ->
			csc.move(fixture.whitePlayer, new Move(moveStr));
		});
		
		assert csc.chessGame.allGameMoves.size() == moves.size() * 2 - 1;
	}
	
	@Test
	void testPromotionOfWhite() {
		final String board = "ke1:Ke8:pg7";
		final Move move = new Move("g7-g8");
		
		csc.addOpponent();
		csc.chessGame.setChessBoard(boardFactory.getChessBoard(board));
		
		csc.move(fixture.whitePlayer, move);
		csc.promotePawnTo(QueenPiece.QUEEN_WHITE, move.end);
		
		assert csc.chessGame.gameState == GameState.RUNNING;
		assert ComparePlayers.isSameName(csc.chessGame.whitePlayer, csc.chessGame.currentPlayer);
	}
	
	@Test
	void testPromotionOfBlack() {
		final String board = "ke1:Kh8:Pa2:rg6:rg1";
		csc.addOpponent();
		csc.chessGame.setChessBoard(boardFactory.getChessBoard(board));
		
		csc.move(fixture.whitePlayer, new Move("g6-g7"));
		
		assert csc.chessGame.gameState == GameState.WHITE_IN_CHECK;
		assert ComparePlayers.isSameName(csc.chessGame.whitePlayer, csc.chessGame.currentPlayer);
	}
	
	@Test
	void testPromotionofPlayerBlack() {
		final String board = "ke1:Ke8:Pg2";
		final Move move = new Move("g2-g1");
		
		csc = new ComputerServerChessGame(gameUID, new ComputerPlayer());
		csc.chessGameFactory = factory;
		csc.addOpponent(fixture.blackPlayer);
		csc.chessGame.setChessBoard(boardFactory.getChessBoard(board));
		
		csc.move(fixture.blackPlayer, move);
		csc.promotePawnTo(QueenPiece.QUEEN_BLACK, move.end);
		
		assert csc.chessGame.gameState == GameState.RUNNING;
		assert ComparePlayers.isSameName(csc.chessGame.blackPlayer, csc.chessGame.currentPlayer);
	}
}
