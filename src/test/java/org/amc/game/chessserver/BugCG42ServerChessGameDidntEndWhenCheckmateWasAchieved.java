package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactory;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessBoardUtilities;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chess.view.ChessBoardView;
import org.amc.game.chessserver.ServerChessGame;
import org.junit.*;

/**
 * Bug Jira CG-42: Chess game didn't end when checkmate was achieved
 * 
 * Test ServerChessGame for ServerChessGame.status.FINISHED
 * 
 * @author Adrian Mclaughlin
 *
 */
public class BugCG42ServerChessGameDidntEndWhenCheckmateWasAchieved {

    private ServerChessGame serverChessGame;
    private ChessGamePlayer whitePlayer;
    private static ChessBoardFactory boardFactory;
    private ChessBoardUtilities cbUtils;

    @BeforeClass
    public static void setUpFactory() {
        boardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
    }

    @Before
    public void setUp() throws Exception {
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        ChessGamePlayer blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        ChessBoard board = boardFactory
                        .getChessBoard("ra8:nc8:rc6:pd3:pb2:pe2:pf2:ph2:nb1:ke1:Pf7:Pg6:Ph6:Ka5:Pc3");
        new ChessBoardView(board);
        cbUtils = new ChessBoardUtilities(board); 
        
        ChessGame chessGame = new ChessGame(board, whitePlayer, blackPlayer);
        chessGame.setChessBoard(board);
        serverChessGame = new TwoViewServerChessGame(0l, chessGame);
    }

    @Test
    public void test() throws IllegalMoveException {
        serverChessGame.move(whitePlayer, cbUtils.createMove("C6", "B6"));
        assertTrue(serverChessGame.getCurrentStatus() == ServerChessGame.ServerGameStatus.FINISHED);
    }

}
