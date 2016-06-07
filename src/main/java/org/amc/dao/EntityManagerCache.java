package org.amc.dao;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EntityManagerCache {
    
    ConcurrentMap<Long, ManagerInfo> entityManagerMap;
    private EntityManagerFactory entityManagerFactory;
    
    
    public EntityManagerCache() {
        entityManagerMap = new ConcurrentHashMap<Long, ManagerInfo>();
    }
    
    public EntityManager getEntityManager(Long gameUid) {
        ManagerInfo managerInfo = entityManagerMap.get(gameUid);
        
        EntityManager manager = null;
        if(managerInfo == null) {
            manager = entityManagerFactory.createEntityManager();
            entityManagerMap.putIfAbsent(gameUid, new ManagerInfo(manager));
        } else {
            manager = entityManagerMap.get(gameUid).getEntityManager();
            if (!manager.isOpen()) {
                manager = entityManagerFactory.createEntityManager();
                entityManagerMap.replace(gameUid, new ManagerInfo(manager));
            }
        }
        return manager;
    }
    
    public boolean isEmpty() {
        return entityManagerMap.isEmpty();
    }
    
    public void remove(Long gameUid) {
        this.entityManagerMap.remove(gameUid);
    }
    
    void putEntityManager(Long uid, EntityManager entityManager) {
        entityManagerMap.put(uid, new ManagerInfo(entityManager));
    }
    
    Set<Long> getOldestEntityManagers(Calendar date) {
       Set<Long> oldest = new HashSet<>();
       Set<Long> gameUids = entityManagerMap.keySet();

       for(Long gameUid : gameUids) {
           ManagerInfo info = entityManagerMap.get(gameUid);
           if(info.getLastUsedDate().before(date)) {
               oldest.add(gameUid);
           }
       }
       return oldest;
    }
    
    @Resource(name = "applicationEntityManagerFactory")
    public void setEntityManagerFactory(EntityManagerFactory emFactory) {
        this.entityManagerFactory = emFactory;
    }
    
    private static class ManagerInfo {
        EntityManager entityManager;
        private Calendar lastUsedDate;
        
        public ManagerInfo(EntityManager entityManager) {
            this.entityManager = entityManager;
            updateAccessDate();
        }
        
        public Calendar getLastUsedDate() {
            return lastUsedDate;
        }

        public EntityManager getEntityManager() {
            updateAccessDate();
            return entityManager;
        }
        
        private void updateAccessDate() {
            this.lastUsedDate = Calendar.getInstance();
        }
    }
    
    
}
