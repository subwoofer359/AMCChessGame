package org.amc.dao

import static org.mockito.Mockito.*;

import org.amc.dao.EntityManagerCache.ManagerInfo;
import org.amc.dao.EntityManagerCacheCleaner.EmCacheCleanerThread;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ConcurrentMap;

import javax.persistence.EntityManager;

import groovy.transform.TypeChecked;

@TypeChecked
class EmCacheCleanerThreadTest {
    @Mock
    EntityManagerCache cache;
    
    @Mock
    EntityManager entityManager;
    
    EmCacheCleanerThread thread;
    
    final int minutesToLive = 1;
    
    Set<Long> oldGameUids;
    
    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this);
        thread = new EmCacheCleanerThread(cache, minutesToLive);
        oldGameUids = new HashSet([1L, 33L, 44L]);
        when(cache.getEntityManager(anyLong())).thenReturn(entityManager);
        when(cache.getOldestEntityManagers(any(Calendar.class))).thenReturn(oldGameUids);
    }
    
    @Test
    void testThread() {

        when(entityManager.isOpen()).thenReturn(true);
        
        thread.run();
        
        final int methodCalled = oldGameUids.size();
        
        verify(entityManager, times(methodCalled)).close();
        
        verify(cache, times(methodCalled)).remove(anyLong());
    }
    
    @Test
    void testThreadEntityManagerIsClosed() {

        when(entityManager.isOpen()).thenReturn(false);
        
        thread.run();
        
        final int methodCalled = oldGameUids.size();
        
        verify(entityManager, never()).close();
        
        verify(cache, times(methodCalled)).remove(anyLong());
    }
}
