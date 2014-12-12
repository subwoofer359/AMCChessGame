package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;
import static org.junit.Assert.*;

import org.amc.game.chess.ChessGame.GameState;
import org.amc.game.chess.view.ChessBoardView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

public class GameStateTest {
    private ChessBoard board;
    private ChessGame chessGame;
    private Player whitePlayer;
    private Player blackPlayer;
    private ChessBoardFactory factory;
    
    @Before
    public void setUp() throws Exception {
        board=new ChessBoard();
        whitePlayer=new HumanPlayer("White Player",Colour.WHITE);
        blackPlayer=new HumanPlayer("Black Player", Colour.BLACK);
        factory=new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        chessGame=new ChessGame(board,whitePlayer,blackPlayer);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void runningStateTest() {
        assertEquals(GameState.RUNNING,chessGame.getGameState());
    }
    
    @Test
    public void whiteInCheckTest()throws ParseException,InvalidMoveException{
        ChessBoard board=factory.getChessBoard("Kd7:Qe6:ke1");
        chessGame.setChessBoard(board);
        ChessBoardView view =new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.move(blackPlayer, new Move(new Location(E,6),new Location(E,8)));
        assertEquals(GameState.WHITE_IN_CHECK,chessGame.getGameState());
    }
    
    @Test
    public void blackInCheckTest()throws ParseException,InvalidMoveException{
        ChessBoard board=factory.getChessBoard("kd1:qe1:Ke8");
        chessGame.setChessBoard(board);
        ChessBoardView view =new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.move(whitePlayer, new Move(new Location(E,1),new Location(E,2)));
        assertEquals(GameState.BLACK_IN_CHECK,chessGame.getGameState());
    }
    
    @Test
    public void blackInCheckmateTest() throws ParseException,InvalidMoveException{
        ChessBoard board=factory.getChessBoard("kd1:qe1:Ke8:Rd8:Rf8:Pd7:Pf7");
        chessGame.setChessBoard(board);
        ChessBoardView view =new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.move(whitePlayer, new Move(new Location(E,1),new Location(E,2)));
        assertEquals(GameState.BLACK_CHECKMATE,chessGame.getGameState());
    }
    
    @Test
    public void whiteInCheckmateTest() throws ParseException,InvalidMoveException{
        ChessBoard board=factory.getChessBoard("Kd8:Qe8:ke1:rd1:rf1:pd2:pf2");
        chessGame.setChessBoard(board);
        ChessBoardView view =new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.move(blackPlayer, new Move(new Location(E,8),new Location(E,7)));
        assertEquals(GameState.WHITE_CHECKMATE,chessGame.getGameState());
    }
    
    @Test
    public void blackMovesOutOfCheckTest()throws ParseException,InvalidMoveException{
        ChessBoard board=factory.getChessBoard("kd1:qe1:Ke8");
        chessGame.setChessBoard(board);
        ChessBoardView view =new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.move(whitePlayer, new Move(new Location(E,1),new Location(E,2)));
        assertEquals(GameState.BLACK_IN_CHECK,chessGame.getGameState());
        chessGame.move(blackPlayer, new Move(new Location(E,8),new Location(F,7)));
        assertEquals(GameState.RUNNING,chessGame.getGameState());
    }
    
    @Test
    public void whiteMovesOutOfCheckTest()throws ParseException,InvalidMoveException{
        ChessBoard board=factory.getChessBoard("Kd7:Qe6:ke1");
        chessGame.setChessBoard(board);
        ChessBoardView view =new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.move(blackPlayer, new Move(new Location(E,6),new Location(E,8)));
        assertEquals(GameState.WHITE_IN_CHECK,chessGame.getGameState());
        chessGame.move(whitePlayer, new Move(new Location(E,1),new Location(F,2)));
        assertEquals(GameState.RUNNING,chessGame.getGameState());
    }
    
    @Test
    public void changeFromWhiteToBlackCheck()throws ParseException,InvalidMoveException{
        ChessBoard board=factory.getChessBoard("Ka8:ke1:Qe8:bb1");
        chessGame.setChessBoard(board);
        ChessBoardView view =new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.move(blackPlayer, new Move(new Location(E,8),new Location(E,7)));
        assertEquals(GameState.WHITE_IN_CHECK,chessGame.getGameState());
        chessGame.move(whitePlayer, new Move(new Location(B,1),new Location(E,4)));
        assertEquals(GameState.BLACK_IN_CHECK,chessGame.getGameState());
    }
    
    @Test
    public void testForAGameWhichHasEndedInStalemate()throws ParseException,InvalidMoveException{
        ChessBoard board=factory.getChessBoard("Ke4:Pf6:Pg7:kh5:pf5:ph4");
        chessGame.setChessBoard(board);
        ChessBoardView view =new ChessBoardView(board);
        view.displayTheBoard();
        chessGame.move(blackPlayer, new Move(new Location(E,4),new Location(F,5)));
        assertEquals(GameState.STALEMATE,chessGame.getGameState());
    }

}
