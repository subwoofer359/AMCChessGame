package org.amc.game.chess;

import static org.junit.Assert.*;
import static org.amc.game.chess.ChessBoard.Coordinate.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChessGameTest {

    private Player whitePlayer;
    private Player blackPlayer;
    private ChessBoard board;
    private ChessGame chessGame;
    private Location endLocation;
    private Location startLocation;
    
    @Before
    public void setUp() throws Exception {
        whitePlayer=new HumanPlayer("Teddy", Colour.WHITE);
        blackPlayer=new HumanPlayer("Robin", Colour.BLACK);
        board=new ChessBoard();
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE), StartingSquare.WHITE_KING.getLocation());
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK), StartingSquare.BLACK_KING.getLocation());
        chessGame=new ChessGame(board, whitePlayer, blackPlayer);
        startLocation = new Location(A, 8);
        endLocation = new Location(B, 7);
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
        assertTrue(chessGame.doesThePlayerStillHaveTheirKing(whitePlayer));
    }
    
    @Test
    public void testPlayerDoesNotHaveTheirKing(){
        board.removePieceOnBoardAt(StartingSquare.BLACK_KING.getLocation());
        assertTrue(chessGame.doesThePlayerStillHaveTheirKing(whitePlayer));
        assertFalse(chessGame.doesThePlayerStillHaveTheirKing(blackPlayer));
    }
    
    @Test
    public void isGameOverOnePlayerLosesTheirKing(){
        board.initialise();
        BishopPiece bishop=new BishopPiece(Colour.WHITE);
        
        assertFalse(chessGame.isGameOver(whitePlayer, blackPlayer));
        board.putPieceOnBoardAt(bishop,StartingSquare.BLACK_KING.getLocation());
        assertTrue(chessGame.isGameOver(whitePlayer, blackPlayer));
        
        board.initialise();
        bishop=new BishopPiece(Colour.BLACK);
        
        assertFalse(chessGame.isGameOver(whitePlayer, blackPlayer));
        board.putPieceOnBoardAt(bishop,StartingSquare.WHITE_KING.getLocation());
        assertTrue(chessGame.isGameOver(whitePlayer, blackPlayer));
       
    }
    

    
    @Test(expected = InvalidMoveException.class)
    public void testMoveWithAnEmptySquare()throws InvalidMoveException {
        chessGame.move(whitePlayer, new Move(startLocation, endLocation));
    }
    
    @Test(expected = InvalidMoveException.class)
    public void testPlayerCantMoveOtherPlayersPiece() throws InvalidMoveException {
        BishopPiece bishop = new BishopPiece(Colour.WHITE);
        board.putPieceOnBoardAt(bishop, startLocation);
        chessGame.move(blackPlayer, new Move(startLocation, new Location(B, 7)));
    }
    
    @Test
    public void testPlayerCanMoveTheirOwnPiece() throws InvalidMoveException {
        BishopPiece bishop = new BishopPiece(Colour.WHITE);
        board.putPieceOnBoardAt(bishop, startLocation);
        chessGame.move(whitePlayer, new Move(startLocation, endLocation));
        assertEquals(bishop, board.getPieceFromBoardAt(endLocation));
        assertNull(board.getPieceFromBoardAt(startLocation));
    }
    
    @Test
    public void doesGameRuleApply(){
        RookPiece rook = new RookPiece(Colour.WHITE);
        Location rookStartPosition = new Location(H,1);
        Move move=new Move(StartingSquare.WHITE_KING.getLocation(),new Location(G,1));
        board.putPieceOnBoardAt(rook, rookStartPosition);
        assertTrue(chessGame.doesAGameRuleApply(board, move));
    }
    
    @Test
    public void doesNotGameRuleApply(){
        RookPiece rook = new RookPiece(Colour.WHITE);
        Location rookStartPosition = new Location(H,1);
        Move move=new Move(StartingSquare.WHITE_KING.getLocation(),new Location(F,1));
        board.putPieceOnBoardAt(rook, rookStartPosition);
        assertFalse(chessGame.doesAGameRuleApply(board, move));
    }
    
    @Test
    public void gameRuleApplied() throws InvalidMoveException{
        RookPiece rook = new RookPiece(Colour.WHITE);
        Location rookStartPosition = new Location(H,1);
        Move move=new Move(StartingSquare.WHITE_KING.getLocation(),new Location(F,1));
        board.putPieceOnBoardAt(rook, rookStartPosition);
        chessGame.move(whitePlayer, move);
    }
}
