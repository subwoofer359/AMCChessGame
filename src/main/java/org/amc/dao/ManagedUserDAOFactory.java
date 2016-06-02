package org.amc.dao;

import org.amc.DAOException;
import org.amc.User;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ManagedUserDAOFactory {
    
    private EntityManagerFactory emFactory;
    
    public DAO<?> getUserDAO() {
        EntityManager entityManager = emFactory.createEntityManager();
        DAO<User> userDAO = new DAO<>(User.class);
        userDAO.setEntityManager(entityManager);
        return userDAO;
    }
    
    static class ManagedUserDAO extends DAO<User> {
        private EntityManagerFactory entityManagerFactory;
        private EntityManager entityManager;
        
        public ManagedUserDAO() {
            super(User.class);
            
        }

        public void addEntity(User entity) throws DAOException {
            super.addEntity(entity);
            entityManager.close();
        }


        public void deleteEntity(User entity) throws DAOException {
            super.deleteEntity(entity);
            entityManager.close();
        }

        public List<User> findEntities() throws DAOException {
            List<User> userList = super.findEntities();
            entityManager.close();
            return userList;
        }

        public List<User> findEntities(String col, Object value) throws DAOException {
            List<User> userList = super.findEntities(col, value);
            entityManager.close();
            return userList;
        }

        public User getEntity(int id) throws DAOException {
            User user = super.getEntity(id);
            entityManager.close();
            return user;
        }

        public EntityManager getEntityManager() {
            entityManager = entityManagerFactory.createEntityManager();
            super.setEntityManager(entityManager);
            return super.getEntityManager();
        }

        public void setEntityManager(EntityManager entityManager) {
            super.setEntityManager(entityManager);
        }

        public User updateEntity(User entity) throws DAOException {
            User user = super.updateEntity(entity);
            entityManager.close();
            return user;
        }

        public void detachEntity(User entity) {
            super.detachEntity(entity);
            entityManager.close();
        }
        
        public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
            this.entityManagerFactory = entityManagerFactory;
        }
        
    }
       
}
