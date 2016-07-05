package org.amc.dao;

import org.amc.DAOException;
import org.amc.game.chess.Player;
import org.amc.game.chess.StandardChessGameFactory;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.apache.log4j.Logger;
import org.apache.openjpa.persistence.OpenJPAEntityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

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

public class ServerChessGameDAO extends DAO<AbstractServerChessGame> implements SCGDAOInterface {

    private static final Logger logger = Logger.getLogger(ServerChessGameDAO.class);

    static final String GET_SERVERCHESSGAME_QUERY = "serverChessGameByUid";

    static final String NATIVE_OBSERVERS_QUERY = "Select uid,observers from serverChessGames where uid = ?1";

    private ObserverFactoryChain chain;

    private EntityManagerCache entityManagerCache;

    public ServerChessGameDAO() {
        super(ServerChessGame.class);
    }

    void addObservers(AbstractServerChessGame serverChessGame) throws DAOException {
        EntityManager entityManager = getEntityManager(serverChessGame.getUid());
        synchronized (entityManager) {
            addObserversIfNone(entityManager, serverChessGame);
        }
    }

    private void addObserversIfNone(EntityManager entityManager,
                    AbstractServerChessGame serverChessGame) throws DAOException {
        if (serverChessGame.getNoOfObservers() == 0) {
            Query query = entityManager.createNativeQuery(NATIVE_OBSERVERS_QUERY,
                            SCGObservers.class);
            query.setParameter(1, serverChessGame.getUid());
            queryAndAddObservers(query, serverChessGame);
        }
    }

    private void queryAndAddObservers(Query query, AbstractServerChessGame serverChessGame)
                    throws DAOException {
        try {
            SCGObservers scgObervers = (SCGObservers) query.getSingleResult();
            chain.addObserver(scgObervers.getObservers(), serverChessGame);
        } catch (PersistenceException pe) {
            logger.error(pe);
            throw new DAOException(pe);
        }
    }

    /**
     * 
     * @param uid
     *            unique identifier of the ServerChessGame to be retrieved
     * @return ServerChessGame with Observers attached
     * @throws DAOException
     *             if the ServerChessGame can't be retrieved
     */
    public AbstractServerChessGame getServerChessGame(long uid) throws DAOException {
        EntityManager entityManager = getEntityManager(uid);
        synchronized (entityManager) {
            return getSCGame(entityManager, uid);
        }
    }

    private AbstractServerChessGame getSCGame(EntityManager entityManager, long uid)
                    throws DAOException {
        Query query = entityManager.createNamedQuery(GET_SERVERCHESSGAME_QUERY);
        query.setParameter(1, uid);
        try {
            AbstractServerChessGame scg = (AbstractServerChessGame) query.getSingleResult();
            return configureServerChessGame(entityManager, scg);
        } catch (NoResultException nre) {
            logger.error(nre);
            throw new DAOException(nre);
        }
    }

    private AbstractServerChessGame configureServerChessGame(EntityManager entityManager,
                    AbstractServerChessGame serverChessGame) throws DAOException {
        markChessBoardFieldDirty(entityManager, serverChessGame);
        addObservers(serverChessGame);
        serverChessGame.setChessGameFactory(new StandardChessGameFactory());
        return serverChessGame;
    }

    private void markChessBoardFieldDirty(EntityManager entityManager, AbstractServerChessGame scg) {
        if (entityManager instanceof OpenJPAEntityManager) {
            ((OpenJPAEntityManager) entityManager).dirty(scg.getChessGame(), "board");
        }
    }

    public AbstractServerChessGame saveServerChessGame(AbstractServerChessGame serverChessGame)
                    throws DAOException {
        EntityManager entityManager = getEntityManager(serverChessGame.getUid());
        synchronized (entityManager) {
            try {
                return saveGame(entityManager, serverChessGame);
            } catch (PersistenceException pe) {
                logger.error(pe);
                throw new DAOException(pe);
            }
        }
    }

    private AbstractServerChessGame saveGame(EntityManager entityManager,
                    AbstractServerChessGame serverChessGame) {
    	
    	AbstractServerChessGame scg = serverChessGame;
        entityManager.getTransaction().begin();

        if (isNotInThePersistenceContext(entityManager, serverChessGame)) {
            scg = entityManager.merge(serverChessGame);
        }

        markChessBoardFieldDirty(entityManager, scg);
        entityManager.flush();
        entityManager.getTransaction().commit();
        return scg;
    }

    private boolean isNotInThePersistenceContext(EntityManager em,
                    AbstractServerChessGame serverChessGame) {
        return !em.contains(serverChessGame);
    }

    @Override
    public void deleteEntity(AbstractServerChessGame serverChessGame) throws DAOException {
        EntityManager entityManager = getEntityManager(serverChessGame.getUid());
        synchronized (entityManager) {
            entityManager.getTransaction().begin();
            entityManager.remove(serverChessGame);
            entityManager.getTransaction().commit();
        }
    }

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
    public Map<Long, ServerChessGame> getGamesForPlayer(Player player) throws DAOException {
        EntityManager em = getEntityManager();
        TypedQuery<ServerChessGame> query = em.createNamedQuery("getChessGamesByPlayer",
                        ServerChessGame.class);
        query.setParameter(1, player.getUserName());
        try {
            Map<Long, ServerChessGame> games = new HashMap<>();
            List<ServerChessGame> results = query.getResultList();
            for (ServerChessGame game : results) {
                games.put(game.getUid(), game);
            }
            return games;
        } catch (PersistenceException pe) {
            logger.error(pe);
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
     * @throws DAOException
     *             if database operation raises an exception
     */
    public Map<Long, ChessGameInfo> getGameInfoForPlayer(Player player) throws DAOException {
        EntityManager em = getEntityManager();
        TypedQuery<ChessGameInfo> query = em.createNamedQuery("getChessGameInfo",
                ChessGameInfo.class);
        query.setParameter(1, player.getUserName());
        try {
            Map<Long, ChessGameInfo> games = new WeakHashMap<>();
            List<ChessGameInfo> results = query.getResultList();
            for (ChessGameInfo game : results) {
                games.put(game.getUid(), game);
            }
            return games;
        } catch (PersistenceException pe) {
            logger.error(pe);
            throw new DAOException(pe);
        }
    }

    public EntityManager getEntityManager(Long gameUid) {
        return entityManagerCache.getEntityManager(gameUid);
    }

    @Resource(name = "defaultObserverFactoryChain")
    public void setObserverFactoryChain(ObserverFactoryChain chain) {
        this.chain = chain;
    }

    @Resource(name = "myEntityManagerCache")
    public void setEntityManagerCache(EntityManagerCache entityManagerCache) {
        this.entityManagerCache = entityManagerCache;
    }

    /**
     * Helper Class to retrieve the list of observers in String
     * format in the database
     * 
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
