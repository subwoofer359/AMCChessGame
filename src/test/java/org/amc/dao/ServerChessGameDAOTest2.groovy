package org.amc.dao

import org.amc.game.chess.Player;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import groovy.mock.interceptor.StubFor;

import org.amc.EntityManagerThreadLocal;
import org.amc.game.chess.*;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.ServerChessGame
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
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
    };

    Player player
    static def PLAYER_NAME = "Ted";
    
    private static final AbstractServerChessGame GAME = new ServerChessGame() {
        @Override
        public void addOpponent(Player opponent) {
            // do nothing

        }
    };

    private static final String QUERY_NAME = 'getChessGamesByPlayer';

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<AbstractServerChessGame> query;

    @Before
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        player = new HumanPlayer(PLAYER_NAME);

        when(em.createNamedQuery(eq(QUERY_NAME), anyObject())).thenReturn(query);
        when(query.getResultList()).thenReturn([GAME]);
    }

    @Test
    void testGetServerChessGamesGivenPlayer() {
        def games = dao.getGamesForPlayer(player);
        assert games?.isEmpty() == false;
        assert games[0] == GAME;
    }
}
