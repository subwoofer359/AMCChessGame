package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.amc.game.chess.ChessBoard.Coordinate.*;

public class BugCG24PawnCantMoveTwoSquares {
    private Player whitePlayer;
    private Player blackPlayer;
    private ChessBoard board;
    private ChessGame game;
    @Before
    public void setUp() throws Exception {
        whitePlayer=new HumanPlayer("Teddy", Colour.WHITE);
        blackPlayer=new HumanPlayer("Robin", Colour.BLACK);
        board=new ChessBoard();
        SetupChessBoard.setUpChessBoardToDefault(board);
        game=new ChessGame(board, whitePlayer, blackPlayer);
        
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws InvalidMoveException{
        Move firstMove =new Move(new Location(H,2),new Location(H,4));
        game.move(whitePlayer, firstMove);
        Move secondMoveThatFails =new Move(new Location(A,7),new Location(A,5));
        game.move(blackPlayer, secondMoveThatFails);
    }

}