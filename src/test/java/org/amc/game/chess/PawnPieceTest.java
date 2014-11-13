package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PawnPieceTest  {
    private ChessBoard board;
    private Location testWhiteStartPosition = new Location(ChessBoard.Coordinate.F, 2);
    private Location testBlackStartPosition = new Location(ChessBoard.Coordinate.F, 7);
    
    private static final Location[] invalidWhiteMovesFromF2={
        new Location(ChessBoard.Coordinate.F,1),
        new Location(ChessBoard.Coordinate.E,1),
        new Location(ChessBoard.Coordinate.E,2),
        new Location(ChessBoard.Coordinate.G,2),
        new Location(ChessBoard.Coordinate.G,1),
        new Location(ChessBoard.Coordinate.E,4),
        new Location(ChessBoard.Coordinate.G,4),
        new Location(ChessBoard.Coordinate.F,5),
        new Location(ChessBoard.Coordinate.D,4),
        new Location(ChessBoard.Coordinate.H,4),
        new Location(ChessBoard.Coordinate.H,1)
    };
    
    private static final Location[] invalidBlackMovesFromF7={
        new Location(ChessBoard.Coordinate.F,8),
        new Location(ChessBoard.Coordinate.E,8),
        new Location(ChessBoard.Coordinate.E,7),
        new Location(ChessBoard.Coordinate.G,7),
        new Location(ChessBoard.Coordinate.G,8),
        new Location(ChessBoard.Coordinate.E,5),
        new Location(ChessBoard.Coordinate.G,5),
        new Location(ChessBoard.Coordinate.F,4),
        new Location(ChessBoard.Coordinate.D,5),
        new Location(ChessBoard.Coordinate.H,5),
        new Location(ChessBoard.Coordinate.H,8)
    };
    
    @Before
    public void setUp() throws Exception {
        board = new ChessBoard();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testOnEmptyBoardIsValidWhiteMove() {
        PawnPiece pawn=new PawnPiece(Colour.WHITE);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        
        assertTrue(pawn.isValidMove(board, new Move(testWhiteStartPosition,
                        new Location(ChessBoard.Coordinate.F,3))));
        assertTrue(pawn.isValidMove(board, new Move(testWhiteStartPosition,
                        new Location(ChessBoard.Coordinate.F,4))));
        
    }
    
    @Test
    public void testOnEmptyBoardIsValidBlackMove() {
        PawnPiece pawn=new PawnPiece(Colour.BLACK);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        
        assertTrue(pawn.isValidMove(board, new Move(testBlackStartPosition,
                        new Location(ChessBoard.Coordinate.F,6))));
        assertTrue(pawn.isValidMove(board, new Move(testBlackStartPosition,
                        new Location(ChessBoard.Coordinate.F,5))));
        
    }

    @Test
    public void testOnEmptyBoardIsNotValidWhiteMove() {
        PawnPiece pawn=new PawnPiece(Colour.WHITE);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        
        for(Location endLocation:invalidWhiteMovesFromF2){
            assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition,endLocation)));
        }
        
    }
    
    @Test
    public void testOnEmptyBoardIsNotValidBlackMove() {
        PawnPiece pawn=new PawnPiece(Colour.BLACK);
        board.putPieceOnBoardAt(pawn, testBlackStartPosition);
        
        for(Location endLocation:invalidBlackMovesFromF7){
            assertFalse(pawn.isValidMove(board, new Move(testBlackStartPosition,endLocation)));
        }
        
    }
    

    @Test
    public void testOnEmptyBoardIsNotValidNonIntialWhiteMove() {
        PawnPiece pawn=new PawnPiece(Colour.WHITE);
        pawn.moved();
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        Location endLocation=new Location(Coordinate.F,4);
        assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition,endLocation)));
    }
    
    @Test
    public void testOnEmptyBoardIsNotValidNonIntialBlackMove() {
        PawnPiece pawn=new PawnPiece(Colour.BLACK);
        pawn.moved();
        board.putPieceOnBoardAt(pawn, testBlackStartPosition);
        Location endLocation=new Location(Coordinate.F,4);
        assertFalse(pawn.isValidMove(board, new Move(testBlackStartPosition,endLocation)));
    }
    
    @Test
    public void testOnBoardIsValidWhiteCapture() {
        PawnPiece pawn=new PawnPiece(Colour.WHITE);
        PawnPiece enemyPawn=new PawnPiece(Colour.BLACK);
        Location captureLocationOne= new Location(Coordinate.E,3);
        Location captureLocationTwo= new Location(Coordinate.G,3);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        board.putPieceOnBoardAt(enemyPawn,captureLocationOne);
        board.putPieceOnBoardAt(enemyPawn, captureLocationTwo);
        assertTrue(pawn.isValidMove(board, new Move(testWhiteStartPosition,captureLocationOne)));
        assertTrue(pawn.isValidMove(board, new Move(testWhiteStartPosition,captureLocationTwo)));
    }
    
    @Test
    public void testOnBoardIsValidBlackCapture() {
        PawnPiece pawn=new PawnPiece(Colour.BLACK);
        PawnPiece enemyPawn=new PawnPiece(Colour.WHITE);
        Location captureLocationOne= new Location(Coordinate.E,6);
        Location captureLocationTwo= new Location(Coordinate.G,6);
        board.putPieceOnBoardAt(pawn, testBlackStartPosition);
        board.putPieceOnBoardAt(enemyPawn,captureLocationOne);
        board.putPieceOnBoardAt(enemyPawn, captureLocationTwo);
        assertTrue(pawn.isValidMove(board, new Move(testBlackStartPosition,captureLocationOne)));
        assertTrue(pawn.isValidMove(board, new Move(testBlackStartPosition,captureLocationTwo)));
    }
    
    @Test
    public void testOnBoardInvalidWhiteCapture() {
        PawnPiece pawn=new PawnPiece(Colour.WHITE);
        PawnPiece enemyPawn=new PawnPiece(Colour.WHITE);
        Location captureLocationOne= new Location(Coordinate.E,3);
        Location captureLocationTwo= new Location(Coordinate.G,3);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        board.putPieceOnBoardAt(enemyPawn,captureLocationOne);
        assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition,captureLocationOne)));
        assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition,captureLocationTwo)));
    }
    
    @Test
    public void testOnBoardInvalidBlackCapture() {
        PawnPiece pawn=new PawnPiece(Colour.BLACK);
        PawnPiece enemyPawn=new PawnPiece(Colour.BLACK);
        Location captureLocationOne= new Location(Coordinate.E,6);
        Location captureLocationTwo= new Location(Coordinate.G,6);
        board.putPieceOnBoardAt(pawn, testBlackStartPosition);
        board.putPieceOnBoardAt(enemyPawn,captureLocationOne);
        assertFalse(pawn.isValidMove(board, new Move(testBlackStartPosition,captureLocationOne)));
        assertFalse(pawn.isValidMove(board, new Move(testBlackStartPosition,captureLocationTwo)));
    }

    @Test
    public void testOnBoardIsNotValidBlackMove() {
        PawnPiece pawn=new PawnPiece(Colour.BLACK);
        PawnPiece enemyPawn=new PawnPiece(Colour.WHITE);
        board.putPieceOnBoardAt(pawn, testBlackStartPosition);
        Location endLocationOne=new Location(Coordinate.F, 6);
        Location endLocationTwo=new Location(Coordinate.F, 5);
        
        board.putPieceOnBoardAt(enemyPawn,endLocationOne );
        
        assertFalse(pawn.isValidMove(board, new Move(testBlackStartPosition,endLocationOne)));
        assertFalse(pawn.isValidMove(board, new Move(testBlackStartPosition,endLocationTwo)));
    }
    
    @Test
    public void testOnBoardIsNotValidWhiteMove() {
        PawnPiece pawn=new PawnPiece(Colour.WHITE);
        PawnPiece enemyPawn=new PawnPiece(Colour.BLACK);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        Location endLocationOne=new Location(Coordinate.F, 3);
        Location endLocationTwo=new Location(Coordinate.F, 4);
        
        board.putPieceOnBoardAt(enemyPawn,endLocationOne );
        
        assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition,endLocationOne)));
        assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition,endLocationTwo)));
    }
    
    //----------------------en passant capture-------------------------------------
    
    @Test
    public void testIsEnPassantCapture(){
        PawnPiece whitePawn=new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn=new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition=new Location(Coordinate.E,5);
        Location blackPawnStartPosition=new Location(Coordinate.F,7);
        Location blackPawnEndPosition=new Location(Coordinate.F,5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);
        
        Move blackMove =new Move(blackPawnStartPosition,blackPawnEndPosition);
        Move whiteEnPassantMove =new Move(whitePawnStartPosition,new Location(Coordinate.F,6));
        
        board.allGameMoves.add(blackMove);
        assertTrue(whitePawn.isEnPassantCapture(board, whiteEnPassantMove));
    }
    
    @Test
    public void testIsNotEnPassantCapture(){
        PawnPiece whitePawn=new PawnPiece(Colour.WHITE);
        BishopPiece blackBishop=new BishopPiece(Colour.BLACK);
        Location whitePawnStartPosition=new Location(Coordinate.E,5);
        Location blackPawnStartPosition=new Location(Coordinate.F,7);
        Location blackPawnEndPosition=new Location(Coordinate.F,5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackBishop, blackPawnEndPosition);
        
        Move blackMove =new Move(blackPawnStartPosition,blackPawnEndPosition);
        Move whiteEnPassantMove =new Move(whitePawnStartPosition,new Location(Coordinate.F,6));
        
        board.allGameMoves.add(blackMove);
        assertFalse(whitePawn.isEnPassantCapture(board, whiteEnPassantMove));
    }
    
    @Test
    public void testIsNotEnPassantCaptureMoveLessTwo(){
        PawnPiece whitePawn=new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn=new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition=new Location(Coordinate.E,5);
        Location blackPawnStartPosition=new Location(Coordinate.F,7);
        Location blackPawnEndPosition=new Location(Coordinate.F,6);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);
        
        Move blackMove =new Move(blackPawnStartPosition,blackPawnEndPosition);
        Move whiteEnPassantMove =new Move(whitePawnStartPosition,new Location(Coordinate.F,6));
        
        board.allGameMoves.add(blackMove);
        assertFalse(whitePawn.isEnPassantCapture(board, whiteEnPassantMove));
    }
    
    @Test
    public void testIsNotEnPassantCaptureAfterTwoSquareMove(){
        PawnPiece whitePawn=new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn=new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition=new Location(Coordinate.E,5);
        Location blackPawnStartPosition=new Location(Coordinate.F,7);
        Location blackPawnEndPosition=new Location(Coordinate.F,5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);
        
        Move blackMove =new Move(blackPawnStartPosition,blackPawnEndPosition);
        Move whiteEnPassantMove =new Move(whitePawnStartPosition,new Location(Coordinate.D,6));
        
        board.allGameMoves.add(blackMove);
        assertFalse(whitePawn.isEnPassantCapture(board, whiteEnPassantMove));
    }
    
    @Test
    public void testIsNotEnPassantCaptureAfterTwoSeparateSquareMove(){
        PawnPiece whitePawn=new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn=new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition=new Location(Coordinate.E,5);
        Location blackPawnStartPosition=new Location(Coordinate.F,6);
        Location blackPawnEndPosition=new Location(Coordinate.F,5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);
        
        Move blackMove =new Move(blackPawnStartPosition,blackPawnEndPosition);
        Move whiteEnPassantMove =new Move(whitePawnStartPosition,new Location(Coordinate.D,6));
        
        board.allGameMoves.add(blackMove);
        assertFalse(whitePawn.isEnPassantCapture(board, whiteEnPassantMove));
    }
    
}
