package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StartPageControllerChessAppPageTest {
    private MockHttpSession session;
    private ConcurrentMap<Long, ServerChessGame> gameMap;
    private StartPageController controller;
    private ChessGamePlayer whitePlayer;

    @Before
    public void setUp() throws Exception {
        session = new MockHttpSession();
        gameMap = new ConcurrentHashMap<>();
        controller = new StartPageController();
        controller.setGameMap(gameMap);
        whitePlayer = new ChessGamePlayer(new HumanPlayer("Ted"), Colour.WHITE);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPlayerAvailable() {
        session.setAttribute(ServerConstants.PLAYER.toString(), whitePlayer);
        ModelAndView mav = controller.chessGameApplication(session);
        assertNotNull(session.getAttribute(ServerConstants.PLAYER.toString()));
        ModelAndViewAssert.assertModelAttributeAvailable(mav, ServerConstants.GAMEMAP.toString());
        ModelAndViewAssert.assertViewName(mav,
                        StartPageController.Views.CHESS_APPLICATION_PAGE.getPageName());
    }

    @Test
    public void testPlayerNotAvailable() {
        ModelAndView mav = controller.chessGameApplication(session);
        assertNull(session.getAttribute(ServerConstants.PLAYER.toString()));
        ModelAndViewAssert.assertViewName(mav,
                        StartPageController.Views.CREATE_PLAYER_PAGE.getPageName());
    }

}
