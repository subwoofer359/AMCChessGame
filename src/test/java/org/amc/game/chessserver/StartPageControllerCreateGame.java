package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StartPageControllerCreateGame {
    private Model model;
    private ConcurrentMap<Long, ServerChessGame> gameMap;
    private ChessGamePlayer whitePlayer;
    private StartPageController controller;
    
    @Before
    public void setUp() throws Exception {
        model=new ExtendedModelMap();
        gameMap =new ConcurrentHashMap<>();
        whitePlayer=new ChessGamePlayer(new HumanPlayer("Ted"), Colour.WHITE);
        controller=new StartPageController();
        controller.setGameMap(gameMap);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        assertSessionAttributeNull();    
        controller.createGame(model, whitePlayer);
        assertGameMapNotEmpty();
        assertPlayerIsAddedToChessGame();
        assertLongStoreInSessionAttribute();
    }
    
    private void assertSessionAttributeNull(){
        assertNull(model.asMap().get(ServerConstants.GAME_UUID.toString()));
    }
    
    private void assertLongStoreInSessionAttribute(){
        assertEquals(model.asMap().get(ServerConstants.GAME_UUID.toString()).getClass(),Long.class);
    }
    
    private void assertGameMapNotEmpty(){
        assertTrue(gameMap.size()==1);
    }
    
    private void assertPlayerIsAddedToChessGame(){
        List<ServerChessGame> games= new ArrayList<>(gameMap.values());
        assertTrue(games.get(0).getPlayer().equals(whitePlayer));
    }
}