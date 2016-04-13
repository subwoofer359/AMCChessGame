package org.amc.dao;

import org.amc.DAOException;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.apache.log4j.Logger;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DatabaseGameMap implements ConcurrentMap<Long, AbstractServerChessGame> {

    ServerChessGameDAO serverChessGameDAO;
    
    private static final Logger logger = Logger.getLogger(DatabaseGameMap.class);
    
    private ConcurrentMap<Long, AbstractServerChessGame> gameMap;
    
    public DatabaseGameMap() {
        gameMap = new ConcurrentHashMap<Long, AbstractServerChessGame>();
    }
    
    @Override
    public int size() {
        try {
            Set<AbstractServerChessGame> gameSet = new HashSet<>();
            gameSet.addAll(this.gameMap.values());
            gameSet.addAll(this.serverChessGameDAO.findEntities());
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
    public AbstractServerChessGame get(Object key) {
        try {
            if (key != null && key instanceof Long) {
                AbstractServerChessGame game = gameMap.get(key);
                if (game == null) {
                    game = this.serverChessGameDAO.getServerChessGame((Long) key);
                    if (game == null) {
                        logger.error("AbstractServerChessGame with uid:" + key + " doesn't exist!");
                        return null;
                    } else {
                        serverChessGameDAO.detachEntity(game);
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
    public AbstractServerChessGame put(Long key, AbstractServerChessGame value) {
        return gameMap.putIfAbsent(key, value);
    }

    @Override
    public AbstractServerChessGame remove(Object key) {
        AbstractServerChessGame gameToDelete = this.get(key);
        if(gameToDelete == null) {
            logger.debug("No AbstractServerChessGame to remove");
        } else {
            try {
                gameMap.remove(key);
                serverChessGameDAO.deleteEntity(gameToDelete);
            } catch (DAOException de) {
                logger.error("GameMapRemove: Game in Database newer");
                logger.error(de);
                try {
                    AbstractServerChessGame game = serverChessGameDAO.getServerChessGame(gameToDelete.getUid());
                    serverChessGameDAO.deleteEntity(game);
                    logger.info(String.format("ServerChessGame(%d) has been removed", gameToDelete.getUid()));
                } catch (DAOException de2) {
                    logger.error(de2);
                }
                    
            }
        }
        return gameToDelete;
    }

    
    /*
     * Not implemented
     */
    @Override
    public void putAll(Map<? extends Long, ? extends AbstractServerChessGame> m) {
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
        keys.addAll(gameMap.keySet());
        keys.addAll(serverChessGameDAO.getGameUids());
        return keys;
    }

    @Override
    public Collection<AbstractServerChessGame> values() {
        try {
            Set<AbstractServerChessGame> games = new HashSet<>(gameMap.values());
            games.addAll(serverChessGameDAO.findEntities());
            return games;
        } catch(DAOException de) {
            logger.error(de);
        }
        return Collections.emptyList();
    }

    @Override
    public Set<Entry<Long, AbstractServerChessGame>> entrySet() {
        Set<Map.Entry<Long, AbstractServerChessGame>> gameSet = new HashSet<>();
        Collection<AbstractServerChessGame> games = values();
        for(AbstractServerChessGame game: games) {
            gameSet.add(new AbstractMap.SimpleEntry<Long,AbstractServerChessGame>(game.getUid(), game));
        }
        return gameSet;
    }
    
    @Override
    public AbstractServerChessGame putIfAbsent(Long gameUID, AbstractServerChessGame serverChessGame) {
        return gameMap.putIfAbsent(gameUID, serverChessGame);
    }

    @Override
    public boolean remove(Object gameUID, Object serverChessGame) {
        return gameMap.remove(gameUID, serverChessGame);
    }

    @Override
    public boolean replace(Long gameUID, AbstractServerChessGame oldServerChessGame, AbstractServerChessGame newServerChessGame) {
        serverChessGameDAO.detachEntity(newServerChessGame);
        return gameMap.replace(gameUID, oldServerChessGame, newServerChessGame);
    }
    
    /**
     * Clears the cache. 
     * ServerChessGames in the cache may not be persisted to the database.
     */
    public void clearCache() {
        gameMap.clear();
    }
    
    public AbstractServerChessGame replace(Long gameUid, AbstractServerChessGame serverChessGame) {
        serverChessGameDAO.detachEntity(serverChessGame);
        return gameMap.replace(gameUid, serverChessGame);
    }
    /**
     * Retrieve a collection of AbstractServerChessGames from the cache
     * @return Collection of AbstractServerChessGames
     */
    public Collection<AbstractServerChessGame> cacheValues() {
        return gameMap.values();
    }
       
    public void setServerChessGameDAO(ServerChessGameDAO serverChessGameDAO) {
        this.serverChessGameDAO = serverChessGameDAO;
    }
    
    void setInternalHashMap(ConcurrentMap<Long, AbstractServerChessGame> gameMap) {
        this.gameMap = gameMap;
    }
}
