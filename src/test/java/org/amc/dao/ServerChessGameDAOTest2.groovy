package org.amc.dao

import org.amc.game.chess.Player;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import groovy.mock.interceptor.StubFor;

import org.amc.DAOException;
import org.amc.EntityManagerThreadLocal;
import org.amc.game.chess.*;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.ServerChessGame
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    
    private static final AbstractServerChessGame GAME = new ServerChessGame() {
        @Override
        public void addOpponent(Player opponent) {
            // do nothing

        }
       
        @Override
        public long getUid() {
            return GAME_UID;
        };
    };

    private static final String QUERY_NAME = 'getChessGamesByPlayer';

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<AbstractServerChessGame> query;
    
    @Mock
    EntityManagerCache emCache;

    @Before
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        player = new HumanPlayer(PLAYER_NAME);
        dao.setEntityManagerCache(emCache);

        when(em.createNamedQuery(eq(QUERY_NAME), anyObject())).thenReturn(query);
        when(query.getResultList()).thenReturn([GAME]);
    }

    @Test
    void testGetServerChessGamesGivenPlayer() {
        def games = dao.getGamesForPlayer(player);
        assert games?.isEmpty() == false;
        assert games[GAME.getUid()] == GAME;
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
}
