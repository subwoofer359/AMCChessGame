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
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StartPageControllerChessAppPageTest {
    private MockServletContext servletContext;
    private Model model;
    private ConcurrentMap<Long, ChessGame> gameMap;
    private StartPageController controller;
    private Player whitePlayer;
    
    
    @Before
    public void setUp() throws Exception {
        servletContext=new MockServletContext();
        model=new ExtendedModelMap();
        gameMap =new ConcurrentHashMap<>();
        servletContext.setAttribute(ServerConstants.GAMEMAP.toString(), gameMap);
        controller=new StartPageController();
        controller.setServletContext(servletContext);
        whitePlayer=new HumanPlayer("Ted", Colour.WHITE);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPlayerAvailable() {
        model.addAttribute(ServerConstants.PLAYER.toString(), whitePlayer);
        ModelAndView mav=controller.chessGameApplication(model);
        ModelAndViewAssert.assertModelAttributeAvailable(mav, ServerConstants.PLAYER.toString());
        ModelAndViewAssert.assertModelAttributeAvailable(mav, ServerConstants.GAMEMAP.toString());
        ModelAndViewAssert.assertViewName(mav, StartPageController.Views.CHESS_APPLICATION_PAGE.getPageName());
    }
    
    @Test
    public void testPlayerNotAvailable() {
        ModelAndView mav=controller.chessGameApplication(model);
        assertNull(mav.getModel().get(ServerConstants.PLAYER.toString()));
        ModelAndViewAssert.assertViewName(mav, StartPageController.Views.CREATE_PLAYER_PAGE.getPageName());
    }

}
