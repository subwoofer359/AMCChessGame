package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;
import static org.junit.Assert.*;

import org.amc.game.chess.view.ChessBoardView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ChessGameCastlingKingInCheck {
    private static Player whitePlayer = new HumanPlayer("Teddy", Colour.WHITE);
    private static Player blackPlayer = new HumanPlayer("Robin", Colour.BLACK);
    private Player currentPlayer;
    private ChessPiece attackingPiece;
    private Move defendingChessPieceMove;
    private ChessBoard board;
    private ChessGame chessGame;
    private ChessPiece kingPiece;
    private ChessPiece rookPiece;
    private ChessBoardView view;
    
    public ChessGameCastlingKingInCheck(Player currentPlayer,Move defendingChessPieceMove) {
        this.currentPlayer=currentPlayer;
        this.defendingChessPieceMove=defendingChessPieceMove;
    }
    
    @Before
    public void setUp() throws Exception {
        board=new ChessBoard();
        view=new ChessBoardView(board);
        chessGame=new ChessGame(board,whitePlayer,blackPlayer);
        attackingPiece=new BishopPiece(Colour.BLACK);
        board.putPieceOnBoardAt(attackingPiece, new Location(E,3));
        kingPiece=new KingPiece(Colour.WHITE);
        rookPiece=new RookPiece(Colour.WHITE);
        board.putPieceOnBoardAt(kingPiece, StartingSquare.WHITE_KING.getLocation());
        board.putPieceOnBoardAt(rookPiece, StartingSquare.WHITE_ROOK_RIGHT.getLocation());
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK), StartingSquare.BLACK_KING.getLocation());
    }
    
    @Parameters
    public static Collection<?> addedChessPieces(){
        
        return Arrays.asList(new Object[][]{
                        {whitePlayer,new Move(StartingSquare.WHITE_KING.getLocation(),new Location(G,1))}
                        });
        
    }
    
    
    @Test(expected=IllegalMoveException.class)
    public void testInvalidMoveExceptionThrown() throws IllegalMoveException{
        chessGame.move(currentPlayer, defendingChessPieceMove);
        assertEquals(kingPiece,board.getPieceFromBoardAt(StartingSquare.WHITE_KING.getLocation()));
        assertEquals(rookPiece,board.getPieceFromBoardAt(StartingSquare.WHITE_ROOK_RIGHT.getLocation()));
    }
    
    @Test
    public void testPiecesAreReturnedToStartPositions(){
        try{
            chessGame.move(currentPlayer, defendingChessPieceMove);
        }catch(IllegalMoveException ime){
            
        }
        assertEquals(kingPiece,board.getPieceFromBoardAt(StartingSquare.WHITE_KING.getLocation()));
        assertEquals(rookPiece,board.getPieceFromBoardAt(StartingSquare.WHITE_ROOK_RIGHT.getLocation()));
    }
}
