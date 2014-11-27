package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static org.amc.game.chess.ChessBoard.Coordinate.*;

public class IsKingCheckmated {
    private Player whitePlayer;
    private Player blackPlayer;
    private ChessBoard board;
    private ChessGame chessGame;
    private ChessPiece whiteKing;
    private ChessPiece blackKing;
    private PlayersKingCheckmated checkmateCondtion;
    @Before
    public void setUp() throws Exception {
        whitePlayer=new HumanPlayer("Teddy", Colour.WHITE);
        blackPlayer=new HumanPlayer("Robin", Colour.BLACK);
        whiteKing=new KingPiece(Colour.WHITE);
        blackKing=new KingPiece(Colour.BLACK);
        board=new ChessBoard(); 
        chessGame=new ChessGame(board, whitePlayer, blackPlayer);
        checkmateCondtion=new PlayersKingCheckmated();
    }

    @Test
    public void checkmateWithARookTest() {
        Location whiteKingLocation=new Location(F,5);
        Location blackKingLocation=new Location(H,5);
        Location whiteRookLocation=new Location(A,1);
       
        ChessPiece whiteRook=new RookPiece(Colour.WHITE);
        
        board.putPieceOnBoardAt(whiteKing, whiteKingLocation);
        board.putPieceOnBoardAt(blackKing, blackKingLocation);
        board.putPieceOnBoardAt(whiteRook, whiteRookLocation);
        Move rookMove=new Move(new Location(A,1),new Location(H,1));
        board.move(rookMove);
        
        assertTrue(chessGame.isCheckMate(blackPlayer, board));
    }
    
    @Test
    public void checkmateFoolsMate(){
        board.initialise();
        board.removePieceOnBoardAt(new Location(D,8));
        board.removePieceOnBoardAt(new Location(E,7));
        board.removePieceOnBoardAt(new Location(F,2));
        board.removePieceOnBoardAt(new Location(G,2));
        board.putPieceOnBoardAt(new PawnPiece(Colour.BLACK),new Location(E,5));
        board.putPieceOnBoardAt(new QueenPiece(Colour.BLACK),new Location(D,8));
        board.putPieceOnBoardAt(new PawnPiece(Colour.WHITE),new Location(G,4));
        board.putPieceOnBoardAt(new PawnPiece(Colour.WHITE),new Location(F,3));
        Move queenMove=new Move(new Location(D,8),new Location(H,4));
        board.move(queenMove);
        
        assertTrue(chessGame.isCheckMate(whitePlayer, board));    
    }
    
    @Test
    public void checkmateSupportMate(){
        board.putPieceOnBoardAt(new QueenPiece(Colour.WHITE),new Location(B,1));
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),new Location(A,6));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),new Location(C,6));
        Move queenMove=new Move(new Location(B,1),new Location(B,6));
        board.move(queenMove);
        assertTrue(chessGame.isCheckMate(blackPlayer, board));
    }
    
    @Test
    public void notCheckmateSupportMate(){
        board.putPieceOnBoardAt(new QueenPiece(Colour.WHITE),new Location(B,1));
        Move move=new Move(new Location(B,1),new Location(B,6));
        board.move(move);
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),new Location(C,6));
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),new Location(A,6));
        board.putPieceOnBoardAt(new BishopPiece(Colour.BLACK),new Location(C,7));
        
        assertFalse(chessGame.isCheckMate(blackPlayer, board));
        
    }
    
    @Test
    public void checkmateRightTriangleMate(){
        board.putPieceOnBoardAt(new QueenPiece(Colour.WHITE),new Location(A,1));
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),new Location(H,6));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),new Location(F,6));
        Move queenMove=new Move(new Location(A,1),new Location(H,1));
        board.move(queenMove);
        
        assertTrue(chessGame.isCheckMate(blackPlayer, board));
    }
    
    @Test
    public void checkmateOuterRowMate(){
        board.putPieceOnBoardAt(new QueenPiece(Colour.WHITE),new Location(A,5));
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),new Location(H,3));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),new Location(F,2));
        Move queenMove=new Move(new Location(A,5),new Location(H,5));
        board.move(queenMove);
        assertTrue(chessGame.isCheckMate(blackPlayer, board));
    }
    
    @Test
    public void notCheckmateOuterRowMate(){
        board.putPieceOnBoardAt(new QueenPiece(Colour.WHITE),new Location(H,4));
        board.move(new Move(new Location(H,4),new Location(H,5)));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),new Location(F,2));
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),new Location(H,3));
        board.putPieceOnBoardAt(new RookPiece(Colour.BLACK),new Location(A,4));
        
        assertFalse(chessGame.isCheckMate(blackPlayer, board));
    }
    
    @Test
    public void checkmateWithTwoBishops(){
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),new Location(B,8));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),new Location(B,6));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE),new Location(B,7));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE),new Location(E,7));
        Move move =new Move(new Location(E,7),new Location(D,6));
        board.move(move);
        
        assertTrue(chessGame.isCheckMate(blackPlayer, board));
    }
    
    @Test
    public void checkmateWithTwoBishopsCanBeBlocked(){
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),new Location(B,8));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),new Location(B,6));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE),new Location(B,7));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE),new Location(E,7));
        board.putPieceOnBoardAt(new RookPiece(Colour.BLACK),new Location(H,7));
        
        Move move =new Move(new Location(E,7),new Location(D,6));
        board.move(move);
        
        assertFalse(chessGame.isCheckMate(blackPlayer, board));
        assertTrue(checkmateCondtion.canAttackingPieceBeBlocked(blackPlayer, whitePlayer,board));
    }
    
    @Test
    public void checkmateWithTwoBishopsCanBeCaptured(){
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),new Location(B,8));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),new Location(B,6));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE),new Location(B,7));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE),new Location(E,7));
        board.putPieceOnBoardAt(new RookPiece(Colour.BLACK),new Location(H,6));
        
        Move move =new Move(new Location(E,7),new Location(D,6));
        board.move(move);
       
        assertTrue(checkmateCondtion.canAttackingPieceBeCaptured(blackPlayer,whitePlayer, board));
        assertFalse(chessGame.isCheckMate(blackPlayer, board));
    }
    
    @Test
    public void notCheckMate(){
        board.initialise();
        assertFalse(chessGame.isCheckMate(whitePlayer, board));
        assertFalse(chessGame.isCheckMate(blackPlayer, board));
    }
    
    @Test
    public void testCannotAttackingPieceBeBlocked(){
        board.putPieceOnBoardAt(new QueenPiece(Colour.WHITE),new Location(H,8));
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),new Location(H,3));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),new Location(F,2));
        Move move =new Move(new Location(H,8),new Location(H,5));
        board.move(move);
        assertFalse(checkmateCondtion.canAttackingPieceBeBlocked(blackPlayer,whitePlayer, board));
    }
    
    @Test
    public void testCanAttackingPieceBeBlocked(){
        board.putPieceOnBoardAt(new QueenPiece(Colour.WHITE),new Location(H,8));
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),new Location(H,3));
        board.putPieceOnBoardAt(new RookPiece(Colour.BLACK),new Location(A,4));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),new Location(F,2));
        Move move =new Move(new Location(H,8),new Location(H,5));
        board.move(move);
        assertTrue(checkmateCondtion.canAttackingPieceBeBlocked(blackPlayer, whitePlayer,board));
    }
    

}
