package org.amc.dao

import org.springframework.test.annotation.Repeat

import javax.persistence.Cache
import javax.persistence.PersistenceUnitUtil
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.metamodel.Metamodel
import java.util.concurrent.CountDownLatch

import static org.mockito.Mockito.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
/**
 * Created by adrian on 14/06/16.
 */
class EntityCacheMapGetThreadTest {

    static final Long gameUid = 1L;
    List<EntityManager> entityManagerList;

    EntityManagerCache emc;

    EntityManagerFactory entityManagerfactory;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        entityManagerList = Collections.synchronizedList(new ArrayList<EntityManager>());
        entityManagerfactory = new MockEntityManagerFactory();
        emc = new EntityManagerCache();
        emc.setEntityManagerFactory(entityManagerfactory);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    void testGetEntityManager() {
        def size = 50;
        def range = (1..size);
        CountDownLatch latch = new CountDownLatch(size);
        def workers = [];

        range.each {
            workers << new CacheWorker(latch, emc);

        };

        workers.each {
            it.start();
        }

        latch.await();
        EntityManager expectedEntityManager = emc.getEntityManager(gameUid);

        entityManagerList.each{
            assert expectedEntityManager == it;
        };
    }

    private class CacheWorker extends Thread {
        EntityManagerCache entityManagerCache;
        CountDownLatch countDownLatch;

        public CacheWorker(CountDownLatch countDownLatch, EntityManagerCache entityManagerCache) {
            this.entityManagerCache = entityManagerCache;
            this.countDownLatch = countDownLatch;
        }
        @Override
        void run() {
            entityManagerList << entityManagerCache.getEntityManager(gameUid);
            countDownLatch.countDown();
        }
    }


    private class MockEntityManagerFactory implements EntityManagerFactory {
        @Override
        EntityManager createEntityManager() {
            EntityManager em = mock(EntityManager.class);
            when(em.isOpen()).thenReturn(true);
            return em;
        }

        @Override
        EntityManager createEntityManager(Map map) {
            return null
        }

        @Override
        CriteriaBuilder getCriteriaBuilder() {
            return null
        }

        @Override
        Metamodel getMetamodel() {
            return null
        }

        @Override
        boolean isOpen() {
            return false
        }

        @Override
        void close() {

        }

        @Override
        Map<String, Object> getProperties() {
            return null
        }

        @Override
        Cache getCache() {
            return null
        }

        @Override
        PersistenceUnitUtil getPersistenceUnitUtil() {
            return null
        }
    }
}
