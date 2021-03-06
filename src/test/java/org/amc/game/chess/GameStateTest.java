package org.amc.game.chess;

import static org.amc.game.chess.AbstractChessGame.GameState.*;
import static org.junit.Assert.*;

import org.amc.game.chess.view.ChessBoardView;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

public class GameStateTest {
    private ChessGame chessGame;
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ChessBoardFactory factory;

    @Before
    public void setUp() throws Exception {
        ChessBoard board = new ChessBoard();
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        factory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        chessGame = new ChessGame(board, whitePlayer, blackPlayer);
    }

    @Test
    public void runningStateTest() {
        assertEquals(RUNNING, chessGame.getGameState());
    }

    @Test
    public void whiteInCheckTest() throws ParseException, IllegalMoveException {
        ChessBoard board = factory.getChessBoard("Kd7:Qe6:ke1");
        chessGame.setChessBoard(board);
        ChessBoardView view = new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.changePlayer();
        chessGame.move(blackPlayer, new Move("E6-E8"));
        assertEquals(WHITE_IN_CHECK, chessGame.getGameState());
    }

    @Test
    public void blackInCheckTest() throws ParseException, IllegalMoveException {
        ChessBoard board = factory.getChessBoard("kd1:qe1:Ke8");
        chessGame.setChessBoard(board);
        ChessBoardView view = new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.move(whitePlayer, new Move("E1-E2"));
        assertEquals(BLACK_IN_CHECK, chessGame.getGameState());
    }

    @Test
    public void blackInCheckmateTest() throws ParseException, IllegalMoveException {
        ChessBoard board = factory.getChessBoard("kd1:qe1:Ke8:Rd8:Rf8:Pd7:Pf7");
        chessGame.setChessBoard(board);
        ChessBoardView view = new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.move(whitePlayer, new Move("E1-E2"));
        assertEquals(BLACK_CHECKMATE, chessGame.getGameState());
    }

    @Test
    public void whiteInCheckmateTest() throws ParseException, IllegalMoveException {
        ChessBoard board = factory.getChessBoard("Kd8:Qe8:ke1:rd1:rf1:pd2:pf2");
        chessGame.setChessBoard(board);
        ChessBoardView view = new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.changePlayer();
        chessGame.move(blackPlayer, new Move("E8-E7"));
        assertEquals(WHITE_CHECKMATE, chessGame.getGameState());
    }

    @Test
    public void blackMovesOutOfCheckTest() throws ParseException, IllegalMoveException {
        ChessBoard board = factory.getChessBoard("kd1:qe1:Ke8");
        chessGame.setChessBoard(board);
        ChessBoardView view = new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.move(whitePlayer, new Move("E1-E2"));
        assertEquals(BLACK_IN_CHECK, chessGame.getGameState());
        chessGame.changePlayer();
        chessGame.move(blackPlayer, new Move("E8-F7"));
        assertEquals(RUNNING, chessGame.getGameState());
    }

    @Test
    public void whiteMovesOutOfCheckTest() throws ParseException, IllegalMoveException {
        ChessBoard board = factory.getChessBoard("Kd7:Qe6:ke1");
        chessGame.setChessBoard(board);
        ChessBoardView view = new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.changePlayer();
        chessGame.move(blackPlayer, new Move("E6-E8"));
        assertEquals(WHITE_IN_CHECK, chessGame.getGameState());
        chessGame.changePlayer();
        chessGame.move(whitePlayer, new Move("E1-F2"));
        assertEquals(RUNNING, chessGame.getGameState());
    }

    @Test
    public void changeFromWhiteToBlackCheck() throws ParseException, IllegalMoveException {
        final String chessBoardStr = "Ka8:ke1:Qe8:bb1";
        final String blackMove = "E8-E7";
        final String whiteMove = "B1-E4";
        
    	ChessBoard board = factory.getChessBoard(chessBoardStr);
        chessGame.setChessBoard(board);
        ChessBoardView view = new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.changePlayer();
        chessGame.move(blackPlayer, new Move(blackMove));
        assertEquals(WHITE_IN_CHECK, chessGame.getGameState());
        chessGame.changePlayer();
        chessGame.move(whitePlayer, new Move(whiteMove));
        assertEquals(BLACK_IN_CHECK, chessGame.getGameState());
    }

    @Test
    public void testForAGameWhichHasEndedInStalemate() throws ParseException, IllegalMoveException {
        ChessBoard board = factory.getChessBoard("Ke4:Pf6:Pg7:kh5:pf5:ph4");
        chessGame.setChessBoard(board);
        ChessBoardView view = new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.changePlayer();
        chessGame.move(blackPlayer, new Move("E4-F5"));
        assertEquals(STALEMATE, chessGame.getGameState());
    }

}
