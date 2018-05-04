package org.amc.dao;

import org.amc.DAOException;
import org.apache.log4j.Logger;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

/**
 * 
 * Represents a general Data Access Object to provide the link between the
 * business logic and database. Fetches and holds a reference to the Persistence
 * EntityManager
 * 
 * @author Adrian Mclaughlin
 * @version 1.1
 * @param <T> Entity
 */
public class DAO<T> implements DAOInterface<T> {

	//Error messages
	private static final String PERSIST_ERROR  = "DAO<%s>: Error has occurred when trying to persist entity";
    
    private static final String DELETE_ERROR = "DAO<%s>: Error has occurred when trying to delete entity";
    
    private static final String FIND_ERROR = "DAO<%s>: Error has occurred when trying to find entities";
    
    private static final String ENTITY_NOT_FOUND = "DAO<%s>: Error has occurred when trying to retrieve entity. "
    		+ "The entity should exist in the database but it doesn't";
    
    private static final String RETRIEVE_ERROR = "DAO<%s>: Error has occurred when trying to retrieve entity";
    
    private static final String MERGE_ERROR = "DAO<%s>: Error has occurred when trying to merge entity into the persistence context";

    private static final String NOT_ENTITY_ERROR = "DAO: is not an entity";
    
    //Queries
    private static final String FIND_BY_ID = "Select x from %s x where x.id = ?1";
    
    private static final String FIND_BY_COL = "Select x from %s x where x.%s = ?1";
    
    private static final String FIND_ALL = "Select x from %s x";
	
	/**
     * Logger used by the object
     */
    private static final Logger LOGGER = Logger.getLogger(DAO.class);
    
    /**
     * The class this DAO is handling
     */
    private final Class<T> entityClass;
    
    private EntityManager entityManager;

    public DAO(Class<T> entityClass) {
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
        	logErrorMsg(PERSIST_ERROR);
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
        	logErrorMsg(DELETE_ERROR);
            throw new DAOException(pe);
        }
    }

    /* (non-Javadoc)
     * @see org.amc.dao.DAOInterface#findEntities()
     */
    @Override
    public List<T> findEntities() throws DAOException {
    	TypedQuery<T> query = getEntityManager().createQuery(getSimpleQuery(FIND_ALL), entityClass);
    	
        try {		
        	return query.getResultList();
        } catch (PersistenceException pe) {
        	logErrorMsg(FIND_ERROR);
            throw new DAOException(pe);
        }
    }

    /* (non-Javadoc)
     * @see org.amc.dao.DAOInterface#findEntities(java.lang.String, java.lang.Object)
     */
    @Override
    public List<T> findEntities(String col, Object value) throws DAOException {

    	String FIND_QUERY = String.format(FIND_BY_COL, entityClass.getSimpleName(), col);
        TypedQuery<T> query = getEntityManager().createQuery(FIND_QUERY, entityClass);
        
        try {
            query.setParameter(1, value);
            LOGGER.debug(query.toString());
            return query.getResultList();
        } catch (PersistenceException pe) {
        	logErrorMsg(FIND_ERROR);
            throw new DAOException(pe);
        }

    }


    /* (non-Javadoc)
     * @see org.amc.dao.DAOInterface#getEntity(int)
     */
    @Override
    public T getEntity(int id) throws DAOException {
 
        TypedQuery<T> query = getEntityManager().createQuery(getSimpleQuery(FIND_BY_ID), entityClass);
                       
        try {
            query.setParameter(1, id);
            return query.getSingleResult();
        } catch (NoResultException nre) {
        	logErrorMsg(ENTITY_NOT_FOUND);
            throw new DAOException(nre);
        } catch (PersistenceException pe) {
        	logErrorMsg(RETRIEVE_ERROR);
            throw new DAOException(pe);
        }

    }

    /**
     * Returns a <code>Class</code> object of class <code>Entity</code>
     * which this DAO object has been initialised with
     * 
     * @return Class object which this DAO is handling
     */
    @Override
    public Class<? extends T> getEntityClass() {
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
            logErrorMsg(MERGE_ERROR);
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
            LOGGER.error(NOT_ENTITY_ERROR);
        }
    }
    
    private void logErrorMsg(String error) {
    	LOGGER.error(String.format(error, this.entityClass.getSimpleName()));
    }
    
    private String getSimpleQuery(String query) {
    	return String.format(query, entityClass.getSimpleName());
    }

}
