package org.amc.dao;

import org.amc.DAOException;
import org.amc.game.chessserver.ServerChessGame;
import org.apache.log4j.Logger;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DatabaseGameMap implements Map<Long, ServerChessGame> {

    ServerChessGameDAO chessGameDAO;
    private static final Logger logger = Logger.getLogger(DatabaseGameMap.class);
    
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
                return this.chessGameDAO.findEntities("uid", (Long)key).size() == 1;
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
            if(key != null && key instanceof Long){
                List<ServerChessGame> games = this.chessGameDAO.findEntities("uid", (Long)key);
                if(games.size() == 1) {
                    return games.get(0);
                }
                else {
                    logger.error("The key should retrieve only one ServerChessGame but it retrieved:" + games.size());
                    return null;
                }
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
           
        } else {
            try {
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

    /*
     * Not implemented
     */
    @Override
    public void clear() {
        logger.error("DatabaseGameMap.clear() is not implemented");
        throw new UnsupportedOperationException();
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
        return null;
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
    
}
