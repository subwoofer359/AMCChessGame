package org.amc.game.chess.controller;

import static org.amc.game.chess.NoChessPiece.NO_CHESSPIECE;
import static org.junit.Assert.*;

import org.amc.game.chess.AbstractChessGame;
import org.amc.game.chess.BishopPiece;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.KingPiece;
import org.amc.game.chess.Location;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chess.StandardChessGameFactory;
import org.amc.game.chess.StartingSquare;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.controller.ConsoleController;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

public class ConsoleControllerTest {
    private ChessBoard board;
    private Location startLocation;
    private Location endLocation;
    private AbstractChessGame game;
    private ChessPiece piece;

    @Before
    public void setUp() throws Exception {
        ChessGamePlayer player1 = new RealChessGamePlayer(new HumanPlayer("Test1"), Colour.BLACK);
        ChessGamePlayer player2 = new RealChessGamePlayer(new HumanPlayer("Test2"), Colour.WHITE);
        game = new StandardChessGameFactory().getChessGame(new ChessBoard(), player1, player2);
        board = game.getChessBoard();
        board.put(KingPiece.getPiece(Colour.WHITE),
                        StartingSquare.WHITE_KING.getLocation());
        board.put(KingPiece.getPiece(Colour.BLACK),
                        StartingSquare.BLACK_KING.getLocation());
        startLocation = new Location(Coordinate.A, 1);
        endLocation = new Location(Coordinate.B, 2);
        
        piece = BishopPiece.getPiece(Colour.BLACK);
        board.put(piece, startLocation);
    }

    @Test
    public void testTakeTurn() throws IllegalMoveException {
        ConsoleController controller = new ConsoleController(game);
        MockUserInput userInput = new MockUserInput();
        userInput.setOutput("A1-B2");
        controller.setConsole(userInput);

        controller.takeTurn();
        assertEquals(piece.moved(), board.get(endLocation));
        assertEquals(NO_CHESSPIECE, board.get(startLocation));
    }

    @Test(expected = IllegalMoveException.class)
    public void testTakeTurnThrowsException() throws IllegalMoveException, ParseException {
        ConsoleController controller = new ConsoleController(game);
        MockUserInput userInput = new MockUserInput();
        userInput.setOutput("A1-E2");
        controller.setConsole(userInput);

        controller.takeTurn();
        assertEquals(piece, board.get(endLocation));
        assertNull(board.get(startLocation));
    }

    public static class MockUserInput implements ConsoleController.UserConsole {

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
         * 
         * @param output
         *            String
         */
        public void setOutput(String output) {
            this.output = output;
        }

    }
}
