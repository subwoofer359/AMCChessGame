package org.amc.dao

import org.springframework.test.annotation.Repeat

import javax.persistence.Cache
import javax.persistence.PersistenceUnitUtil
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.metamodel.Metamodel

import groovy.transform.TypeChecked

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
@TypeChecked
class EntityCacheMapGetThreadTest {

    private static final Long gameUid = 1L;
    private List<EntityManager> entityManagerList;

    private EntityManagerCache emc;

    private EntityManagerFactory entityManagerfactory;

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this);

        entityManagerList = Collections.synchronizedList(new ArrayList<EntityManager>());
        entityManagerfactory = new MockEntityManagerFactory();
        emc = new EntityManagerCache();
        emc.entityManagerFactory = entityManagerfactory;
    }

    @Test
    void testGetEntityManager() {
        int size = 50;
        Range range = (1..size);
        CountDownLatch latch = new CountDownLatch(size);
        List<CacheWorker> workers = [];

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
