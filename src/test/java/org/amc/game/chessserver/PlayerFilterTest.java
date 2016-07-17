package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;

import org.amc.DAOException;
import org.amc.dao.DAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.junit.Before;
import org.junit.Test;
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
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterConfig fConfig;
    private MockFilterChain chain;
    private MockHttpSession session;
    private Player player;
    private DAO<Player> playerDAO;

    /**
     * After doFilter call, Session variables PLAYER and REMOTE_ADDRESS should be
     * set with no exceptions thrown
     * 
     * @throws ServletException
     * @throws IOException
     * @throws DAOException
     */
    @SuppressWarnings("unchecked")
	@Before
    public void setUp() throws Exception {
    	request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        MockServletContext context = new MockServletContext();
        fConfig = new MockFilterConfig(context);
        chain = new MockFilterChain();
        session = new MockHttpSession();
        player = new HumanPlayer(FULLNAME);
        playerDAO = mock(DAO.class);

        // Spring Context
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        when(applicationContext.getBean(PlayerFilter.PLAYERDAO, DAO.class)).thenReturn(playerDAO);

        // Store Spring Context in ServletContext
        context.setAttribute(PlayerFilter.SPRING_WEBAPPCONTEXT, applicationContext);

        Principal playerPrincipal = mock(Principal.class);
        when(playerPrincipal.getName()).thenReturn(PLAYERNAME);
        
        // Create User to be retrieved from mock database query
        
        request.setUserPrincipal(playerPrincipal);
        request.setSession(session);
    }
    
    @Test
    public void testUserSavedToSession() throws ServletException, IOException, DAOException {
    	when(playerDAO.findEntities(PlayerFilter.PLAYER_USERNAME, PLAYERNAME)).thenReturn(Arrays.asList(player));
    	PlayerFilter filter = new PlayerFilter();
        filter.init(fConfig);
        filter.doFilter(request, response, chain);

        assertEquals(session.getAttribute(SESSIONVAR_PLAYER), player);
    }
    
    @Test(expected=ServletException.class)
    public void getPlayerNoPlayerExistTest() throws ServletException, IOException, DAOException {
    	PlayerFilter filter = new PlayerFilter();
        filter.init(fConfig);
        filter.doFilter(request, response, chain);
    }
    
    @Test(expected=ServletException.class)
    public void getPlayerMoreThanPlayerExistTest() throws ServletException, IOException, DAOException {
    	List<Player> playerList = new ArrayList<Player>();
    	playerList.add(player);
    	playerList.add(player);
    	
    	when(playerDAO.findEntities(PlayerFilter.PLAYER_USERNAME, PLAYERNAME)).thenReturn(playerList);
    	PlayerFilter filter = new PlayerFilter();
        filter.init(fConfig);
        filter.doFilter(request, response, chain);
    }
    
    @Test(expected=ServletException.class)
    public void getPlayerDAOException() throws ServletException, IOException, DAOException {
    	when(playerDAO.findEntities(PlayerFilter.PLAYER_USERNAME, PLAYERNAME))
    		.thenThrow(new DAOException());
    	PlayerFilter filter = new PlayerFilter();
        filter.init(fConfig);
        filter.doFilter(request, response, chain);
    }
}
