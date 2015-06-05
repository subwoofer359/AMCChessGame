package org.amc;

import org.amc.game.chessserver.EntityManagerFilter;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author Adrian Mclaughlin
 * @version 1
 *
 */
public final class EntityManagerThreadLocal {
    
    private static Logger logger = Logger.getLogger(EntityManagerThreadLocal.class);
    /**
     * JPA EntityManagerFactory Set up Spring IOC Thread safe
     */
    private static EntityManagerFactory FACTORY;

    /**
     * ThreadLocal to be used by different threads
     */
    private final static ThreadLocal<EntityManager> ENTITYMANAGER = new ThreadLocal<EntityManager>() {

        /**
         * @return EntityManager
         */
        @Override
        protected EntityManager initialValue() {
            logger.debug("EntityManager created");
            return FACTORY.createEntityManager();
        }

    };

    /**
     * @param factory
     */
    public static void setEntityManagerFactory(EntityManagerFactory factory) {
        EntityManagerThreadLocal.FACTORY = factory;
    }

    /**
     * @return an JPA EntityManager
     */
    public static EntityManager getEntityManager() {
        logger.debug("Object retrieved EntityManager instance with get");
        return ENTITYMANAGER.get();
    }

    /**
     * Called to tidy up and release resources held by the EntityManager
     */
    public static void closeEntityManager() {
        ENTITYMANAGER.get().close();
        ENTITYMANAGER.remove();
        logger.debug("EntityManager closed");
    }

    /**
     * Constructor not to be called
     */
    private EntityManagerThreadLocal() {
        throw new AssertionError(this.getClass().getSimpleName() + " is an utility class");
    }

}
