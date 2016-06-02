package org.amc.game.chessserver.spring

import static org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus.FINISHED;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.dao.DAO;
import org.amc.dao.ManagedDAOFactory;
import org.amc.dao.SCGameDAO;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.ServerChessGame;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;


class ChessGameCleanUpTest {
    ChessGameCleanUp cleanUpListener;
    
    static final def ENTITYMANAGER_FACTORY = 'applicationEntityManagerFactory';
    
    def games;
    
    @Mock
    ServletContextEvent event;
    
    @Mock
    ServletContext servletContext;
    
    @Mock
    WebApplicationContext wac;
    
    @Mock
    DAO<AbstractServerChessGame> scgDao;
    
    @Mock
    ManagedDAOFactory daoFactory;
    
    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this);
        cleanUpListener = new ChessGameCleanUp();
        
    
        games = new ArrayList();
        (1..5).each({
            games.add(mock(ServerChessGame.class));
        });
    
        when(wac.getBean(ChessGameCleanUp.DAO_FACTORY)).thenReturn(daoFactory);
        
        when(daoFactory.getServerChessGameDAO()).thenReturn(scgDao);
        
        when(scgDao.findEntities('currentStatus', AbstractServerChessGame.ServerGameStatus.FINISHED)).thenReturn(games);    
        
        when(event.getServletContext()).thenReturn(servletContext);
        
        when(servletContext.getAttribute(ChessGameCleanUp.SPRING_ROOT)).thenReturn(wac);
        
    }
    
    @Test
    void test() {
        cleanUpListener.contextInitialized(event);
        verify(scgDao, times(5)).deleteEntity(any());
    }
    
    @Test
    void testThrowsException() {
        when(scgDao.deleteEntity(any())).thenThrow(new DAOException());
        try {
            cleanUpListener.contextInitialized(event);
            
        } catch (DAOException de) {
            fail('DAOException not caught');
        }
    }
}
