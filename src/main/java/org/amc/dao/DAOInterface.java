package org.amc.dao;

import org.amc.DAOException;
import org.amc.game.chess.Player;

import java.util.List;

import javax.persistence.EntityManager;

public interface DAOInterface<T> {

    /**
     * Adds the {@link Player} object to the database
     * 
     * @param entity
     *            the new <code>Player</code> to be stored in the database
     * @throws DAOException
     *             if a problem occurs in the underlying database
     */
    void addEntity(T entity) throws DAOException;

    /**
     * Deletes the {@link Player} object from the database
     * 
     * @param entity
     *            the <code>Player</code> to be deleted
     * @throws DAOException
     *             if a problem occurs in the underlying database
     */
    void deleteEntity(T entity) throws DAOException;

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
     * 
     */
    List<T> findEntities() throws DAOException;

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
    List<T> findEntities(String col, Object value) throws DAOException;

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
    T getEntity(int id) throws DAOException;

    /**
     * Provides access to {@link EntityManagerThreadLocal} which contains the
     * reference for the {@link EntityManager}
     * 
     * @return EntityManager for this class subclasses to use
     * @see EntityManager
     * @see EntityManagerThreadLocal
     */
    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    /**
     * Updates the current {@link Player} object in the database.
     * 
     * @param entity
     *            the <code>Player</code> to be updated.
     * @throws DAOException
     *             if a problem occurs in the underlying database
     */
    T updateEntity(T entity) throws DAOException;

    /**
     * Detaches Entity from the Persistence context
     *
     * @param entity
     * @see EntityManager#detach(Object)
     */
    void detachEntity(T entity);
    
    
    /**
     * Returns a <code>Class</code> object of class <code>Player</code>
     * which this DAO object has been initialised with
     * 
     * @return Class object which this DAO is handling
     * @see Player
     */
    Class<? extends T> getEntityClass();

}