package org.amc.dao

import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

class EntityCacheMapTest {
    EntityManagerCache emc;
    
    @Mock
    EntityManagerFactory factory;
    
    @Mock
    EntityManager em;
    
    @Mock
    EntityManager newEm;

    @Mock
    ConcurrentMap<Long, EntityManagerCache.ManagerInfo> entityManagerMap;

    static final Long GAME_UID = 1234L;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        emc = new EntityManagerCache();
        emc.setEntityManagerFactory(factory);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetEntityManagerNoEntityInCache() {
        when(factory.createEntityManager()).thenReturn(em);
        
        assert emc.isEmpty() == true;
        EntityManager enManager = emc.getEntityManager(GAME_UID);
        
        verify(factory, times(1)).createEntityManager();
        
        //verify EntityManager is stored in cache
        assert emc.isEmpty() == false;
    }
    
    @Test
    public void testGetEntityManagerWithEntityInCache() {
        when(factory.createEntityManager()).thenReturn(em);
        when(em.isOpen()).thenReturn(true);
        emc.putEntityManager(GAME_UID, em);
        
        assert emc.isEmpty() == false;
        EntityManager enManager = emc.getEntityManager(GAME_UID);
        
        verify(factory, never()).createEntityManager();
        
        //verify EntityManager is stored in cache
        assert emc.isEmpty() == false;
    }
    
    @Test
    public void testReturnOnlyOpenEntityManager() {
        //old entityManager is closed
        when(em.isOpen()).thenReturn(false);
        emc.putEntityManager(GAME_UID, em);
        
        
        //return an open entityManager
        when(factory.createEntityManager()).thenReturn(newEm);
        when(newEm.isOpen()).thenReturn(true);
        
        
        EntityManager enManager = emc.getEntityManager(GAME_UID);
        
        assert enManager.isOpen() == true;
    }
    
    @Test
    public void checkDateUpdateOnAccessOfEntityManager() {
        when(factory.createEntityManager()).thenReturn(newEm);
        when(newEm.isOpen()).thenReturn(true);
        
        Long uid = 1L;
        
        emc.getEntityManager(uid);
        
        Calendar firstAccess = emc.entityManagerMap?.get(uid)?.lastUsedDate;
        
        emc.getEntityManager(uid);
        Calendar secondAccess = emc.entityManagerMap?.get(uid)?.lastUsedDate;
        assert firstAccess.before(secondAccess);
    }

    @Test
    void getOldestEntity() {
        when(factory.createEntityManager()).thenReturn(newEm);
        when(newEm.isOpen()).thenReturn(true);
        (1..100).each() {
            emc.getEntityManager(it);
        }
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -1);
        def oldEms = emc.getOldestEntityManagers(c);
        assert oldEms.isEmpty() == true;
        assert emc.isEmpty() == false;
    }


    @Test
    void getEntityManagerWhenValueAlreadyInMap() {
        emc.entityManagerMap = entityManagerMap;

        EntityManagerCache.ManagerInfo info = new EntityManagerCache.ManagerInfo(em);

        when(factory.createEntityManager()).thenReturn(newEm);
        when(entityManagerMap.putIfAbsent(eq(GAME_UID), any())).thenReturn(info);

        EntityManager entityManager = this.emc.getEntityManager(GAME_UID);

        assert entityManager == em;

    }
}
