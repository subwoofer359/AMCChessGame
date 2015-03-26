package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.D;
import static org.amc.game.chess.ChessBoard.Coordinate.H;
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ChessGameMoveKingOutOfCheck {
    private static Player whitePlayer = new HumanPlayer("Teddy", Colour.WHITE);
    private static Player blackPlayer = new HumanPlayer("Robin", Colour.BLACK);
    private Player currentPlayer;
    private ChessPiece attackingPiece;
    private Move defendingChessPieceMove;
    private ChessBoard board;
    private ChessGame chessGame;
    
    public ChessGameMoveKingOutOfCheck(Player currentPlayer,Move defendingChessPieceMove) {
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
                        {whitePlayer,new Move(StartingSquare.WHITE_KING.getLocation(),new Location(D,2))}
                        });
        
    }
    
    @Test
    public void kingMovesOutOfCheckTest() throws IllegalMoveException{
        ChessPiece kingPiece=board.getPieceFromBoardAt(defendingChessPieceMove.getStart());
        chessGame.move(currentPlayer, defendingChessPieceMove);
        assertEquals(kingPiece,board.getPieceFromBoardAt(defendingChessPieceMove.getEnd()));
        assertNull(board.getPieceFromBoardAt(defendingChessPieceMove.getStart()));
    }
}