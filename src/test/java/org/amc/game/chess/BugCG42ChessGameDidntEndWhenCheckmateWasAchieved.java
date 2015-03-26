package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;
import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactory;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chess.view.ChessBoardView;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Bug Jira CG-42: Chess game didn't end when checkmate was achieved
 * 
 * Test ChessGame for ChessGame.GameState.BLACK_CHECKMATE
 * 
 * @author Adrian Mclaughlin
 *
 */
public class BugCG42ChessGameDidntEndWhenCheckmateWasAchieved {

    private ChessGame chessGame;
    private ChessBoard board;
    private Player whitePlayer;
    private Player blackPlayer;
    private static ChessBoardFactory boardFactory;
    
    @BeforeClass
    public static void setUpFactory(){
        boardFactory=new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
    }
    
    @Before
    public void setUp() throws Exception {
        whitePlayer=new HumanPlayer("White Player",Colour.WHITE);
        blackPlayer=new HumanPlayer("Black Player", Colour.BLACK);
        board=boardFactory.getChessBoard("ra8:nc8:rc6:pd3:pb2:pe2:pf2:ph2:nb1:ke1:Pf7:Pg6:Ph6:Ka5:Pc3");
        new ChessBoardView(board);
        chessGame=new ChessGame(board, whitePlayer, blackPlayer);
        chessGame.setChessBoard(board);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws IllegalMoveException {
        Move move =new Move(new Location(C,6), new Location(B,6));
        chessGame.move(whitePlayer, move);
        assertTrue(chessGame.isCheckMate(blackPlayer, board));
        assertTrue(chessGame.getGameState() == ChessGame.GameState.BLACK_CHECKMATE);
    }

}
