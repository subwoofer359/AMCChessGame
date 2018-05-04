package org.amc.game.chess

import static org.junit.Assert.*

import org.amc.game.chess.AbstractChessGame.GameState
import org.amc.game.chess.view.ChessBoardView
import org.amc.game.chessserver.AbstractServerChessGame
import org.amc.game.chessserver.ComputerServerChessGame
import org.junit.Before
import org.junit.Test

import groovy.transform.TypeChecked

@TypeChecked
class CG_116_BugFix_PawnPromotionTest {
	ComputerServerChessGame scg;
	
	ChessGameFixture fixture = new ChessGameFixture();
	
	ChessBoard board;
	
	private static final String BOARD = "ra1:nb1:kf1:ng1:bd2:qe2:rf2:Ph3:pd5:qb6:Pd6:Nf6:Kf7:Ph7:pe7:Bf8";
	
	static final long GAME_UID = 1234L;
	
	@Before
	public void setUp() throws Exception {
		board = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation()).getChessBoard(BOARD);
		fixture.chessGame = new StandardChessGame(board, fixture.whitePlayer, fixture.blackPlayer);
		
		scg = new ComputerServerChessGame(GAME_UID, fixture.whitePlayer);
		
		scg.chessGameFactory = new ChessGameFactory() {
			public AbstractChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite, ChessGamePlayer playerBlack) {
				return fixture.chessGame;
			}
		}
		
		scg.addOpponent();
	}

	@Test
	public void test() {
		Move move = new Move("e7-f8");
		
		scg.move(fixture.whitePlayer, move);
		
		new ChessBoardView(scg.chessGame.chessBoard).displayTheBoard();
		
		assert fixture.chessGame.gameState == GameState.PAWN_PROMOTION;
		assert ComparePlayers.isSamePlayer(scg.chessGame.currentPlayer, scg.chessGame.whitePlayer);
	}

}
