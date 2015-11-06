package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.amc.DAOException;
import org.amc.dao.DAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;

public final class PlayerFilterTest {
    private final static String FULLNAME = "Adrian McLaughlin";
    private final static String PLAYERNAME = "adrian";
    // UserFilter constants todo replace this code
    private final static String SESSIONVAR_PLAYER = "PLAYER";
    

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * After doFilter call, Session variables PLAYER and REMOTE_ADDRESS should be
     * set with no exceptions thrown
     * 
     * @throws ServletException
     * @throws IOException
     * @throws DAOException
     */
    @Test
    public void testUserSavedToSession() throws ServletException, IOException, DAOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockServletContext context = new MockServletContext();
        MockFilterConfig fConfig = new MockFilterConfig(context);
        MockFilterChain chain = new MockFilterChain();
        MockHttpSession session = new MockHttpSession();

        // Create User to be retrieved from mock database query
        Player player = new HumanPlayer(FULLNAME);
        
        DAO<Player> playerDAO = mock(DAO.class);
        List<Player> playerList = new ArrayList<Player>();
        playerList.add(player);
        when(playerDAO.findEntities(PlayerFilter.PLAYER_USERNAME, PLAYERNAME)).thenReturn(playerList);

        // Spring Context
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        when(applicationContext.getBean(PlayerFilter.PLAYERDAO, DAO.class)).thenReturn(playerDAO);

        // Store Spring Context in ServletContext
        context.setAttribute(PlayerFilter.SPRING_WEBAPPCONTEXT, applicationContext);

        Principal playerPrincipal = mock(Principal.class);
        when(playerPrincipal.getName()).thenReturn(PLAYERNAME);
        request.setUserPrincipal(playerPrincipal);
        request.setSession(session);

        PlayerFilter filter = new PlayerFilter();
        filter.init(fConfig);
        filter.doFilter(request, response, chain);

        assertEquals(session.getAttribute(SESSIONVAR_PLAYER), player);
    }
}
