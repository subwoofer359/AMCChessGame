package org.amc.dao;

import org.amc.DAOException;
import org.amc.User;

import org.amc.game.chessserver.AbstractServerChessGame;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ManagedDAOFactory {
    
    private EntityManagerFactory emFactory;
    
    public DAOInterface<?> getUserDAO() {
        ManagedDAO<User> userDAO = new ManagedDAO<>(new DAO<User>(User.class));
        userDAO.setEntityManagerFactory(emFactory);
        return userDAO;
    }
    
    public DAOInterface<?> getServerChessGameDAO() {
        ManagedDAO<AbstractServerChessGame> serverChessGameDAO = new ManagedDAO<>(
                        new DAO<AbstractServerChessGame>(AbstractServerChessGame.class));
        serverChessGameDAO.setEntityManagerFactory(emFactory);
        return serverChessGameDAO;
    }
    
    public void setEmFactory(EntityManagerFactory emFactory) {
        this.emFactory = emFactory;
    }
    
    static class ManagedDAO<T> implements DAOInterface<T> {
        private EntityManagerFactory entityManagerFactory;
        private EntityManager entityManager;
        private DAOInterface<T> dao;
        
        public ManagedDAO(DAOInterface<T> dao) {
            this.dao = dao;
            
        }

        public void addEntity(T entity) throws DAOException {
            setUpEntityManager();
            dao.addEntity(entity);
            entityManager.close();
        }


        public void deleteEntity(T entity) throws DAOException {
            setUpEntityManager();
            dao.deleteEntity(entity);
            entityManager.close();
        }

        public List<T> findEntities() throws DAOException {
            setUpEntityManager();
            List<T> entityList = dao.findEntities();
            entityManager.close();
            return entityList;
        }

        public List<T> findEntities(String col, Object value) throws DAOException {
            setUpEntityManager();
            List<T> entityList = dao.findEntities(col, value);
            entityManager.close();
            return entityList;
        }

        public T getEntity(int id) throws DAOException {
            setUpEntityManager();
            T entity = dao.getEntity(id);
            entityManager.close();
            return entity;
        }

        public T updateEntity(T entity) throws DAOException {
            setUpEntityManager();
            T entityTemp = dao.updateEntity(entity);
            entityManager.close();
            return entityTemp;
        }

        public void detachEntity(T entity) {
            setUpEntityManager();
            dao.detachEntity(entity);
            entityManager.close();
        }
        
        public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
            this.entityManagerFactory = entityManagerFactory;
        }

        public EntityManager getEntityManager() {
            entityManager = entityManagerFactory.createEntityManager();
            dao.setEntityManager(entityManager);
            return dao.getEntityManager();
        }

        public void setEntityManager(EntityManager entityManager) {
            dao.setEntityManager(entityManager);
        }
        
        public void setUpEntityManager() {
            entityManager = entityManagerFactory.createEntityManager();
            dao.setEntityManager(entityManager);
        }

        @Override
        public Class<?> getEntityClass() {
            return dao.getEntityClass();
        }
        
        DAOInterface<T> getDAO() {
            return this.dao;
        }
    }
}
