package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class IsKingNotInCheckTest {

    private Player whitePlayer;
    private Player blackPlayer;
    private ChessPiece attackingChessPiece;
    private Location attackingChessPieceLocation;
    
    public IsKingNotInCheckTest(ChessPiece attackingChessPiece,Location attackingChessPieceLocation) {
        this.attackingChessPiece=attackingChessPiece;
        this.attackingChessPieceLocation=attackingChessPieceLocation;
    }
    
    @Parameters
    public static Collection<?> addedChessPieces(){
        
        return Arrays.asList(new Object[][]{
                        {new BishopPiece(Colour.BLACK), new Location(A,2)},
                        {new PawnPiece(Colour.BLACK), new Location(E,2)},
                        {new BishopPiece(Colour.WHITE), new Location(A,2)}
                        });
        
    }
    
    @Before
    public void setUp() throws Exception {
        whitePlayer=new HumanPlayer("Teddy", Colour.WHITE);
        blackPlayer=new HumanPlayer("Robin", Colour.BLACK);
    }

    @Test
    public void testKingIsNotChecked() {
        ChessBoard board=new ChessBoard();
        ChessPiece whiteKing=new KingPiece(Colour.WHITE);
        ChessPiece blackKing=new KingPiece(Colour.BLACK);
        board.putPieceOnBoardAt(whiteKing, StartingSquare.WHITE_KING.getLocation());
        board.putPieceOnBoardAt(blackKing, StartingSquare.BLACK_KING.getLocation());
        board.putPieceOnBoardAt(attackingChessPiece, attackingChessPieceLocation);
        ChessGame chessGame=new ChessGame(board,whitePlayer, blackPlayer);
        assertFalse(chessGame.isPlayersKingInCheck(whitePlayer,board));
    }
}
