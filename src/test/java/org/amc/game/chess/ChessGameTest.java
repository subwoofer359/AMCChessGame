package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChessGameTest {

    private Player whitePlayer;
    private Player blackPlayer;
    private ChessBoard board;
    private ChessGame chessGame;
    
    @Before
    public void setUp() throws Exception {
        whitePlayer=new HumanPlayer("Teddy", Colour.WHITE);
        blackPlayer=new HumanPlayer("Robin", Colour.BLACK);
        board=new ChessBoard();
        chessGame=new ChessGame(board, whitePlayer, blackPlayer);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testChangePlayer() {
        assertEquals(whitePlayer, chessGame.getCurrentPlayer());
        chessGame.changePlayer();
        assertEquals(blackPlayer, chessGame.getCurrentPlayer());
        chessGame.changePlayer();
        assertEquals(whitePlayer, chessGame.getCurrentPlayer());
    }
    
    @Test
    public void testPlayerHasTheirKing(){
        KingPiece kingWhite=new KingPiece(Colour.WHITE);
        board.putPieceOnBoardAt(kingWhite, new Location(Coordinate.D,4));
        assertTrue(chessGame.doesThePlayerStillHaveTheirKing(whitePlayer));
    }
    
    @Test
    public void testPlayerDoesNotHaveTheirKing(){
        KingPiece kingWhite=new KingPiece(Colour.WHITE);
        board.putPieceOnBoardAt(kingWhite, new Location(Coordinate.D,4));
        assertTrue(chessGame.doesThePlayerStillHaveTheirKing(whitePlayer));
        assertFalse(chessGame.doesThePlayerStillHaveTheirKing(blackPlayer));
    }
    
    @Test
    public void isGameOverOnePlayerLosesTheirKing(){
        board.initialise();
        BishopPiece bishop=new BishopPiece(Colour.WHITE);
        
        assertFalse(chessGame.isGameOver(whitePlayer, blackPlayer));
        board.putPieceOnBoardAt(bishop,new Location(Coordinate.E,8));
        assertTrue(chessGame.isGameOver(whitePlayer, blackPlayer));
        
        board.initialise();
        bishop=new BishopPiece(Colour.BLACK);
        
        assertFalse(chessGame.isGameOver(whitePlayer, blackPlayer));
        board.putPieceOnBoardAt(bishop,new Location(Coordinate.E,1));
        assertTrue(chessGame.isGameOver(whitePlayer, blackPlayer));
       
    }
}
