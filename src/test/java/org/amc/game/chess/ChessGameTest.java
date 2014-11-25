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
        KingPiece kingWhite=new KingPiece(Colour.WHITE);
        board.putPieceOnBoardAt(kingWhite, new Location(D,4));
        assertTrue(chessGame.doesThePlayerStillHaveTheirKing(whitePlayer));
    }
    
    @Test
    public void testPlayerDoesNotHaveTheirKing(){
        KingPiece kingWhite=new KingPiece(Colour.WHITE);
        board.putPieceOnBoardAt(kingWhite, new Location(D,4));
        assertTrue(chessGame.doesThePlayerStillHaveTheirKing(whitePlayer));
        assertFalse(chessGame.doesThePlayerStillHaveTheirKing(blackPlayer));
    }
    
    @Test
    public void isGameOverOnePlayerLosesTheirKing(){
        board.initialise();
        BishopPiece bishop=new BishopPiece(Colour.WHITE);
        
        assertFalse(chessGame.isGameOver(whitePlayer, blackPlayer));
        board.putPieceOnBoardAt(bishop,new Location(E,8));
        assertTrue(chessGame.isGameOver(whitePlayer, blackPlayer));
        
        board.initialise();
        bishop=new BishopPiece(Colour.BLACK);
        
        assertFalse(chessGame.isGameOver(whitePlayer, blackPlayer));
        board.putPieceOnBoardAt(bishop,new Location(E,1));
        assertTrue(chessGame.isGameOver(whitePlayer, blackPlayer));
       
    }
    
    @Test
    public void TestWhiteEnPassantCapture()throws InvalidMoveException{
        PawnPiece whitePawn=new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn=new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition=new Location(E,5);
        Location whitePawnEndPosition=new Location(F,6);
        Location blackPawnStartPosition=new Location(F,7);
        Location blackPawnEndPosition=new Location(F,5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);
        
        Move blackMove =new Move(blackPawnStartPosition,blackPawnEndPosition);
        Move whiteEnPassantMove =new Move(whitePawnStartPosition,new Location(F,6));
        
        board.allGameMoves.add(blackMove);
        
        chessGame.move(whitePlayer, whiteEnPassantMove);
        assertTrue(board.getPieceFromBoardAt(whitePawnEndPosition).equals(whitePawn));
        assertNull(board.getPieceFromBoardAt(blackPawnEndPosition));
    }
    
    @Test
    public void TestBlackEnPassantCapture()throws InvalidMoveException{
        PawnPiece whitePawn=new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn=new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition=new Location(F,2);
        Location whitePawnEndPosition=new Location(F,4);
        Location blackPawnStartPosition=new Location(G,4);
        Location blackPawnEndPosition=new Location(F,3);
        board.putPieceOnBoardAt(blackPawn, blackPawnStartPosition);
        board.putPieceOnBoardAt(whitePawn, whitePawnEndPosition);
        
        Move blackEnPassantMove =new Move(blackPawnStartPosition,blackPawnEndPosition);
        Move whiteMove =new Move(whitePawnStartPosition,whitePawnEndPosition);
        
        board.allGameMoves.add(whiteMove);
        
        chessGame.move(blackPlayer, blackEnPassantMove);
        assertTrue(board.getPieceFromBoardAt(blackPawnEndPosition).equals(blackPawn));
        assertNull(board.getPieceFromBoardAt(whitePawnEndPosition));
    }
    
    @Test
    public void notMoveEnPassantCapture() {
        BishopPiece bishop=new BishopPiece(Colour.WHITE);
        Location startSquare=new Location(A,2);
        Location endSquare=new Location(B,3);
        Move move =new Move(startSquare,endSquare);
        
        board.putPieceOnBoardAt(bishop,startSquare);
        EnPassantRule rule =new EnPassantRule();
        
        assertFalse(rule.isMoveEnPassantCapture(board,move));
        
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
        KingPiece king =new KingPiece(Colour.WHITE);
        RookPiece rook = new RookPiece(Colour.WHITE);
        Location kingStartPosition = new Location(E,1);
        Location rookStartPosition = new Location(H,1);
        Move move=new Move(kingStartPosition,new Location(G,1));
        board.putPieceOnBoardAt(king, kingStartPosition);
        board.putPieceOnBoardAt(rook, rookStartPosition);
        assertTrue(chessGame.doesAGameRuleApply(board, move));
    }
    
    @Test
    public void doesNotGameRuleApply(){
        KingPiece king =new KingPiece(Colour.WHITE);
        RookPiece rook = new RookPiece(Colour.WHITE);
        Location kingStartPosition = new Location(E,1);
        Location rookStartPosition = new Location(H,1);
        Move move=new Move(kingStartPosition,new Location(F,1));
        board.putPieceOnBoardAt(king, kingStartPosition);
        board.putPieceOnBoardAt(rook, rookStartPosition);
        assertFalse(chessGame.doesAGameRuleApply(board, move));
    }
    
    @Test
    public void gameRuleApplied() throws InvalidMoveException{
        KingPiece king =new KingPiece(Colour.WHITE);
        RookPiece rook = new RookPiece(Colour.WHITE);
        Location kingStartPosition = new Location(E,1);
        Location rookStartPosition = new Location(H,1);
        Move move=new Move(kingStartPosition,new Location(F,1));
        board.putPieceOnBoardAt(king, kingStartPosition);
        board.putPieceOnBoardAt(rook, rookStartPosition);
        chessGame.move(whitePlayer, move);
    }
}
