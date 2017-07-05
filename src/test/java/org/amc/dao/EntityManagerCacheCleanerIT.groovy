package org.amc.dao

import org.junit.Before;
import org.junit.Ignore
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.EntityManager;

/**
 * Test needs to be rework and not require Spring for initialisation of Cleaner
 * and Thread.Sleep to decide when to check if task is complete
 * @return
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(['/EntityManagerFactory.groovy', '/perTestConfig/EMCCleanerConfig.groovy'])
class EntityManagerCacheCleanerIT {
        
    EntityManagerCacheCleaner cleaner;
    EntityManagerCache enCache;
    
    @Autowired
    WebApplicationContext wac;
    
    def threads = [];
 
    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this);
        
        cleaner = wac.getBean('entityManagerCacheCleaner');
        enCache = wac.getBean('myEntityManagerCache');
        
        (1..100).each {
            threads << new Thread(new TestInput(enCache));
        }
    }
    
    @Test
    void test() {
        final def gameUid = [10L ,20L, 30L, 40L];
        def managers = [];
        cleaner.livePeriod = Calendar.MILLISECOND;
        gameUid.each {
            managers << enCache.getEntityManager(it);
        }
       
        
        threads.each {
            it.start();
        };
    
        Thread.sleep(1000);
        
        managers.each {
            assert it.isOpen() != true;
        }
    }
    
    static class TestInput implements Runnable {
        EntityManagerCache enCache;
        
        TestInput(EntityManagerCache enCache) {
            this.enCache = enCache;
        }
        
        @Override
        public void run() {
            try {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                enCache.getEntityManager(random.nextLong(30));
            } catch(Exception e) {
                println(e.getMessage());
            }
        }
        
    }
}
