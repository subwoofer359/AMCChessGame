package org.amc.dao

import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.ManagedUserDAOFactory.ManagedUserDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import groovy.transform.TypeChecked;

@TypeChecked
class ManagedUserDAOTest {

    ManagedUserDAO dao;
    
    @Mock
    EntityManagerFactory entityManagerFactory;
    
    @Mock
    EntityManager entityManager;
    
    @Mock
    TypedQuery<User> query;
    
    @Mock
    EntityTransaction transaction;
    
    @Mock
    User user;
    
    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this);
        dao = new ManagedUserDAO();
        dao.setEntityManagerFactory(entityManagerFactory);
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.createQuery(anyString(), eq(User))).thenReturn(query);
    }

    @Test
    void addEntityTest() {
        dao.addEntity(user);
        verify(entityManager, times(1)).close();
    }
    
    @Test
    void adddeleteTest() {
        dao.deleteEntity(user);
        verify(entityManager, times(1)).close();
    }
    
    @Test
    void findEntitiesTest() {
        dao.findEntities();
        verify(entityManager, times(1)).close();
    }
    
    @Test
    void findEntitiesWithArgumentsTest() {
        dao.findEntities('userName', 'ted');
        verify(entityManager, times(1)).close();
    }
    
    @Test
    void getEntityTest() {
        dao.getEntity(0);
        verify(entityManager, times(1)).close();
    }
    
    @Test
    void updateEntityTest() {
        dao.updateEntity(user);
        verify(entityManager, times(1)).close();
    }

    @Test
    void detachEntityTest() {
        dao.detachEntity(user);
        verify(entityManager, times(1)).close();
    }
}
