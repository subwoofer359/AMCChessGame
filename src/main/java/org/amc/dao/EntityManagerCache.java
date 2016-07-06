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
        entityManagerMap = new ConcurrentHashMap<>();
    }
    
    public EntityManager getEntityManager(Long gameUid) {
        ManagerInfo managerInfo = entityManagerMap.get(gameUid);
        
        EntityManager manager;
        if(managerInfo == null) {
            manager = createEntityManager(gameUid, null);
        } else {
            manager = getEntityManagerCreateNewIfClosed(gameUid);
        }
        return manager;
    }

    private EntityManager createEntityManager(long gameUid, ManagerInfo managerInfo) {
        EntityManager manager = entityManagerFactory.createEntityManager();
        ManagerInfo returnInfo = entityManagerMap.putIfAbsent(gameUid, new ManagerInfo(manager));
        if(isNotTheSameManagerInfo(returnInfo, managerInfo)) {
            manager.close();
            manager = returnInfo.getEntityManager();
        }
        return manager;
    }
    
    private boolean isNotTheSameManagerInfo(ManagerInfo a, ManagerInfo b) {
    	return a != null && !a.equals(b);
    }

    private EntityManager getEntityManagerCreateNewIfClosed(Long gameUid) {
        EntityManager manager = entityManagerMap.get(gameUid).getEntityManager();
        if (!manager.isOpen()) {
            manager = entityManagerFactory.createEntityManager();
            entityManagerMap.replace(gameUid, new ManagerInfo(manager));
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
    
    static class ManagerInfo {
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
