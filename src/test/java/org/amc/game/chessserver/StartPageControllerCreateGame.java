package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StartPageControllerCreateGame {
    private MockServletContext servletContext;
    private Model model;
    private ConcurrentMap<Long, ChessGame> gameMap;
    private Player whitePlayer;
    private StartPageController controller;
    
    @Before
    public void setUp() throws Exception {
        model=new ExtendedModelMap();
        servletContext=new MockServletContext();
        gameMap =new ConcurrentHashMap<>();
        servletContext.setAttribute(ServerConstants.GAMEMAP.toString(), gameMap);
        whitePlayer=new HumanPlayer("Ted", Colour.WHITE);
        controller=new StartPageController();
        controller.setServletContext(servletContext);
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
        List<ChessGame> games= new ArrayList<>(gameMap.values());
        assertTrue(games.get(0).getCurrentPlayer().equals(whitePlayer));
    }
}