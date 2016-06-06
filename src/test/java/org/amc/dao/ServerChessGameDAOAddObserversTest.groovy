package org.amc.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalMatchers.*;

import org.amc.DAOException;
import org.amc.dao.ServerChessGameDAO.SCGObservers;
import org.amc.game.GameObserver;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.*;

import groovy.transform.CompileStatic;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;


class ServerChessGameDAOAddObserversTest {
    
    @Mock
    private EntityManager em;
    
    @Mock
    Query query;
    
    @Mock
    ObserverFactoryChain chain;
    
    static final Long GAME_UID = 1234L;
    
    static final OBSERVERS_STR = "observers";

    final SCGameDAO dao = new ServerChessGameDAO() {
        @Override
        public EntityManager getEntityManager(Long gameUid) {
            return em;
        }
    };

    SCGObservers observers;

    AbstractServerChessGame game = new ServerChessGame() {
        @Override
        public void addOpponent(Player opponent) {
            // do nothing

        }
   
        @Override
        public long getUid() {
            return GAME_UID;
        };
    };
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        dao.setObserverFactoryChain(chain);
        
        observers = new SCGObservers();
        observers.setObservers(OBSERVERS_STR);
        
        when(
            em.createNativeQuery(
                ServerChessGameDAO.NATIVE_OBSERVERS_QUERY,
                SCGObservers.class)
            )
            .thenReturn(query);
            
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAddObservers() {

        when(query.getSingleResult()).thenReturn(observers);
        
        assert game.getNoOfObservers() == 0;
        
        dao.addObservers(game);
        verify(query, times(1)).getSingleResult();
        verify(chain, times(1)).addObserver(OBSERVERS_STR, game);
    }
    
    @Test
    public void testAddObserversToOtherObservers() {    
        GameObserver observer = mock(GameObserver.class);
        game.attachObserver(observer);
        
        assert game.getNoOfObservers() == 1;
        
        dao.addObservers(game);
        
        verify(query, never()).getSingleResult();
        verify(chain, never()).addObserver(OBSERVERS_STR, game);
    }
    
    @Test(expected = DAOException.class)
    public void testAddObserversThrowsException() {
        when(query.getSingleResult()).thenThrow(new PersistenceException());
        
        assert game.getNoOfObservers() == 0;
        
        dao.addObservers(game);
    }
}
