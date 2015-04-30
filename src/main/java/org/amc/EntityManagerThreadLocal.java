package org.amc;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author Adrian Mclaughlin
 * @version 1
 *
 */
public final class EntityManagerThreadLocal {
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
        return ENTITYMANAGER.get();
    }

    /**
     * Called to tidy up and release resources held by the EntityManager
     */
    public static void closeEntityManager() {
        ENTITYMANAGER.get().close();
        ENTITYMANAGER.remove();
    }

    /**
     * Constructor not to be called
     */
    private EntityManagerThreadLocal() {
        throw new AssertionError(this.getClass().getSimpleName() + " is an utility class");
    }

}
