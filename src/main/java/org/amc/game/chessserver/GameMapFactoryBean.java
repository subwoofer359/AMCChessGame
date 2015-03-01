package org.amc.game.chessserver;

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
public class GameMapFactoryBean{

    private Map<Long, ServerChessGame> gameMap;
    
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
        gameMap=new ConcurrentHashMap<Long, ServerChessGame>();
    }
    
    /**
     * clears gameMap
     * Called by Spring container on bean destruction
     */
    @PreDestroy
    public void destroyGameMap(){
        gameMap.clear();
        System.out.println("Game Map has been cleared");
    }

}
    