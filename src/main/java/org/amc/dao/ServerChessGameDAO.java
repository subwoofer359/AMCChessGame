package org.amc.dao;

import org.amc.DAOException;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.apache.log4j.Logger;
import org.apache.openjpa.persistence.OpenJPAEntityManager;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

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
    
    public ServerChessGame getServerChessGame(long uid) throws DAOException {
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNamedQuery(GET_SERVERCHESSGAME_QUERY);
        query.setParameter(1, uid);
        
        ServerChessGame scg = (ServerChessGame)query.getSingleResult();
        
        addObservers(scg);
        
        if(entityManager instanceof OpenJPAEntityManager) {
            if(scg.getChessGame() != null) {
                ((OpenJPAEntityManager)getEntityManager()).dirty(scg.getChessGame(), "board");
            }
        } else {
            throw new DAOException("Not an OpenJPA entity manager: changes to chessboard won't be saved");
        }
        
        return scg;
    }
    
    @Resource(name="defaultObserverFactoryChain")
    public void setObserverFactoryChain(ObserverFactoryChain chain) {
        this.chain = chain;
    }

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
