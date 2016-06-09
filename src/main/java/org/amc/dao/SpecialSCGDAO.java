package org.amc.dao;

import org.amc.DAOException;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.observers.ObserverFactoryChain;

import java.util.Map;

import javax.persistence.EntityManager;

public interface SpecialSCGDAO {
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

    /**
     * Retrieve specific EntityManager
     * If the EntityManager is closed a new EntityManager
     * will be created
     * @param gameUid key or identifier of EntityManager
     * @return Open EntityManager
     */
    EntityManager getEntityManager(Long gameUid);
    
    /**
     * Delete the Server Chess Game using the shared entityManager
     * @param serverChessGame to be deleted
     * @throws DAOException if game can't be deleted
     */
    void deleteEntity(AbstractServerChessGame serverChessGame) throws DAOException;

    /**
     * Set the Factory chain to create observers for each ServerChessGame
     * @param chain {@link ObserverFactoryChain}
     */
    void setObserverFactoryChain(ObserverFactoryChain chain);

    /**
     * Set the EntityManagerCache to be used to store shared entityManagers
     * @param entityManagerCache
     */
    void setEntityManagerCache(EntityManagerCache entityManagerCache);

}
