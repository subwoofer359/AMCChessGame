package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerChessGameTest {
    private Player player;
    private Player opponent;
    
    @Before
    public void setUp() throws Exception {
        player=new HumanPlayer("Ted", Colour.BLACK);
        opponent=new HumanPlayer("Chris",Colour.WHITE);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInitialState() {
        ServerChessGame game=new ServerChessGame(player);
        assertEquals(game.getPlayer(),player);
        assertNull(game.getChessGame());
        assertEquals(ServerChessGame.status.AWAITING_PLAYER,game.getCurrentStatus());
        assertEquals(Colour.WHITE,player.getColour());
    }
    
    @Test
    public void testAddOpponent() {
        ServerChessGame game=new ServerChessGame(player);
        game.addOpponent(opponent);
        assertEquals(game.getPlayer(),player);
        assertNotNull(game.getChessGame());
        assertEquals(ServerChessGame.status.IN_PROGRESS,game.getCurrentStatus());
        assertEquals(Colour.BLACK,opponent.getColour());
    }
    
    @Test
    public void testAddOpponentToFinishedGame(){
        ServerChessGame game=new ServerChessGame(player);
        game.setCurrentStatus(ServerChessGame.status.FINISHED);
        game.addOpponent(opponent);
        assertEquals(game.getPlayer(),player);
        assertNull(game.getChessGame());
        assertEquals(ServerChessGame.status.FINISHED,game.getCurrentStatus());
        assertEquals(Colour.WHITE,opponent.getColour());
    }

}
