package org.amc.dao;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.EntityManager;

public class EntityManagerCache {
    
    ConcurrentMap<Long, EntityManager> entityManagerMap;
    public EntityManagerCache() {
        entityManagerMap = new ConcurrentHashMap<Long, EntityManager>();
    }
    
    public EntityManager getEntityManager(Long gameUid) {
        return entityManagerMap.get(gameUid);
    }
    
    public void putEntityManager(Long gameUid, EntityManager emManager) {
        entityManagerMap.put(gameUid, emManager);
    }
}
