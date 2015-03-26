package org.amc.game.chess.controller;

import static org.junit.Assert.*;

import org.amc.game.chess.BishopPiece;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.KingPiece;
import org.amc.game.chess.Location;
import org.amc.game.chess.Player;
import org.amc.game.chess.StartingSquare;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.controller.ConsoleController;
import org.amc.game.chess.controller.InputParser;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

import static org.mockito.Mockito.*;

public class ConsoleControllerTest {
    private ChessBoard board;
    private Player player1;
    private Player player2;
    private Location startLocation;
    private Location endLocation;
    private ChessGame game;
    private ChessPiece piece;
    
    @Before
    public void setUp() throws Exception {
        player1=new HumanPlayer("Test1", Colour.BLACK);
        player2=new HumanPlayer("Test2", Colour.WHITE);
        board=new ChessBoard();
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE), StartingSquare.WHITE_KING.getLocation());
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK), StartingSquare.BLACK_KING.getLocation());
        startLocation=new Location(Coordinate.A,1);
        endLocation=new Location(Coordinate.B,2);
        game=new ChessGame(board,player1,player2);
        piece =new BishopPiece(Colour.BLACK);
        board.putPieceOnBoardAt(piece,startLocation);
    }

    @Test
    public void testTakeTurn()throws IllegalMoveException{
        ConsoleController controller=new ConsoleController(game);
        MockUserInput userInput=new MockUserInput();
        userInput.setOutput("A1B2");
        controller.setConsole(userInput);
        
        controller.takeTurn();
        assertEquals(piece, board.getPieceFromBoardAt(endLocation));
        assertNull(board.getPieceFromBoardAt(startLocation));
    }
    
    @SuppressWarnings("unchecked")
    @Test(expected=IllegalMoveException.class)
    public void testTakeTurnThrowsException()throws IllegalMoveException,ParseException{
        InputParser parser=mock(InputParser.class);
        when(parser.parseMoveString(anyString())).thenThrow(ParseException.class);
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
