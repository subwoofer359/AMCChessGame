package org.amc.dao;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Component
@Scope(value ="websocket", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class WebSocketEntityManagerFactory {

    private EntityManagerFactory entityManagerFactory;
    
    private EntityManager emManager;
    
    private static final Logger logger = Logger.getLogger(WebSocketEntityManagerFactory.class);
    
    @PostConstruct
    public void init() {
        emManager = entityManagerFactory.createEntityManager();
        logger.info("++++++++++++ EntityManager:" + emManager + "has been created is WSM");
    }
    
    @PreDestroy
    public void destroy() {
        logger.info("+++++++++++ EntityManager:" + emManager + "has been destoryed is WSM");
        try{
            emManager.getTransaction().begin();
            emManager.flush();
            emManager.getTransaction();
        } finally {
            emManager.close();
            emManager = null;
        }
        
        
    }
    
    public EntityManager getEntityManager() {
        return emManager;
    }
    
    @Resource(name="applicationEntityManagerFactory")
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
}
