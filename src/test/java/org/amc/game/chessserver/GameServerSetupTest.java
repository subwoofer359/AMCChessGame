package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContextEvent;

public class GameServerSetupTest {    
    private MockServletContext servletContext;
    private ServletContextEvent sce;
    private GameServerSetup gameSetup;
    
    @Before
    public void setUp() throws Exception {
        gameSetup=new GameServerSetup();
        servletContext=new MockServletContext();
        sce=new ServletContextEvent(servletContext);
    }

    @Test
    public void contextInitializedTest() {
        String gameMap=ServerConstants.GAME_MAP.toString();
        assertNull(servletContext.getAttribute(gameMap));
        gameSetup.contextInitialized(sce);
        assertEquals(servletContext.getAttribute(gameMap).getClass(),
                        ConcurrentHashMap.class);
    }
    
    @Test
    public void contextDestroyedTest(){
        gameSetup.contextInitialized(sce);
        String gameMap=ServerConstants.GAME_MAP.toString();
        ConcurrentHashMap<?, ?> cmap=(ConcurrentHashMap<?,?>)sce.getServletContext().getAttribute(gameMap);
        assertNotNull(servletContext.getAttribute(gameMap));
        gameSetup.contextDestroyed(sce);
        assertEquals(0,cmap.size());
        assertNull(servletContext.getAttribute(gameMap));
    }

}
