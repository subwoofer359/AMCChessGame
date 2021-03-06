package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.AbstractChessGame;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactory;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Move;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chess.view.ChessBoardView;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BugCG52ServerChessGameDidntEndInCheckmateTest {

	private ServerChessGame serverChessGame;
	private ChessGamePlayer whitePlayer;
	private ChessGamePlayer blackPlayer;
	private static ChessBoardFactory boardFactory;
	private static final String CHESSBOARD_CONFIG = "nc8:qc2:pb6:pf4:bf2:pg2:ph2:kc1:re1:ng1:Kd8:Bf8:Rh8:Ph7:Nf6:Pg6:Pd5:Pf5:Pd4";

	@BeforeClass
	public static void setUpFactory() {
		boardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
	}

	@Before
	public void setUp() throws Exception {
		whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
		blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
		ChessBoard board = boardFactory.getChessBoard(CHESSBOARD_CONFIG);
		new ChessBoardView(board);

		ChessGame chessGame = new ChessGame(board, whitePlayer, blackPlayer);
		chessGame.setChessBoard(board);
		serverChessGame = new TwoViewServerChessGame(0l, chessGame);
	}

	@Test
	public void test() throws IllegalMoveException {
		Move move = new Move("C2-C7");
		serverChessGame.move(whitePlayer, move);
		AbstractChessGame game = serverChessGame.getChessGame();
		System.out.println("Game state:" + game.getGameState());
		assertTrue(game.getGameState() == ChessGame.GameState.BLACK_CHECKMATE);
	}

}
