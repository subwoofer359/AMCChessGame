package org.amc.game.chess;

import static org.junit.Assert.*;
import static org.amc.game.chess.ChessBoard.Coordinate.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.List;

public class ChessBoardTest {

    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ChessBoard board;
  
    private ChessBoardFactory factory;

    @Before
    public void setUp() throws Exception {
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        factory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        board = new ChessBoard();
    }

    @After
    public void tearDown() throws Exception {
    }

   

    @Test
    public void testInitialse() {
        board.initialise();
        for (int i = 7; i <= 8; i++) {
            for (Coordinate coord : Coordinate.values()) {
                ChessPiece piece = board.getPieceFromBoardAt(coord.getIndex(), i);
                assertTrue(piece instanceof ChessPiece);
                assertEquals(piece.getColour(), Colour.BLACK);
            }
        }
        for (int i = 3; i <= 6; i++) {
            for (Coordinate coord : Coordinate.values()) {
                assertNull(board.getPieceFromBoardAt(coord.getIndex(), i));
            }
        }
        for (int i = 1; i <= 2; i++) {
            for (Coordinate coord : Coordinate.values()) {
                ChessPiece piece = board.getPieceFromBoardAt(coord.getIndex(), i);
                assertTrue(piece instanceof ChessPiece);
                assertEquals(piece.getColour(), Colour.WHITE);
            }
        }

    }

    /**
     * Tests the copying constructor of ChessBoard and copy method of ChessPiece
     */
    @Test
    public void CloneConstructorTest() {
        board.initialise();
        ChessBoard clone = new ChessBoard(board);
        for (Coordinate coord : ChessBoard.Coordinate.values()) {
            for (int i = 1; i <= ChessBoard.BOARD_WIDTH; i++) {
                Location location = new Location(coord, i);
                ChessPiece piece = board.getPieceFromBoardAt(location);
                ChessPiece clonedPiece = clone.getPieceFromBoardAt(location);
                if (piece instanceof ChessPiece) {
                    //assertFalse(piece == clonedPiece);
                    assertTrue(piece.equals(clonedPiece));
                } else {
                    assertNull(piece);
                    assertNull(clonedPiece);
                }
            }
        }

    }

    @Test
    public void CloneConstuctorPieceMovedCopyTest() {
        board.initialise();
        board.getPieceFromBoardAt(StartingSquare.BLACK_KING.getLocation()).moved();
        board.getPieceFromBoardAt(StartingSquare.WHITE_KING.getLocation()).moved();
        ChessBoard clone = new ChessBoard(board);
        assertTrue(clone.getPieceFromBoardAt(StartingSquare.BLACK_KING.getLocation()).hasMoved());
        assertTrue(clone.getPieceFromBoardAt(StartingSquare.WHITE_KING.getLocation()).hasMoved());
        assertFalse(clone.getPieceFromBoardAt(StartingSquare.BLACK_KNIGHT_LEFT.getLocation())
                        .hasMoved());
        assertFalse(clone.getPieceFromBoardAt(StartingSquare.WHITE_ROOK_LEFT.getLocation())
                        .hasMoved());

    }

    @Test
    public void getListOfPlayersPiecesOnTheBoardTest() throws ParseException {
        ChessBoard board = factory.getChessBoard("Ke1:ke2:Bf1:nf3:na1:ra2");
        List<?> blackPieceList = board.getListOfPlayersPiecesOnTheBoard(blackPlayer);
        List<?> whitePieceList = board.getListOfPlayersPiecesOnTheBoard(whitePlayer);
        assertTrue(blackPieceList.size() == 2 && whitePieceList.size() == 4);
    }

    @Test
    public void getPlayersKingLocationTest() throws ParseException {
        ChessBoard board = factory.getChessBoard("Ke1:ka1");
        Location blackKingLocation = board.getPlayersKingLocation(blackPlayer);
        Location whiteKingLocation = board.getPlayersKingLocation(whitePlayer);

        assertTrue(new Location(E, 1).equals(blackKingLocation));
        assertTrue(new Location(A, 1).equals(whiteKingLocation));
    }

    @Test(expected = RuntimeException.class)
    public void getPlayersKingLocationFailTest() throws ParseException {
        ChessBoard board = factory.getChessBoard("ka1");
        board.getPlayersKingLocation(blackPlayer);
    }
}
