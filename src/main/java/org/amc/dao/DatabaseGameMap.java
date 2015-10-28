package org.amc.dao;

import org.amc.DAOException;
import org.amc.game.chessserver.ServerChessGame;
import org.apache.log4j.Logger;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseGameMap implements Map<Long, ServerChessGame> {

    ServerChessGameDAO chessGameDAO;
    
    private static final Logger logger = Logger.getLogger(DatabaseGameMap.class);
    
    Map<Long, ServerChessGame> gameMap;
    
    public DatabaseGameMap() {
        gameMap = new ConcurrentHashMap<>();
    }
    
    @Override
    public int size() {
        try {
            return this.chessGameDAO.findEntities().size();
        } catch (DAOException de) {
            logger.error(de);
            return 0;
        }
    }

    /**
     * If a DAOException is thrown then method returns true
     */
    @Override
    public boolean isEmpty() {
        try {
            return this.chessGameDAO.findEntities().isEmpty();
        } catch (DAOException de) {
            logger.error(de);
            return true;
        }
    }

    @Override
    public boolean containsKey(Object key) {
        try {
            if(key != null && key instanceof Long){
                if(gameMap.containsKey(key)) {
                    return true;
                } else {
                    return this.chessGameDAO.findEntities("uid", (Long)key).size() == 1;
                }
            } else {
                return false;
            }
            
        } catch (DAOException de) {
            logger.error(de);
            return false;
        }
    }

    
    /**
     * Not implemented
     */
    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServerChessGame get(Object key) {
        try {
            if (key != null && key instanceof Long) {
                ServerChessGame game = gameMap.get(key);
                if (game == null) {
                    synchronized (this) {
                        game = this.chessGameDAO.getServerChessGame((Long) key);
                        if (game == null) {
                            logger.error("ServerChessGame with uid:" + key + " doesn't exist!");
                            return null;
                        } else {
                            gameMap.put((Long) key, game);
                        }
                    }
                }
                return game;
            } else {
                return null;
            }
        } catch (DAOException de) {
            logger.error(de);
            return null;
        }
    }

    @Override
    public ServerChessGame put(Long key, ServerChessGame value) {
        try {
            gameMap.put(key, value);
            chessGameDAO.addEntity(value);
        } catch(DAOException de) {
            logger.error(de);
        }
        return value;
    }

    @Override
    public ServerChessGame remove(Object key) {
        ServerChessGame gameToDelete = this.get(key);
        if(gameToDelete == null) {
            logger.debug("No ServerChessGame to remove");
        } else {
            try {
                gameMap.remove(key);
                chessGameDAO.deleteEntity(gameToDelete);
            } catch (DAOException de) {
                logger.error(de);
            }
        }
        return gameToDelete;
    }

    
    /*
     * Not implemented
     */
    @Override
    public void putAll(Map<? extends Long, ? extends ServerChessGame> m) {
        logger.error("DatabaseGameMap.putAll(Map) is not implemented");
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        logger.error("DatabaseGameMap.clear() is not implemented");
        throw new UnsupportedOperationException();
    }

    /**
     * Clears the cache. 
     * ServerChessGames in the cache may not be persisted to the database.
     */
    public void clearCache() {
        gameMap.clear();
    }
    
    @Override
    public Set<Long> keySet() {
        return chessGameDAO.getGameUids();
    }

    @Override
    public Collection<ServerChessGame> values() {
        try {
            return chessGameDAO.findEntities();
        } catch(DAOException de) {
            logger.error(de);
        }
        return new ArrayList<ServerChessGame>();
    }
    
    /**
     * Retrieve a collection of ServerChessGames from the cache
     * @return Collection of ServerChessGames
     */
    public Collection<ServerChessGame> cacheValues() {
        return gameMap.values();
    }

    @Override
    public Set<java.util.Map.Entry<Long, ServerChessGame>> entrySet() {
        Set<Map.Entry<Long, ServerChessGame>> gameSet = new HashSet<>();
        Collection<ServerChessGame> games = values();
        for(ServerChessGame game: games) {
            gameSet.add(new AbstractMap.SimpleEntry<Long,ServerChessGame>(game.getUid(), game));
        }
        return gameSet;
    }
    
    public void setServerChessGameDAO(ServerChessGameDAO serverChessGameDAO) {
        this.chessGameDAO = serverChessGameDAO;
    }
    
    /**
     * Use for testing purposes only
     */
    
    void setDatabaseHashMap(Map<Long, ServerChessGame> hashMap) {
        this.gameMap = hashMap;
    }
}
