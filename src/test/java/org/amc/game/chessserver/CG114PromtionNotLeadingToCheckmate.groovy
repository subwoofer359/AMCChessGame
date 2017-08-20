package org.amc.game.chessserver

import static org.junit.Assert.*

import org.amc.game.chess.AbstractChessGame
import org.amc.game.chess.ChessBoard
import org.amc.game.chess.ChessBoardFactory
import org.amc.game.chess.ChessBoardFactoryImpl
import org.amc.game.chess.ChessGame
import org.amc.game.chess.ChessGameFactory
import org.amc.game.chess.ChessGameFixture
import org.amc.game.chess.ChessGamePlayer
import org.amc.game.chess.Location
import org.amc.game.chess.Move
import org.amc.game.chess.QueenPiece
import org.amc.game.chess.SimpleChessBoardSetupNotation
import org.amc.game.chess.StandardChessGame
import org.junit.Before
import org.junit.Test

class CG114PromtionNotLeadingToCheckmate {
 
	ServerChessGame scg;
	ChessGameFixture cgFixture;
	ChessBoardFactory factory;
	
	private static long GAME_UUID = 1234L;
	
	private static final String BOARD_CONFIG = "ra1bc1pa2pb2Nf2pg2ph2na3Ph3pc4kf4bg6Pa7Pb7Pc7Ka8pd7Bf8";
	
	@Before
	public void setUp() throws Exception {
		factory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
		cgFixture = new ChessGameFixture();
		cgFixture.chessGame = new StandardChessGame(factory.getChessBoard(BOARD_CONFIG),
			cgFixture.whitePlayer,
			cgFixture.blackPlayer);
		
		scg = new OneViewServerChessGame(GAME_UUID, cgFixture.whitePlayer);
		scg.chessGameFactory = new ChessGameFactory() {
			public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite, ChessGamePlayer playerBlack) {
				return cgFixture.chessGame;
			}
		};
		
		scg.addOpponent(cgFixture.blackPlayer);
	}

	@Test
	public void test() {
		Move move = new Move("D7-D8");
		
		scg.move(cgFixture.whitePlayer, move);
		
		scg.promotePawnTo(QueenPiece.QUEEN_WHITE, new Location("D8"));
		
		assertEquals("Should be Black Checkmate", AbstractChessGame.GameState.BLACK_CHECKMATE, scg.chessGame.gameState);
		assertEquals("Should be Game Over", AbstractServerChessGame.ServerGameStatus.FINISHED, scg.currentStatus);
	}
}
