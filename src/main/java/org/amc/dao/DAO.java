package org.amc.dao;

import org.amc.DAOException;
import org.amc.EntityManagerThreadLocal;
import org.amc.game.chess.Player;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 * 
 * Represents a general Data Access Object to provide the link between the
 * business logic and database. Fetches and holds a reference to the Persistence
 * EntityManager
 * 
 * @author Adrian Mclaughlin
 * @version 1
 * @param <T> WorkEntity
 */
public class DAO<T> implements Serializable {
    /**
     * Serializable
     */
    private static final long serialVersionUID = 854157422459241714L;

    /**
     * Logger used by the object
     */
    private static final Logger LOG = Logger.getLogger(DAO.class);

    /**
     * The class this DAO is handling
     */
    private final Class<?> entityClass;

    public DAO(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Adds the {@link Player} object to the database
     * 
     * @param entity
     *            the new <code>Player</code> to be stored in the database
     * @throws DAOException
     *             if a problem occurs in the underlying database
     */
    public void addEntity(T entity) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (PersistenceException pe) {
            em.getTransaction().rollback();
            em.close();
            LOG.error("DAO<" + entityClass.getSimpleName()
                            + ">:Error has occurred when trying to persist entity");
            throw new DAOException(pe);
        }
    }

    /**
     * Deletes the {@link Player} object from the database
     * 
     * @param entity
     *            the <code>Player</code> to be deleted
     * @throws DAOException
     *             if a problem occurs in the underlying database
     */
    public void deleteEntity(T entity) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T entityToBeRemoved = em.merge(entity);
            em.remove(entityToBeRemoved);
            em.getTransaction().commit();
        } catch (PersistenceException pe) {
            em.close();
            LOG.error("DAO<" + entityClass.getSimpleName()
                            + ">:Error has occurred when trying to delete entity");
            throw new DAOException(pe);
        }
    }

    /**
     * Fetchs a list of all objects of {@link Player} stored in the
     * underlying database. The results from the database are stored in a
     * <code>java.util.List</code>.
     * <p>
     * Caution if there are a lot of rows in the database if may return a very
     * large <code>Collection</code> of objects.
     * 
     * @return List of all WorkEntities in the database
     * @throws DAOException
     *             if a problem occurs in the underlying database
     * @deprecated as of 30/09/14, no replacement.
     */
    @SuppressWarnings("unchecked")
    public List<T> findEntities() throws DAOException {
        try {
            Query query = getEntityManager().createQuery(
                            "Select x from " + entityClass.getSimpleName() + " x");
            List<T> resultList = query.getResultList();
            return resultList;
        } catch (PersistenceException pe) {
            LOG.error("DAO<" + entityClass.getSimpleName()
                            + ">:Error has occurred when trying to find entities");
            throw new DAOException(pe);
        }
    }

    /**
     * Fetchs a list of objects of class {@link Player} stored in the
     * underlying database. The results from the database are stored in a
     * <code>java.util.List</code>.
     * 
     * @param col
     *            String name of the column in the database to query
     * @param value
     *            Object to query for in the database
     * @return List of WorkEntities
     * @throws DAOException
     *             if a problem occurs in the underlying database
     */
    @SuppressWarnings("unchecked")
    public List<T> findEntities(String col, Object value) throws DAOException {

        try {
            Query query = getEntityManager().createQuery(
                            "Select x from " + entityClass.getSimpleName() + " x where x." + col
                                            + " = ?1");
            query.setParameter(1, value);
            LOG.debug(query.toString());
            List<T> resultList = query.getResultList();
            return resultList;
        } catch (PersistenceException pe) {
            LOG.error("DAO<" + entityClass.getSimpleName()
                            + ">:Error has occurred when trying to find entities");
            throw new DAOException(pe);
        }

    }


    /**
     * Retrieves a {@link Player} object who's database ID value is equal to
     * <code>workEntityId</code>
     * <p>
     * A DAOException is thrown if there is no entry in the database with the
     * given <code>workEntityId</code>.
     * 
     * @param workEntityId
     *            The String representation of the number corresponding to the
     *            ID of the <code>Player</code> in the database.
     * @return a Player object
     * @throws DAOException
     *             if a problem occurs in the underlying database
     */
    @SuppressWarnings("unchecked")
    public T getEntity(int id) throws DAOException {

        Query query = getEntityManager().createQuery(
                        "Select x from " + entityClass.getSimpleName() + " x where x.id="
                                        + id + "");
        try {
            T mp = (T) query.getSingleResult();
            return mp;
        } catch (NoResultException nre) {
            LOG.error("DAO<"
                            + entityClass.getSimpleName()
                            + ">:Error has occurred when trying to retrive entity. The entity should exist in the database but it doesn't");
            throw new DAOException(nre);
        } catch (PersistenceException pe) {
            LOG.error("DAO<" + entityClass.getSimpleName()
                            + ">:Error has occurred when trying to retrive entity");
            throw new DAOException(pe);
        }

    }

    /**
     * Returns a <code>Class</code> object of class <code>Player</code>
     * which this DAO object has been initialised with
     * 
     * @return Class object which this DAO is handling
     * @see Player
     */
    protected Class<?> getEntityClass() {
        return this.entityClass;
    }

    /**
     * Provides access to {@link EntityManagerThreadLocal} which contains the
     * reference for the {@link EntityManager}
     * 
     * @return EntityManager for this class subclasses to use
     * @see EntityManager
     * @see EntityManagerThreadLocal
     */
    public EntityManager getEntityManager() {
        return EntityManagerThreadLocal.getEntityManager();
    }

    /**
     * Overrides <code>Object</code> class <code>toString</code> method
     * 
     * @return String representation
     */
    @Override
    public String toString() {
        return "DAO<" + getEntityClass().getSimpleName() + ">";
    }

    /**
     * Updates the current {@link Player} object in the database.
     * 
     * @param entity
     *            the <code>Player</code> to be updated.
     * @throws DAOException
     *             if a problem occurs in the underlying database
     */
    public void updateEntity(T entity) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (PersistenceException pe) {
            em.getTransaction().rollback();
            em.close();
            LOG.error("DAO<"
                            + entityClass.getSimpleName()
                            + ">:Error has occurred when trying to merge entity into the persistence context");
            throw new DAOException(pe);
        }

    }

}
