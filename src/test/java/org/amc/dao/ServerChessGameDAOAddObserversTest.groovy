package org.amc.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalMatchers.*;

import org.amc.DAOException;
import org.amc.dao.ServerChessGameDAO.SCGObservers;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.ServerChessGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;


class ServerChessGameDAOAddObserversTest {
    
    @Mock
    EntityManager em;
    
    @Mock
    Query query;
    
    static final Long GAME_UID = 1234L;

    final ServerChessGameDAO dao = new ServerChessGameDAO() {
        @Override
        public EntityManager getEntityManager() {
            return em;
        }
    };

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
        
        when(em.createNativeQuery(eq(ServerChessGameDAO.NATIVE_OBSERVERS_QUERY),
                            any(SCGObservers.class))).thenReturn(query);
        when(query.getResultList()).thenReturn([game]);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAddObservers() {
        dao.addObservers(game);
        verify(query, never()).getResultList();
    }

}
