package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.*;

import java.text.ParseException;
import java.util.List;

public class ChessBoardTest {

    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ChessBoard board;
    
    private static final int WHITE_START_ROW = 1;
    private static final int WHITE_END_ROW = 2;
    private static final int BLACK_START_ROW = 7;
    private static final int BLACK_END_ROW = 8;
    private static final int EMPTY_START_ROW = 3;
    private static final int EMPTY_END_ROW = 6;
  
    private ChessBoardFactory factory;

    @Before
    public void setUp() throws Exception {
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        factory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        board = new ChessBoard();
    }
    
    @Test
    public void testInitialse() {
        board.initialise();
        checkRows(BLACK_START_ROW, BLACK_END_ROW, Colour.BLACK);
        
        checkRowsForNull(EMPTY_START_ROW, EMPTY_END_ROW);
        
        checkRows(WHITE_START_ROW, WHITE_END_ROW, Colour.WHITE);
    }
    
    private void checkRow(int row, Colour colour) {
    	for (Coordinate coord : Coordinate.values()) {
            ChessPiece piece = board.getPieceFromBoardAt(coord.getIndex(), row);
            assertTrue(piece instanceof ChessPiece);
            assertEquals(piece.getColour(), colour);
        }
    }
    
    private void checkRows(int start, int end, Colour colour) {
    	for(int i = start; i <= end; i++) {
    		checkRow(i, colour);
    	}
    }
    
    private void checkRowForNull(int row) {
    	for (Coordinate coord : Coordinate.values()) {
            assertNull(board.getPieceFromBoardAt(coord.getIndex(), row));
        }
    }
    
    private void checkRowsForNull(int start, int end) {
    	for (int i = start; i <= end; i++) {
            checkRowForNull(i);
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
                    assertTrue("The two pieces should be the same", piece.equals(clonedPiece));
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
        board.putPieceOnBoardAt(KingPiece.getKingPiece(Colour.BLACK).moved(),
                StartingSquare.BLACK_KING.getLocation());
        board.putPieceOnBoardAt(KingPiece.getKingPiece(Colour.WHITE).moved(),
                StartingSquare.WHITE_KING.getLocation());
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

        assertTrue(new Location("E1").equals(blackKingLocation));
        assertTrue(new Location("A1").equals(whiteKingLocation));
    }

    @Test(expected = RuntimeException.class)
    public void getPlayersKingLocationFailTest() throws ParseException {
        ChessBoard board = factory.getChessBoard("ka1");
        board.getPlayersKingLocation(blackPlayer);
    }
}
