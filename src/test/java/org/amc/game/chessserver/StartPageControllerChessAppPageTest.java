package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.RealChessGamePlayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StartPageControllerChessAppPageTest {
    private MockHttpSession session;
    private ConcurrentMap<Long, ServerChessGame> gameMap;
    private StartPageController controller;
    private ChessGamePlayer whitePlayer;
    private ServerChessGameFactory scgFactory;

    @Before
    public void setUp() throws Exception {
        scgFactory = new ServerChessGameFactory();
        session = new MockHttpSession();
        gameMap = new ConcurrentHashMap<>();
        controller = new StartPageController(); 
        
        controller.setGameMap(gameMap);
        controller.setServerChessGameFactory(scgFactory);
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("Ted"), Colour.WHITE);
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
                        StartPageController.CHESS_APPLICATION_PAGE);
    }
    
    @Test
    public void handleMissingSessionAttributesTest() {
        HttpSessionRequiredException hsre = mock(HttpSessionRequiredException.class);
        when(hsre.getMessage()).thenReturn("Mock Exception");
        String view = controller.handleMissingSessionAttributes(hsre);
        assertEquals(StartPageController.TWOVIEW_REDIRECT_PAGE, view);
    }
}
