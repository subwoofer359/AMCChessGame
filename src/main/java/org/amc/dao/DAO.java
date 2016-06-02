package org.amc.dao;

import org.amc.DAOException;
import org.amc.game.chess.Player;
import org.apache.log4j.Logger;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
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
public class DAO<T> implements DAOInterface<T> {

    /**
     * Logger used by the object
     */
    private static final Logger LOG = Logger.getLogger(DAO.class);

    /**
     * The class this DAO is handling
     */
    private final Class<?> entityClass;
    
    private EntityManager entityManager;

    public DAO(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    /* (non-Javadoc)
     * @see org.amc.dao.DAOInterface#addEntity(T)
     */
    @Override
    public void addEntity(T entity) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (OptimisticLockException ole) {
            throw ole;
        } catch (PersistenceException pe) {
            LOG.error("DAO<" + entityClass.getSimpleName()
                            + ">:Error has occurred when trying to persist entity");
            throw new DAOException(pe);
        }
    }

    /* (non-Javadoc)
     * @see org.amc.dao.DAOInterface#deleteEntity(T)
     */
    @Override
    public void deleteEntity(T entity) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T entityToBeRemoved = em.merge(entity);
            em.remove(entityToBeRemoved);
            em.getTransaction().commit();
        } catch (OptimisticLockException ole) {
            throw ole;
        } catch (PersistenceException pe) {
            em.close();
            LOG.error("DAO<" + entityClass.getSimpleName()
                            + ">:Error has occurred when trying to delete entity");
            throw new DAOException(pe);
        }
    }

    /* (non-Javadoc)
     * @see org.amc.dao.DAOInterface#findEntities()
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntities() throws DAOException {
        try {
            Query query = getEntityManager().createQuery(
                            "Select x from " + entityClass.getSimpleName() + " x", entityClass);
            List<T> resultList = query.getResultList();
            return resultList;
        } catch (PersistenceException pe) {
            LOG.error("DAO<" + entityClass.getSimpleName()
                            + ">:Error has occurred when trying to find entities");
            throw new DAOException(pe);
        }
    }

    /* (non-Javadoc)
     * @see org.amc.dao.DAOInterface#findEntities(java.lang.String, java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntities(String col, Object value) throws DAOException {

        try {
            Query query = getEntityManager().createQuery(
                            "Select x from " + entityClass.getSimpleName() + " x where x." + col
                                            + " = ?1", entityClass);
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


    /* (non-Javadoc)
     * @see org.amc.dao.DAOInterface#getEntity(int)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T getEntity(int id) throws DAOException {

        Query query = getEntityManager().createQuery(
                        "Select x from " + entityClass.getSimpleName() + " x where x.id = ?1", entityClass);
        try {
            query.setParameter(1, id);
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
    @Override
    public Class<?> getEntityClass() {
        return this.entityClass;
    }

    /* (non-Javadoc)
     * @see org.amc.dao.DAOInterface#getEntityManager()
     */
    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
    
    /* (non-Javadoc)
     * @see org.amc.dao.DAOInterface#setEntityManager(javax.persistence.EntityManager)
     */
    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
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

    /* (non-Javadoc)
     * @see org.amc.dao.DAOInterface#updateEntity(T)
     */
    @Override
    public T updateEntity(T entity) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T mergedEntity = em.merge(entity);
            em.getTransaction().commit();
            return mergedEntity;
        } catch (OptimisticLockException ole) {
            throw ole;
        } catch (PersistenceException pe) {
            //em.close();
            LOG.error("DAO<"
                            + entityClass.getSimpleName()
                            + ">:Error has occurred when trying to merge entity into the persistence context");
            throw new DAOException(pe);
        }

    }
    
    /* (non-Javadoc)
     * @see org.amc.dao.DAOInterface#detachEntity(T)
     */
    @Override
    public void detachEntity(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.detach(entity);
        } catch (IllegalArgumentException iae) {
            LOG.error("DAO: is not an entity");
        }
    }

}
