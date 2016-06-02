package org.amc.game.chessserver.spring

import static org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus.FINISHED;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.dao.ServerChessGameDAO;
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
    EntityManagerFactory entityManagerFactory;
    
    @Mock
    EntityManager entityManager;
    
    @Mock
    TypedQuery query;
    
    @Mock
    EntityTransaction transaction;
    
    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this);
        cleanUpListener = new ChessGameCleanUp();
        games = new ArrayList();
        (1..5).each({
            games.add(mock(ServerChessGame.class));
        });
    
        when(wac.getBean(ENTITYMANAGER_FACTORY)).thenReturn(entityManagerFactory);
        
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        
        when(entityManager.createQuery(anyString(), any())).thenReturn(query);
        
        when(entityManager.getTransaction()).thenReturn(transaction);
        
        when(query.getResultList()).thenReturn(games);
        
        when(event.getServletContext()).thenReturn(servletContext);
        
        when(servletContext.getAttribute(ChessGameCleanUp.SPRING_ROOT)).thenReturn(wac);
        
    }
    
    @Test
    void test() {
        cleanUpListener.contextInitialized(event);
        verify(entityManager, times(5)).remove(any());
    }
    
    @Test
    void testThrowsException() {
        when(query.getResultList()).thenThrow(new PersistenceException());
        try {
            cleanUpListener.contextInitialized(event);
            
        } catch (DAOException) {
            fail('DAOException not caught');
        }
    }
}
