package org.amc.game.chessserver;

import org.amc.dao.DatabaseGameMap;
import org.amc.dao.ServerChessGameDAO;
import org.apache.log4j.Logger;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

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

    private Map<Long, AbstractServerChessGame> gameMap;
    private ServerChessGameDAO serverChessGameDAO;
    private EntityManagerFactory entityManagerFactory;
    
    /**
     * Called by Spring beans needing an gameMap instance
     * 
     * @return ConcurrentHashMap 
     */
    public Map<Long, AbstractServerChessGame> getGameMap(){
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
           //do nothing 
        }
        System.out.println("Game Map has been cleared");
    }
    
    public void setServerChessGameDAO(ServerChessGameDAO serverChessGameDAO) {
        this.serverChessGameDAO = serverChessGameDAO;
    }
    
    @Resource(name="applicationEntityManagerFactory")
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
}
    