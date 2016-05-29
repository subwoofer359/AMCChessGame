package org.amc.game.chessserver.spring

import static org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus.FINISHED;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chessserver.ServerChessGame;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;


class ChessGameCleanUpTest {
    ChessGameCleanUp cleanUpListener;
    def games;
    
    @Mock
    ServerChessGameDAO scgDAO;
    
    @Mock
    ServletContextEvent event;
    
    @Mock
    ServletContext servletContext;
    
    @Mock
    WebApplicationContext wac;
    
    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this);
        cleanUpListener = new ChessGameCleanUp();
        games = new ArrayList();
        (1..5).each({
            games.add(mock(ServerChessGame.class));
        });
    
        when(wac.getBean("myServerChessGameDAO")).thenReturn(scgDAO);
        
        when(scgDAO.findEntities("currentStatus", FINISHED)).thenReturn(games);
        
        when(event.getServletContext()).thenReturn(servletContext);
        
        when(servletContext.getAttribute(ChessGameCleanUp.SPRING_ROOT)).thenReturn(wac);
    }
    
    @Test
    void test() {
        cleanUpListener.contextInitialized(event);
        verify(scgDAO, times(5)).deleteEntity(any());
    }
}
