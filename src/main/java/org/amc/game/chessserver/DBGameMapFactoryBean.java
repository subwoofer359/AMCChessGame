package org.amc.game.chessserver;

import org.amc.dao.DatabaseGameMap;
import org.amc.dao.ServerChessGameDAO;

import java.util.Map;

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
public class DBGameMapFactoryBean{

    private Map<Long, ServerChessGame> gameMap;
    private ServerChessGameDAO serverChessGameDAO;
    
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
        gameMap=new DatabaseGameMap();
        ((DatabaseGameMap)gameMap).setServerChessGameDAO(serverChessGameDAO);
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
    
    public void setServerChessGameDAO(ServerChessGameDAO serverChessGameDAO) {
        this.serverChessGameDAO = serverChessGameDAO;
    }
}
    