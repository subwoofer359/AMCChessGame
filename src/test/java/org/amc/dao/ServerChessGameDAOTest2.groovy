package org.amc.dao

import org.amc.game.GameObserver;
import org.amc.game.chess.Player;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import groovy.mock.interceptor.StubFor;

import org.amc.DAOException;
import org.amc.game.chess.*;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.ServerChessGame
import org.amc.util.Subject;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAEntityTransaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

class ServerChessGameDAOTest2 {
    /**
     * stub getEntityManager to return a mock
     */
    final ServerChessGameDAO dao = new ServerChessGameDAO() {
        @Override
        public EntityManager getEntityManager() {
            return em;
        }
        
        @Override
        void addObservers(AbstractServerChessGame arg0) throws DAOException {
            //nothing
        };
    };

    Player player
    
    static def PLAYER_NAME = "Ted";
    
    static final Long GAME_UID = 1234L;
    
    AbstractServerChessGame GAME = new ServerChessGame() {
        @Override
        public void addOpponent(Player opponent) {
            // do nothing

        }
       
        @Override
        public long getUid() {
            return GAME_UID;
        };
    };

    static final String QUERY_NAME = 'getChessGamesByPlayer';

    @Mock
    EntityManager em;
    
    @Mock
    EntityTransaction transaction;

    @Mock
    TypedQuery<AbstractServerChessGame> query;
    
    @Mock
    EntityManagerCache emCache;

    @Before
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        player = new HumanPlayer(PLAYER_NAME);
        dao.setEntityManagerCache(emCache);
        setUpEntityManager(em, transaction);
    }
    
    void setUpEntityManager(EntityManager entityManager, EntityTransaction emTransaction) {
        when(entityManager.getTransaction()).thenReturn(emTransaction);
        when(entityManager.createNamedQuery(eq(QUERY_NAME), anyObject())).thenReturn(query);
        when(query.getResultList()).thenReturn([GAME]);
    }

    @Test
    void testGetServerChessGamesGivenPlayer() {
        def games = dao.getGamesForPlayer(player);
        assert games?.isEmpty() == false;
        assert games[GAME.getUid()] == GAME;
    }
    
    @Test(expected = DAOException.class)
    void testGetServerChessGamesGivenPlayerException() {
        when(query.getResultList()).thenThrow(new PersistenceException("Test"));
        def games = dao.getGamesForPlayer(player);
    }
    
    @Test
    void testGetServerChessGame() {
        when(emCache.getEntityManager(eq(GAME_UID))).thenReturn(em);
        when(em.createNamedQuery(eq(ServerChessGameDAO.GET_SERVERCHESSGAME_QUERY))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(GAME);
        def game  = dao.getServerChessGame(GAME_UID);
        
        assert game == GAME;
    }
    
    @Test
    void testGetServerChessGameException() {
        when(emCache.getEntityManager(eq(GAME_UID))).thenReturn(em);
        when(em.createNamedQuery(eq(ServerChessGameDAO.GET_SERVERCHESSGAME_QUERY))).thenReturn(query);
        when(query.getSingleResult()).thenThrow(new NoResultException('More than one result'));
        def game  = dao.getServerChessGame(GAME_UID);
        
        assert game == null;
        
    }
    
    @Test
    void testSaveServerChessGame() {
        when(emCache.getEntityManager(eq(GAME_UID))).thenReturn(em);
        when(em.contains(eq(GAME))).thenReturn(false);
        
        dao.saveServerChessGame(GAME);
        
        verify(em, times(1)).merge(GAME);
        verify(transaction, times(1)).begin();
        verify(transaction, times(1)).commit();
    }
    
    @Test
    void testSaveServerChessGameDAOException() throws DAOException {
        when(emCache.getEntityManager(eq(GAME_UID))).thenReturn(em);
        when(em.contains(eq(GAME))).thenReturn(false);
        doThrow(new PersistenceException("Test Error")).when(transaction).commit();
        
        try {
            dao.saveServerChessGame(GAME);
            fail();
        } catch(DAOException de) {
            verify(transaction, times(1)).begin();
            verify(em, times(1)).merge(GAME);
            verify(em, times(1)).flush();
            verify(transaction, times(1)).commit();
            verify(em, never()).close();
        }
    }
    
    @Test
    void testSaveServerChessGameContainedInEntityManager() {
        when(emCache.getEntityManager(eq(GAME_UID))).thenReturn(em);
        when(em.contains(eq(GAME))).thenReturn(true);
        
        dao.saveServerChessGame(GAME);
        
        verify(em, never()).merge(GAME);
        verify(transaction, times(1)).begin();
        verify(em, times(1)).flush();
        verify(transaction, times(1)).commit();
    }
    
    @Test
    void testSaveServerChessGameInstanceOfOpenJPAEntityManager() {
        OpenJPAEntityManager em = mock(OpenJPAEntityManager.class);
        OpenJPAEntityTransaction transaction = mock(OpenJPAEntityTransaction.class);
        setUpEntityManager(em, transaction);
        
        when(emCache.getEntityManager(eq(GAME_UID))).thenReturn(em);
        when(em.contains(eq(GAME))).thenReturn(true);
        
        dao.saveServerChessGame(GAME);
        
        verify(em, times(1)).dirty(any(ChessGame.class), eq("board"));
    }
}
