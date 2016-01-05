package org.amc.game.chessserver;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Factory for HashMap which will contain all {@link ChessGame} instances
 * Used by the Spring framework
 * 
 * @author Adrian Mclaughlin
 * @version 1.0
 *
 */
public class MemoryGameMapFactoryBean{
    
    private static final Logger logger = Logger.getLogger(MemoryGameMapFactoryBean.class);

    private Map<Long, AbstractServerChessGame> gameMap;
    
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
        gameMap = new ConcurrentHashMap<Long, AbstractServerChessGame>();
    }
    
    /**
     * clears gameMap
     * Called by Spring container on bean destruction
     */
    @PreDestroy
    public void destroyGameMap(){
        if(gameMap != null ) {
            gameMap.clear();
            logger.debug("GameMap has been cleared");
        }
    }
    
    /**
     * Used for testing only
     * @param gameMap {@link Map}
     */
    void setGameMap(Map<Long, AbstractServerChessGame> gameMap) {
        this.gameMap = gameMap;
    }

}
    