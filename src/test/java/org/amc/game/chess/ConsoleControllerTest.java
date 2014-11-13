package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

import static org.mockito.Mockito.*;

public class ConsoleControllerTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTakeTurn()throws InvalidMoveException{
        Player player1=new HumanPlayer("Test1", Colour.BLACK);
        Player player2=new HumanPlayer("Test2", Colour.WHITE);
        Location startLocation=new Location(Coordinate.A,1);
        Location endLocation=new Location(Coordinate.B,2);
        
        ChessPiece piece =new BishopPiece(Colour.BLACK);
        ChessBoard board=new ChessBoard();
        ChessGame game=new ChessGame(board,player1,player2);
        board.putPieceOnBoardAt(piece,startLocation);
        
        ConsoleController controller=new ConsoleController(game);
        MockUserInput userInput=new MockUserInput();
        userInput.setOutput("A1B2");
        controller.setConsole(userInput);
        
        controller.takeTurn();
        assertEquals(piece, board.getPieceFromBoardAt(endLocation));
        assertNull(board.getPieceFromBoardAt(startLocation));
    }
    
    @Test(expected=InvalidMoveException.class)
    public void testTakeTurnThrowsException()throws InvalidMoveException,ParseException{
        Player player1=new HumanPlayer("Test1", Colour.BLACK);
        Player player2=new HumanPlayer("Test2", Colour.WHITE);
        Location startLocation=new Location(Coordinate.A,1);
        Location endLocation=new Location(Coordinate.B,2);
        InputParser parser=mock(InputParser.class);
        when(parser.parseMoveString(anyString())).thenThrow(ParseException.class);
        
        
        ChessPiece piece =new BishopPiece(Colour.BLACK);
        ChessBoard board=new ChessBoard();
        board.putPieceOnBoardAt(piece,startLocation);
        ChessGame game=new ChessGame(board,player1,player2);
        
        ConsoleController controller=new ConsoleController(game);
        controller.setInputParser(parser);
        MockUserInput userInput=new MockUserInput();
        userInput.setOutput("A1B2");
        controller.setConsole(userInput);
        
        controller.takeTurn();
        assertEquals(piece, board.getPieceFromBoardAt(endLocation));
        assertNull(board.getPieceFromBoardAt(startLocation));
    }
    
    public static class MockUserInput implements ConsoleController.UserConsole{

        private String output; 
        @Override
        public String readLine(String fmt, Object... args) {
            return output;
        }

        @Override
        public String readLine() {
            return output;
        }
        
        /**
         * Set the output String for readLine methods to return
         * @param output String
         */
        public void setOutput(String output){
            this.output=output;
        }
        
    }
}
