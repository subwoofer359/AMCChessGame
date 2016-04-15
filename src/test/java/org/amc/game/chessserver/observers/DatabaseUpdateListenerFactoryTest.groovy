package org.amc.game.chessserver.observers

import static org.junit.Assert.*;

import org.amc.dao.ServerChessGameDAO;
import org.amc.game.GameObserver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DatabaseUpdateListenerFactoryTest {

    
    DatabaseUpdateListenerFactory factory;
    
    @Mock
    ServerChessGameDAO serverChessGameDAO;
    
    
    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this);
        factory = new DatabaseUpdateListenerFactory() {
            @Override
            public GameObserver createObserver() {
                return null;
            };
            
        };
        factory.setServerChessGameDAO(serverChessGameDAO);
    }
    
    @Test
    void testIsInstanceOfObserverFactory() {
        assert factory instanceof ObserverFactory;
    }
    
    @Test
    void testForObserverClass() {
        assert factory.forObserverClass() == DatabaseUpdateListener.class;
    }
}
