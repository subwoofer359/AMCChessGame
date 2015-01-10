package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StartPageControllerJoinGameTest {
    private MockServletContext servletContext;
    private ConcurrentMap<Long, ServerChessGame> gameMap;
    private ServerJoinChessGameController controller;
    private Player whitePlayer;
    private Player blackPlayer;
    private long gameUUID=1234L;
    
    @Before
    public void setUp() throws Exception {
        servletContext=new MockServletContext();
        gameMap =new ConcurrentHashMap<>();
        servletContext.setAttribute(ServerConstants.GAMEMAP.toString(), gameMap);
        controller=new ServerJoinChessGameController();
        controller.setServletContext(servletContext);
        whitePlayer=new HumanPlayer("Ted", Colour.WHITE);
        blackPlayer=new HumanPlayer("Chris", Colour.WHITE);
        ServerChessGame chessGame=new ServerChessGame(whitePlayer);
        gameMap.put(gameUUID, chessGame);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        ModelAndView mav=controller.joinGame(gameMap,blackPlayer, gameUUID);
        ModelAndViewAssert.assertViewName(mav, "chessGamePortal");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "GAME_UUID");
        ServerChessGame chessGame=gameMap.get(gameUUID);
        assertEquals(ServerChessGame.status.IN_PROGRESS,chessGame.getCurrentStatus());
        assertEquals(chessGame.getOpponent(),blackPlayer);
    }
    
    @Test
    public void testPlayerJoinsOwnGame(){
        ModelAndView mav=controller.joinGame(gameMap,whitePlayer, gameUUID);
        ModelAndViewAssert.assertViewName(mav, "forward:/app/chessgame/chessapplication");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "ERRORS");
        ServerChessGame chessGame=gameMap.get(gameUUID);
        assertEquals(ServerChessGame.status.AWAITING_PLAYER,chessGame.getCurrentStatus());
    }
    
    @Test
    public void testPlayerJoinsGameAlreadyInProgress(){
        ServerChessGame chessGame=gameMap.get(gameUUID);
        chessGame.addOpponent(blackPlayer);
        assertNotEquals(ServerChessGame.status.AWAITING_PLAYER,chessGame.getCurrentStatus());
        ModelAndView mav=controller.joinGame(gameMap,blackPlayer, gameUUID);
        ModelAndViewAssert.assertViewName(mav, "forward:/app/chessgame/chessapplication");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "ERRORS");
        
    }

}