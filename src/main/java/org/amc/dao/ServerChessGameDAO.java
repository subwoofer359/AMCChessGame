package org.amc.dao;

import org.amc.DAOException;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * A DAO for ServerChessGames persisted to a database
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ServerChessGameDAO extends DAO<ServerChessGame> {

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
        Set<Long> gameUidsSet = new HashSet<Long>(query.getResultList());
    
        return gameUidsSet;
    }
    
    private void addObservers(ServerChessGame serverChessGame) throws DAOException {
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
    public ServerChessGame getServerChessGame(long uid) throws DAOException {
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNamedQuery(GET_SERVERCHESSGAME_QUERY);
        query.setParameter(1, uid);
        
        ServerChessGame scg = (ServerChessGame)query.getSingleResult();
        
        addObservers(scg);
        
        return scg;
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
