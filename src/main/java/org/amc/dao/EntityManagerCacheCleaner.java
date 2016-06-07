package org.amc.dao;

import org.apache.log4j.Logger;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

public class EntityManagerCacheCleaner {
    private TaskScheduler scheduler;
    private static final Logger logger = Logger.getLogger(EntityManagerCacheCleaner.class);
    
    private EntityManagerCache enCache;
    private long period = 10; //10 Seconds
    private TimeUnit periodUnit = TimeUnit.SECONDS;
    private int minutesToLive = 10;
    static int livePeriod = Calendar.MINUTE;
    
    public EntityManagerCacheCleaner(EntityManagerCache enCache, long period) {
        this.enCache = enCache;
        this.period = period;
    }
    
    public EntityManagerCacheCleaner(EntityManagerCache enCache, long period, int minutesToLive) {
        this(enCache, period);
        this.minutesToLive = minutesToLive;
    }
    
    public void init() {
        logger.info(String.format("EntityManagerCacheCleaner task set for a period of %d %s", period, periodUnit));
        scheduleCleaner(enCache);
    }
    
    private void scheduleCleaner(EntityManagerCache enCache) {
        if(scheduler == null) {
            throw new RuntimeException("Scheduler is null");
        } else {
            scheduler.schedule(new EmCacheCleanerThread(enCache, minutesToLive),
                            new PeriodicTrigger(period, periodUnit));
        }
    }
    
    @Resource(name = "myScheduler")
    public void setScheduler(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }
    static class EmCacheCleanerThread implements Runnable {
        
        private EntityManagerCache enCache;
        
        private int minutesToLive;
        
        public EmCacheCleanerThread(EntityManagerCache enCache, int minutesToLive) {
            this.enCache = enCache;
            this.minutesToLive = minutesToLive;
        }

        @Override
        public void run() {
            logger.info("EntityManagerCacheCleaner task started");
            Calendar oldTime = Calendar.getInstance();
            oldTime.add(livePeriod, -1 * minutesToLive);
            
            Set<Long> gameUids = enCache.getOldestEntityManagers(oldTime);
            
            for(Long gameUid : gameUids) {
                EntityManager entityManager = enCache.getEntityManager(gameUid);
                enCache.remove(gameUid);
                if(entityManager.isOpen()) {
                    entityManager.close();
                }
                logger.info("========================================");
                logger.info(" Removed Game:" + gameUid);
                logger.info("========================================");
            }
            
            logger.info("EntityManagerCacheCleaner has run");
            
        }
        
        
    }
}
