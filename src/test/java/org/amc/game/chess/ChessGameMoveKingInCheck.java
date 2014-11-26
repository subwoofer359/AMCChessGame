package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ChessGameMoveKingInCheck {
    private static Player whitePlayer = new HumanPlayer("Teddy", Colour.WHITE);
    private static Player blackPlayer = new HumanPlayer("Robin", Colour.BLACK);
    private Player currentPlayer;
    private ChessPiece attackingPiece;
    private Move defendingChessPieceMove;
    private ChessBoard board;
    private ChessGame chessGame;
    
    public ChessGameMoveKingInCheck(Player currentPlayer,Move defendingChessPieceMove) {
        this.currentPlayer=currentPlayer;
        this.defendingChessPieceMove=defendingChessPieceMove;
    }
    
    @Before
    public void setUp() throws Exception {
        board=new ChessBoard();
        chessGame=new ChessGame(board,whitePlayer,blackPlayer);
        attackingPiece=new BishopPiece(Colour.BLACK);
        board.putPieceOnBoardAt(attackingPiece, new Location(H,4));
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE), StartingSquare.WHITE_KING.getLocation());
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK), StartingSquare.BLACK_KING.getLocation());
    }
    
    @Parameters
    public static Collection<?> addedChessPieces(){
        
        return Arrays.asList(new Object[][]{
                        {whitePlayer,new Move(StartingSquare.WHITE_KING.getLocation(),new Location(F,2))}
                        });
        
    }
    
    
    @Test(expected=InvalidMoveException.class)
    public void test() throws InvalidMoveException{
        ChessPiece piece=board.getPieceFromBoardAt(defendingChessPieceMove.getEnd());
        ChessPiece kingPiece=board.getPieceFromBoardAt(defendingChessPieceMove.getStart());
        chessGame.move(currentPlayer, defendingChessPieceMove);
        assertEquals(piece,board.getPieceFromBoardAt(defendingChessPieceMove.getEnd()));
        assertEquals(kingPiece,board.getPieceFromBoardAt(defendingChessPieceMove.getStart()));
    }
    
    @Test(expected=InvalidMoveException.class)
    public void testCaptureIntoCheck() throws InvalidMoveException{
        ChessPiece pawn=new PawnPiece(Colour.BLACK);
        board.putPieceOnBoardAt(pawn, defendingChessPieceMove.getEnd());
        ChessPiece kingPiece=board.getPieceFromBoardAt(defendingChessPieceMove.getStart());
        chessGame.move(currentPlayer, defendingChessPieceMove);
        assertEquals(pawn,board.getPieceFromBoardAt(defendingChessPieceMove.getEnd()));
        assertEquals(kingPiece,board.getPieceFromBoardAt(defendingChessPieceMove.getStart()));
    }
}
