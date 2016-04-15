package org.amc.dao;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EntityManagerCache {
    
    private ConcurrentMap<Long, EntityManager> entityManagerMap;
    private EntityManagerFactory entityManagerFactory;
    
    public EntityManagerCache() {
        entityManagerMap = new ConcurrentHashMap<Long, EntityManager>();
    }
    
    public EntityManager getEntityManager(Long gameUid) {
        EntityManager manager = entityManagerMap.get(gameUid);
        if(manager == null) {
            manager = entityManagerFactory.createEntityManager();
            entityManagerMap.put(gameUid, manager);
        } else if (!manager.isOpen()) {
            manager = entityManagerFactory.createEntityManager();
            entityManagerMap.replace(gameUid, manager);
        }
        return manager;
    }
    
    public boolean isEmpty() {
        return entityManagerMap.isEmpty();
    }
    
    void putEntityManager(Long uid, EntityManager entityManager) {
        entityManagerMap.put(uid, entityManager);
    }
    
    @Resource(name = "applicationEntityManagerFactory")
    public void setEntityManagerFactory(EntityManagerFactory emFactory) {
        this.entityManagerFactory = emFactory;
    }
}
