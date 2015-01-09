package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StartPageControllerChessAppPageTest {
    private MockServletContext servletContext;
    private MockHttpSession session;
    private ConcurrentMap<Long, ChessGame> gameMap;
    private StartPageController controller;
    private Player whitePlayer;
    
    
    @Before
    public void setUp() throws Exception {
        servletContext=new MockServletContext();
        session=new MockHttpSession();
        gameMap =new ConcurrentHashMap<>();
        servletContext.setAttribute(ServerConstants.GAMEMAP.toString(), gameMap);
        controller=new StartPageController();
        whitePlayer=new HumanPlayer("Ted", Colour.WHITE);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPlayerAvailable() {
        session.setAttribute(ServerConstants.PLAYER.toString(), whitePlayer);
        ModelAndView mav=controller.chessGameApplication(session);
        ModelAndViewAssert.assertModelAttributeAvailable(mav, ServerConstants.PLAYER.toString());
        ModelAndViewAssert.assertModelAttributeAvailable(mav, ServerConstants.GAMEMAP.toString());
        ModelAndViewAssert.assertViewName(mav, StartPageController.Views.CHESS_APPLICATION_PAGE.getPageName());
    }
    
    @Test
    public void testPlayerNotAvailable() {
        ModelAndView mav=controller.chessGameApplication(session);
        assertNull(mav.getModel().get(ServerConstants.PLAYER.toString()));
        ModelAndViewAssert.assertViewName(mav, StartPageController.Views.CREATE_PLAYER_PAGE.getPageName());
    }

}
