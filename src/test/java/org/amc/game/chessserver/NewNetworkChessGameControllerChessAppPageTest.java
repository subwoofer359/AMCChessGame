package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.dao.SCGDAOInterface;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.RealChessGamePlayer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.servlet.ModelAndView;

public class NewNetworkChessGameControllerChessAppPageTest {
    private MockHttpSession session;
    private NewNetworkChessGameController controller;
    private ChessGamePlayer whitePlayer;
    
    @Mock
    private SCGDAOInterface sCGDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ServerChessGameFactory scgFactory = new ServerChessGameFactory();
        session = new MockHttpSession();
        controller = new NewNetworkChessGameController(); 
        controller.setServerChessGameDAO(sCGDAO);
        
        controller.setServerChessGameFactory(scgFactory);
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("Ted"), Colour.WHITE);
    }

    @Test
    public void testPlayerAvailable() {
        session.setAttribute(ServerConstants.PLAYER.toString(), whitePlayer);
        ModelAndView mav = controller.chessGameApplication(session, whitePlayer);
        assertNotNull(session.getAttribute(ServerConstants.PLAYER.toString()));
        ModelAndViewAssert.assertModelAttributeAvailable(mav, ServerConstants.GAMEMAP.toString());
        ModelAndViewAssert.assertViewName(mav,
        		GameControllerHelper.CHESS_APPLICATION_PAGE);
    }
    
    @Test
    public void handleMissingSessionAttributesTest() {
        HttpSessionRequiredException hsre = mock(HttpSessionRequiredException.class);
        when(hsre.getMessage()).thenReturn("Mock Exception");
        String view = controller.handleMissingSessionAttributes(hsre);
        assertEquals(GameControllerHelper.TWOVIEW_REDIRECT_PAGE, view);
    }
}
