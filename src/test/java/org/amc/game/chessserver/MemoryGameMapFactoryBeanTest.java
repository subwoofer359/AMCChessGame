package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.dao.DatabaseGameMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

public class MemoryGameMapFactoryBeanTest {
    
    private MemoryGameMapFactoryBean factory;

    @Before
    public void setUp() throws Exception {
        factory = new MemoryGameMapFactoryBean();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        Map<Long, ServerChessGame> gameMap = factory.getGameMap();
        assertNull(gameMap);
        factory.init();
        gameMap = factory.getGameMap();
        assertNotNull(gameMap);
    }
    
    @Test
    public void testDestory() {
        DatabaseGameMap gameMap = mock(DatabaseGameMap.class);
        factory.setGameMap(gameMap);
        factory.destroyGameMap();
        
        verify(gameMap, times(1)).clear();
        
    }
    
    @Test
    public void testNoInitDestory() {
        factory.destroyGameMap();
    }
}
