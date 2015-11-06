package org.amc.dao;

import org.amc.DAOException;
import org.amc.game.chessserver.ServerChessGame;
import org.apache.log4j.Logger;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DatabaseGameMap implements Map<Long, ServerChessGame> {

    ServerChessGameDAO serverChessGameDAO;
    
    private static final Logger logger = Logger.getLogger(DatabaseGameMap.class);
    
    private ConcurrentMap<Long, ServerChessGame> gameMap;
    
    public DatabaseGameMap() {
        gameMap = new ConcurrentHashMap<Long, ServerChessGame>();
    }
    
    @Override
    public int size() {
        try {
            Set<ServerChessGame> gameSet = new HashSet<>();
            gameSet.addAll(this.serverChessGameDAO.findEntities());
            gameSet.addAll(this.gameMap.values());
            return gameSet.size();
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
            return gameMap.isEmpty() && this.serverChessGameDAO.findEntities().isEmpty();
        } catch (DAOException de) {
            logger.error(de);
            return true;
        }
    }

    @Override
    public boolean containsKey(Object key) {
        try {
            if(key != null && key instanceof Long){
                return this.gameMap.containsKey(key) || 
                                this.serverChessGameDAO.findEntities("uid", (Long)key).size() == 1;
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
                    game = this.serverChessGameDAO.getServerChessGame((Long) key);
                    if (game == null) {
                        logger.error("ServerChessGame with uid:" + key + " doesn't exist!");
                        return null;
                    } else {
                        gameMap.putIfAbsent((Long) key, game);
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
        return gameMap.putIfAbsent(key, value);
    }

    @Override
    public ServerChessGame remove(Object key) {
        ServerChessGame gameToDelete = this.get(key);
        if(gameToDelete == null) {
            logger.debug("No ServerChessGame to remove");
        } else {
            try {
                gameMap.remove(key);
                serverChessGameDAO.deleteEntity(gameToDelete);
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
    
    @Override
    public Set<Long> keySet() {
        Set<Long> keys = new HashSet<>();
        keys.addAll(serverChessGameDAO.getGameUids());
        keys.addAll(gameMap.keySet());
        return keys;
    }

    @Override
    public Collection<ServerChessGame> values() {
        try {
            Set<ServerChessGame> games = new HashSet<>(serverChessGameDAO.findEntities());
            games.addAll(gameMap.values());
            return games;
        } catch(DAOException de) {
            logger.error(de);
        }
        return Collections.emptyList();
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
    
    /**
     * Clears the cache. 
     * ServerChessGames in the cache may not be persisted to the database.
     */
    public void clearCache() {
        gameMap.clear();
    }
    
    /**
     * Retrieve a collection of ServerChessGames from the cache
     * @return Collection of ServerChessGames
     */
    public Collection<ServerChessGame> cacheValues() {
        return gameMap.values();
    }
    
    public void setServerChessGameDAO(ServerChessGameDAO serverChessGameDAO) {
        this.serverChessGameDAO = serverChessGameDAO;
    }
    
    void setInternalHashMap(ConcurrentMap<Long, ServerChessGame> gameMap) {
        this.gameMap = gameMap;
    }
}
