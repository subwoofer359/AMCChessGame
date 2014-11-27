package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.amc.game.chess.ChessBoard.Coordinate.*;

public class IsKingCheckmated {
    private Player whitePlayer;
    private Player blackPlayer;
    private ChessBoard board;
    private ChessGame chessGame;
    private ChessPiece whiteKing;
    private ChessPiece blackKing;
    @Before
    public void setUp() throws Exception {
        whitePlayer=new HumanPlayer("Teddy", Colour.WHITE);
        blackPlayer=new HumanPlayer("Robin", Colour.BLACK);
        whiteKing=new KingPiece(Colour.WHITE);
        blackKing=new KingPiece(Colour.BLACK);
        board=new ChessBoard(); 
        chessGame=new ChessGame(board, whitePlayer, blackPlayer);
    }

    @Test
    public void checkmateWithARookTest() {
        Location whiteKingLocation=new Location(F,5);
        Location blackKingLocation=new Location(H,5);
        Location whiteRookLocation=new Location(H,1);
       
        ChessPiece whiteRook=new RookPiece(Colour.WHITE);
        
        board.putPieceOnBoardAt(whiteKing, whiteKingLocation);
        board.putPieceOnBoardAt(blackKing, blackKingLocation);
        board.putPieceOnBoardAt(whiteRook, whiteRookLocation);
        
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
        board.putPieceOnBoardAt(new QueenPiece(Colour.BLACK),new Location(H,4));
        board.putPieceOnBoardAt(new PawnPiece(Colour.WHITE),new Location(G,4));
        board.putPieceOnBoardAt(new PawnPiece(Colour.WHITE),new Location(F,3));
        
        assertTrue(chessGame.isCheckMate(whitePlayer, board));    
    }
    
    @Test
    public void checkmateSupportMate(){
        board.putPieceOnBoardAt(new QueenPiece(Colour.WHITE),new Location(B,6));
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),new Location(A,6));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),new Location(C,6));
        
        assertTrue(chessGame.isCheckMate(blackPlayer, board));
        
    }
    
    @Test
    public void checkmateRightTriangleMate(){
        board.putPieceOnBoardAt(new QueenPiece(Colour.WHITE),new Location(H,1));
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),new Location(H,6));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),new Location(F,6));
        
        assertTrue(chessGame.isCheckMate(blackPlayer, board));
    }
    
    @Test
    public void checkmateOuterRowMate(){
        board.putPieceOnBoardAt(new QueenPiece(Colour.WHITE),new Location(H,5));
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),new Location(H,3));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),new Location(F,2));
        
        assertTrue(chessGame.isCheckMate(blackPlayer, board));
    }
    
    @Test
    public void checkmateWithTwoBishops(){
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),new Location(B,8));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),new Location(B,6));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE),new Location(B,7));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE),new Location(D,6));
        
        assertTrue(chessGame.isCheckMate(blackPlayer, board));
    }
    
    @Test
    public void notCheckMate(){
        board.initialise();
        assertFalse(chessGame.isCheckMate(whitePlayer, board));
        assertFalse(chessGame.isCheckMate(blackPlayer, board));
    }
    

}
