package org.amc.game.chessserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class GameMapFactoryBean{

    private Map<Long, ServerChessGame> gameMap;
    
    public Map<Long, ServerChessGame> getGameMap(){
        return gameMap;
    }
    
    @PostConstruct
    public void init(){
        gameMap=new ConcurrentHashMap<Long, ServerChessGame>();
    }
    
    @PreDestroy
    public void destroyGameMap(){
        gameMap.clear();
        System.out.println("Game Map has been cleared");
    }

}
    