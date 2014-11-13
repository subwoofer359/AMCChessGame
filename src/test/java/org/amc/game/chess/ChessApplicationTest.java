package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChessApplicationTest {

    private Player whitePlayer;
    private Player blackPlayer;
    private ChessBoard board;
    private ChessApplication game;
    private ChessGame chessGame;
    
    @Before
    public void setUp() throws Exception {
        whitePlayer=new HumanPlayer("Teddy", Colour.WHITE);
        blackPlayer=new HumanPlayer("Robin", Colour.BLACK);
        board=new ChessBoard();
        game=new ChessApplication(whitePlayer,blackPlayer);
        chessGame=new ChessGame(board, whitePlayer, blackPlayer);
        game.setChessGame(chessGame);
    }
    
    @After
    public void tearDown() throws Exception {
    }
 
    /**
     * Todo Need to add an assertion, just calls a line in a catch block in ChessGame.start()
     */
    @Test
    public void testStartThrowsInvalidMoveException(){
        final String[] winningMoves={"F1E1","F1E2","F8B4","E2D3","B4E1"};
        setupChessBoardInSimpleTestState();
        game.setView(new ChessBoardView(board));
        ConsoleController controller=new ConsoleController(chessGame);
        controller.setConsole(new MockUserInput(winningMoves));
        game.setController(controller);
        game.start();
        assertFalse(whitePlayer.isWinner());
        assertTrue(blackPlayer.isWinner());
    }
    private void setupChessBoardInSimpleTestState(){
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location(Coordinate.C,1));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location(Coordinate.F,1));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE), new Location(Coordinate.E, 1));
           
        board.putPieceOnBoardAt(new BishopPiece(Colour.BLACK), new Location(Coordinate.C,8));
        board.putPieceOnBoardAt(new BishopPiece(Colour.BLACK), new Location(Coordinate.F,8));
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK), new Location(Coordinate.E, 8));
    
    }
    
    @Test
    public void testStartPlayerOneWinner(){
        final String[] winningMoves={"F1B5","C8A6","B5E8"};
        setupChessBoardInSimpleTestState();
        game.setView(new ChessBoardView(board));
        ConsoleController controller=new ConsoleController(chessGame);
        controller.setConsole(new MockUserInput(winningMoves));
        game.setController(controller);
        game.start();
        assertTrue(whitePlayer.isWinner());
        assertFalse(blackPlayer.isWinner());
    }
       
    @Test
    public void testStartPlayerTwoWinner(){
        final String[] winningMoves={"F1E2","F8B4","E2D3","B4E1"};
        setupChessBoardInSimpleTestState();
        game.setView(new ChessBoardView(board));
        ConsoleController controller=new ConsoleController(chessGame);
        controller.setConsole(new MockUserInput(winningMoves));
        game.setController(controller);
        game.start();
        assertFalse(whitePlayer.isWinner());
        assertTrue(blackPlayer.isWinner());
    }
    
    
    public static class MockUserInput implements ConsoleController.UserConsole{

        private String[] output={};
        private int counter=0;
        
        public MockUserInput(String[] desiredOutput) {
            this.output=desiredOutput;
        }
        
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
