package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestChessGame {

    private Player whitePlayer;
    private Player blackPlayer;
    private ChessBoard board;
    private ChessGame game;
    
    @Before
    public void setUp() throws Exception {
        whitePlayer=new HumanPlayer("Teddy", Colour.WHITE);
        blackPlayer=new HumanPlayer("Robin", Colour.BLACK);
        board=new ChessBoard();
        game=new ChessGame(whitePlayer,blackPlayer);
        game.setBoard(board);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPlayerHasTheirKing(){
        KingPiece kingWhite=new KingPiece(Colour.WHITE);
        board.putPieceOnBoardAt(kingWhite, new Location(Coordinate.D,4));
        assertTrue(game.doesThePlayerStillHaveTheirKing(whitePlayer));
    }
    
    @Test
    public void testPlayerDoesNotHaveTheirKing(){
        KingPiece kingWhite=new KingPiece(Colour.WHITE);
        board.putPieceOnBoardAt(kingWhite, new Location(Coordinate.D,4));
        assertTrue(game.doesThePlayerStillHaveTheirKing(whitePlayer));
        assertFalse(game.doesThePlayerStillHaveTheirKing(blackPlayer));
    }
    
    @Test
    public void isGameOverOnePlayerLosesTheirKing(){
        board.initialise();
        BishopPiece bishop=new BishopPiece(Colour.WHITE);
        
        assertFalse(game.isGameOver(whitePlayer, blackPlayer));
        board.putPieceOnBoardAt(bishop,new Location(Coordinate.E,8));
        assertTrue(game.isGameOver(whitePlayer, blackPlayer));
        
        board.initialise();
        bishop=new BishopPiece(Colour.BLACK);
        
        assertFalse(game.isGameOver(whitePlayer, blackPlayer));
        board.putPieceOnBoardAt(bishop,new Location(Coordinate.E,1));
        assertTrue(game.isGameOver(whitePlayer, blackPlayer));
       
    }
    
    @Test
    public void testStart(){
        board.initialise();
        game.setView(new ChessBoardView(board));
        ConsoleController controller=new ConsoleController(board, whitePlayer, blackPlayer);
        controller.setConsole(new MockUserInput());
        game.setController(controller);
        game.start();
        fail("Need to improve coverage");
        
    }
    
    public static class MockUserInput implements ConsoleController.UserConsole{

        private String[] output={"F1B5","C8A6","B5E8"};
        private int counter=0;
        @Override
        public String readLine(String fmt, Object... args) {
            return getNextOutput();
        }

        @Override
        public String readLine() {
            return getNextOutput();
        }
        
        private String getNextOutput(){
            if(counter<output.length){
                return output[counter++];
            }
            else
            {
                return null;
            }
        }      
    }
    
}
