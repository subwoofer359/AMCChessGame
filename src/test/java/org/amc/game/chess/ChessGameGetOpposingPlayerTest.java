package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chessserver.ServerChessGame;
import org.junit.Before;
import org.junit.Test;

public class ChessGameGetOpposingPlayerTest {
    
    private ChessGamePlayer whitePlayer = new ChessGamePlayer(new HumanPlayer("Stephen"),
                    Colour.WHITE);

    private ChessGamePlayer blackPlayer = new ChessGamePlayer(new HumanPlayer("Chris"),
                    Colour.BLACK);

    private ChessGame chessGame;
    private ServerChessGame scg;
    
    @Before
    public void setUp() throws Exception {
        ChessBoard board = new ChessBoard();
        SetupChessBoard.setUpChessBoardToDefault(board);
        chessGame = new ChessGame(board, whitePlayer, blackPlayer);
        
        scg = new ServerChessGame(2030L, whitePlayer);
        scg.addOpponent(blackPlayer);
       
    }
    
    @Test
    public void testChessGame() {
        assertEquals(blackPlayer, chessGame.getOpposingPlayer(whitePlayer));
    }
    
    @Test
    public void testServerChessGame() {
        assertTrue(ComparePlayers.comparePlayers(blackPlayer, scg.getChessGame().getOpposingPlayer(whitePlayer)));
    }

}
