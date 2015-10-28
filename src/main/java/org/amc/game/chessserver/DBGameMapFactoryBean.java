package org.amc.game.chessserver;

import org.amc.dao.DatabaseGameMap;
import org.amc.dao.ServerChessGameDAO;
import org.apache.log4j.Logger;
import org.apache.openjpa.persistence.InvalidStateException;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
/**
 * Factory for HashMap which will contain all {@link ChessGame} instances
 * Used by the Spring framework
 * 
 * @author Adrian Mclaughlin
 * @version 1.0
 *
 */
public class DBGameMapFactoryBean{
    
    private static final Logger logger = Logger.getLogger(DBGameMapFactoryBean.class);

    private Map<Long, ServerChessGame> gameMap;
    private ServerChessGameDAO serverChessGameDAO;
    private EntityManagerFactory entityManagerFactory;
    
    /**
     * Called by Spring beans needing an gameMap instance
     * 
     * @return ConcurrentHashMap 
     */
    public Map<Long, ServerChessGame> getGameMap(){
        return gameMap;
    }
    
    /**
     * initialises gameMap
     * Called by Spring container on bean creation
     * 
     */
    @PostConstruct
    public void init(){
        gameMap = new DatabaseGameMap();
        ((DatabaseGameMap)gameMap).setServerChessGameDAO(serverChessGameDAO);
    }
    
    /**
     * clears gameMap
     * Called by Spring container on bean destruction
     */
    @PreDestroy
    public void destroyGameMap() {
        logger.info("Persisting Game Map");
        synchronized (gameMap) {
           saveGameMapToDatabase(); 
        }
        System.out.println("Game Map has been cleared");
    }
    
    private void saveGameMapToDatabase() {
        DatabaseGameMap dbGameMap = (DatabaseGameMap) gameMap;
        EntityManager emManager = entityManagerFactory.createEntityManager();
        try {
            mergeEntitiesIntoPersistenceContext(emManager, dbGameMap);
            dbGameMap.clearCache();
        } catch (PersistenceException pe) {
            logger.info("Persisting GameMap failed");
            logger.error(pe);
        } finally {
            closeEntityManager(emManager);
        }
    }
    
    private void mergeEntitiesIntoPersistenceContext(EntityManager emManager, DatabaseGameMap dbGameMap) {
        emManager.getTransaction().begin();
        for(ServerChessGame game : dbGameMap.cacheValues()) {
            logger.info("Persisting ServerChessGame:" + game.getUid());
            emManager.merge(game);
            game.removeAllObservers();
        }
        emManager.getTransaction().commit();
    }
    
    private void closeEntityManager(EntityManager emManager) {
        try {
            emManager.close();
        } catch(PersistenceException|InvalidStateException pe) {
            logger.error(pe);
        }
    }
    
    public void setServerChessGameDAO(ServerChessGameDAO serverChessGameDAO) {
        this.serverChessGameDAO = serverChessGameDAO;
    }
    
    @Resource(name="applicationEntityManagerFactory")
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
}
    