package org.amc.game.chessserver.observers;

import org.amc.DAOException;
import org.amc.dao.DatabaseGameMap;
import org.amc.game.GameObserver;
import org.amc.game.chess.ChessGame;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.util.Subject;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;
import javax.persistence.OptimisticLockException;

public class DatabaseUpdateListener extends GameObserver {

    private static final Logger logger = Logger.getLogger(DatabaseUpdateListener.class);
    
    @Resource(name = "gameMap")
    private ConcurrentMap<Long, AbstractServerChessGame> gameMap;
 
    public DatabaseUpdateListener() {
    }
    
    @Override
    public void update(Subject subject, Object message) {
        logger.info(logger.getName() + ": Message received from " + 
                        subject);
        if(subject instanceof AbstractServerChessGame){
            AbstractServerChessGame scg = (AbstractServerChessGame)subject;
            if(message instanceof ServerGameStatus || message instanceof ChessGame) {
                try {
                    updateDatabaseAndCache(scg);
                } catch(DAOException | OptimisticLockException de) {
                    logger.error(logger.getName() + ": update to Database failed");
                    logger.error(logger.getName() + ":" + de);
                    
                }
            }
        }
    }
    
    private void updateDatabaseAndCache(AbstractServerChessGame scg) throws DAOException {
        if(gameMap instanceof DatabaseGameMap) {
            DatabaseGameMap dgMap = (DatabaseGameMap)gameMap;
            gameMap.replace(scg.getUid(), dgMap.getServerChessGameDAO().updateEntity(scg));
        } else 
        {
            logger.error("GameMap isn't an instance of DatabaseGameMap");
        }
    }
    
    public void setGameMap(ConcurrentMap<Long, AbstractServerChessGame> gameMap) {
        this.gameMap = gameMap;
    }
}
