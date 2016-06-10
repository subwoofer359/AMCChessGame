package org.amc.dao;

import org.amc.DAOException;
import org.amc.User;

import org.amc.game.chessserver.AbstractServerChessGame;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Create a factory for DAO with the PersistenceContext controller by
 * a Decorator object
 * 
 * @author Adrian Mclaughlin
 *
 */
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
            try {
                dao.addEntity(entity);
            } finally {
                entityManager.close();
            }
        }


        public void deleteEntity(T entity) throws DAOException {
            setUpEntityManager();
            try {
                dao.deleteEntity(entity);
            } finally {
                entityManager.close();
            }
        }

        public List<T> findEntities() throws DAOException {
            setUpEntityManager();
            try {
                return dao.findEntities();
            } finally {
                entityManager.close();
            }
        }

        public List<T> findEntities(String col, Object value) throws DAOException {
            setUpEntityManager();
            try {
                return dao.findEntities(col, value);
            } finally {
                entityManager.close();
            }
        }

        public T getEntity(int id) throws DAOException {
            setUpEntityManager();
            try {
                return dao.getEntity(id);
            } finally {
                entityManager.close();
            }
        }

        public T updateEntity(T entity) throws DAOException {
            setUpEntityManager();
            try {
                return dao.updateEntity(entity);
            } finally {
                entityManager.close();
            }
            
        }

        public void detachEntity(T entity) {
            setUpEntityManager();
            try {
                dao.detachEntity(entity);
            } finally {
                entityManager.close();
            }            
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
