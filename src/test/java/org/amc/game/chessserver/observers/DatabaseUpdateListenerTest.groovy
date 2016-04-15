package org.amc.game.chessserver.observers

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.dao.DatabaseGameMap;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.GameObserver;
import org.amc.game.chess.ChessGame.GameState;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.ServerChessGame;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ConcurrentMap;

import javax.persistence.OptimisticLockException;

class DatabaseUpdateListenerTest {

    static final ServerGameStatus status = ServerGameStatus.IN_PROGRESS;
    DatabaseUpdateListener listener;
    
    @Mock
    ServerChessGameDAO scgDAO;
    
    @Mock
    DatabaseGameMap gameMap;
    
    @Mock
    AbstractServerChessGame scg;
    
    
    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ((DatabaseGameMap)gameMap).setServerChessGameDAO(scgDAO);
        when(gameMap.getServerChessGameDAO()).thenReturn(scgDAO);
        
        listener = new DatabaseUpdateListener();
        listener.setGameToObserver(scg);
        listener.setGameMap(gameMap);
        
        
    }
    
    
    @Test
    void testConstructor() {
        assert listener.getClass() == DatabaseUpdateListener.class;
    }
    
    @Test
    void testInstanceOfGameObserver() {
        assert listener instanceof GameObserver;
    }
    
    @Test
    void testGameStatusUpdate() {
        listener.update(scg, status);
        verify(scgDAO, times(1)).updateEntity(scg);
    }
    
    @Test
    void testGameStatusUpdateOnlyStatusUpdates() {
        listener.update(scg,"Test");
        verify(scgDAO, times(0)).updateEntity(scg);
    }
    
    @Test 
    void testOptimisticLockException() {
        when(scgDAO.updateEntity(anyObject())).thenThrow(new OptimisticLockException("Test Exception"));
        listener.update(scg, status);
        verify(scgDAO, times(1)).updateEntity(scg);
    }
    
}
