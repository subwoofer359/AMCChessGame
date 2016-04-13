package org.amc.dao;

import org.amc.DAOException;
import org.amc.game.chess.Player;
import org.amc.game.chess.StandardChessGameFactory;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.apache.log4j.Logger;
import org.apache.openjpa.persistence.OpenJPAEntityManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * A DAO for ServerChessGames persisted to a database
 * 
 * @author Adrian Mclaughlin
 *
 */

public class ServerChessGameDAO extends DAO<AbstractServerChessGame> {

    private static final Logger logger = Logger.getLogger(ServerChessGameDAO.class);
    
    private static final String GET_SERVERCHESSGAME_QUERY = "serverChessGameByUid";
    private static final String NATIVE_OBSERVERS_QUERY = "Select uid,observers from serverChessGames where uid = ?1";
    
    private ObserverFactoryChain chain;
    
    public ServerChessGameDAO() {
        super(ServerChessGame.class);
    }
    
    public Set<Long> getGameUids() {
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery("Select x.uid from " + getEntityClass().getSimpleName() + " x ORDER BY x.uid");
        try {
        	@SuppressWarnings("unchecked")
            Set<Long> gameUidsSet = new HashSet<Long>(query.getResultList());
        	return gameUidsSet;
        } catch(PersistenceException pe) {
        	logger.error(pe);
        	return Collections.<Long>emptySet();
        }
        
    }
    
    private void addObservers(AbstractServerChessGame serverChessGame) throws DAOException {
        EntityManager entityManager = getEntityManager();
        if(serverChessGame.getNoOfObservers() == 0) {
            Query query = entityManager.createNativeQuery(NATIVE_OBSERVERS_QUERY, SCGObservers.class);
            query.setParameter(1, serverChessGame.getUid());
            try {
                SCGObservers scgObervers = (SCGObservers)query.getSingleResult();
                chain.addObserver(scgObervers.getObservers(), serverChessGame);
            } catch(NoResultException nre) {
                logger.error(nre);
            }
        }
    }
    
    /**
     * 
     * @param uid unique identifier of the ServerChessGame to be retrieved
     * @return ServerChessGame with Observers attached
     * @throws DAOException if the ServerChessGame can't be retrieved
     */
    public AbstractServerChessGame getServerChessGame(long uid) throws DAOException {
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNamedQuery(GET_SERVERCHESSGAME_QUERY);
        query.setParameter(1, uid);
        
        AbstractServerChessGame scg = null;
        try {
            scg = (AbstractServerChessGame)query.getSingleResult();
            addObservers(scg);
            scg.setChessGameFactory(new StandardChessGameFactory());
            return scg;
        } catch(NoResultException nre) {
            logger.error(nre);
        }
        return scg;
    }
    
    public AbstractServerChessGame saveServerChessGame(AbstractServerChessGame serverChessGame) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            if(em.contains(serverChessGame)) {
                
            } else {
                
                serverChessGame = em.merge(serverChessGame);
            }
            
            if(em instanceof OpenJPAEntityManager) {
                ((OpenJPAEntityManager)em).dirty(serverChessGame.getChessGame(), "board");
            }
            em.flush();
            em.getTransaction().commit();
            return serverChessGame;
        } catch (PersistenceException pe) {
            logger.error(pe);
            em.close();
            throw new DAOException(pe);
        }
    }
    
    /**
     * Returns Games for display purposes only. 
     * Certain fields aren't initialised
     * If a fully initialised is required use {@link #getServerChessGame(long)}
     * 
     * @param player
     * @return Map Of partially initialised ServerChessGames
     * @throws DAOException if database operation raises an exception 
     */
    public Map<Long, ServerChessGame> getGamesForPlayer(Player player) throws DAOException {
        EntityManager em = getEntityManager();
        TypedQuery<ServerChessGame> query = em.createNamedQuery("getChessGamesByPlayer", 
                        ServerChessGame.class);
        query.setParameter(1, player.getUserName());
        try {
            Map<Long, ServerChessGame> games = new HashMap<Long, ServerChessGame>();
            List<ServerChessGame> results = query.getResultList();
            for(ServerChessGame game : results) {
                games.put(game.getUid(), game);
            }
            return  games;
        } catch (PersistenceException pe) {
            logger.error(pe);
            em.close();
            throw new DAOException(pe);
        }
    }
    
    @Resource(name="defaultObserverFactoryChain")
    public void setObserverFactoryChain(ObserverFactoryChain chain) {
        this.chain = chain;
    }

    /**
     * Helper Class to retrieve the list of observers in String
     * format in the database
     * @author Adrian Mclaughlin
     *
     */
    public static class SCGObservers {
        private String observers;
        private Long uid;

        public String getObservers() {
            return observers;
        }


        public Long getUid() {
            return uid;
        }


        public void setObservers(String observers) {
            this.observers = observers;
        }


        public void setUid(Long uid) {
            this.uid = uid;
        }
        
        
        
    }
}
