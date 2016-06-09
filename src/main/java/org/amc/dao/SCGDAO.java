package org.amc.dao;

import org.amc.DAOException;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.observers.ObserverFactoryChain;

import java.util.Map;

import javax.persistence.EntityManager;

public interface SCGDAO {
    /**
     * 
     * @param uid
     *            unique identifier of the ServerChessGame to be retrieved
     * @return ServerChessGame with Observers attached
     * @throws DAOException
     *             if the ServerChessGame can't be retrieved
     */
    AbstractServerChessGame getServerChessGame(long uid) throws DAOException;

    AbstractServerChessGame saveServerChessGame(AbstractServerChessGame serverChessGame)
                    throws DAOException;

    /**
     * Returns Games for display purposes only.
     * Certain fields aren't initialised
     * If a fully initialised is required use {@link #getServerChessGame(long)}
     * 
     * @param player
     * @return Map Of partially initialised ServerChessGames
     * @throws DAOException
     *             if database operation raises an exception
     */
    Map<Long, ServerChessGame> getGamesForPlayer(Player player) throws DAOException;

    EntityManager getEntityManager(Long gameUid);
    
    void deleteEntity(AbstractServerChessGame serverChessGame) throws DAOException;

    void setObserverFactoryChain(ObserverFactoryChain chain);

    void setEntityManagerCache(EntityManagerCache entityManagerCache);

}
