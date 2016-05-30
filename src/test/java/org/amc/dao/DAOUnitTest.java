package org.amc.dao;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.DAOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TypedQuery;

public class DAOUnitTest {
    
    private DAO<TestEntity> dao;
    
    @Mock
    private EntityManager em;
    
    @Mock
    private EntityTransaction transaction;
    
    private static final TestEntity entity = new TestEntity();
    
    @Mock
    private EntityManagerFactory emFactory;
    
    @Mock
    private TypedQuery<TestEntity> query;
    
    private ArgumentCaptor<String> queryString;
    private static final String COLUMN = "name";
    private static final String VALUE = "Ted";
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        queryString = ArgumentCaptor.forClass(String.class);
        
        when(emFactory.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(transaction);
        when(em.createQuery(queryString.capture(), eq(TestEntity.class))).thenReturn(query);
 
        dao = new DAO<DAOUnitTest.TestEntity>(TestEntity.class) {
            @Override
            public EntityManager getEntityManager() {
                return em;
            };
        };
    }

    /**
     * The factory in EntityManagerThreadLocal has to be restored to it's concrete value 
     * so not to interfere with other tests
     */
    @After
    public void tearDown() throws Exception {
        reset(transaction);
    }

    @Test
    
    public void persistTest() throws DAOException {
        
        dao.addEntity(entity);
        verify(transaction, times(1)).begin();
        verify(em, times(1)).persist(eq(entity));
        verify(transaction, times(1)).commit();
    }
    
    @Test(expected=OptimisticLockException.class)
    
    public void persistTestThrowOptimisticLockException() throws DAOException {
        doThrow(new OptimisticLockException()).when(transaction).commit();
        dao.addEntity(entity);
    }
    
    @Test(expected=DAOException.class)
    
    public void persistTestThrowDAOException() throws DAOException {
        doThrow(new PersistenceException()).when(em).persist(any());
        dao.addEntity(entity);
    }
    
    @Test
    
    public void updateTest() throws DAOException {
        dao.updateEntity(entity);
        verify(transaction, times(1)).begin();
        verify(em, times(1)).merge(eq(entity));
        verify(transaction, times(1)).commit();
    }
    
    @Test(expected=OptimisticLockException.class)
    
    public void updateTestThrowOptimisticLockException() throws DAOException {
        doThrow(new OptimisticLockException()).when(transaction).commit();
        dao.updateEntity(entity);
    }
    
    @Test(expected=DAOException.class)
    
    public void updateTestThrowDAOException() throws DAOException {
        doThrow(new PersistenceException()).when(em).merge(any());
        dao.updateEntity(entity);
    }
    
    @Test
    public void deleteTest() throws DAOException {
        when(em.merge(eq(entity))).thenReturn(entity);
        dao.deleteEntity(entity);
        verify(transaction, times(1)).begin();
        verify(em, times(1)).merge(eq(entity));
        verify(em, times(1)).remove(eq(entity));
        verify(transaction, times(1)).commit();
    }
    
    @Test(expected=OptimisticLockException.class)
    public void deleteThrowOptimisticLockException() throws DAOException {
        doThrow(new OptimisticLockException()).when(transaction).commit();
        dao.deleteEntity(entity);
    }
    
    @Test(expected=DAOException.class)
    public void deleteTestThrowDAOException() throws DAOException {
        doThrow(new PersistenceException()).when(em).remove(any());
        dao.deleteEntity(entity);
    }
    
    @Test
    public void detachTest() throws DAOException {
        dao.detachEntity(entity);
        verify(em, times(1)).detach(eq(entity));
    }
    
    @Test
    public void detachTestThrowsIllegalArgumentException() {
        doThrow(new IllegalArgumentException()).when(em).detach(any());
        dao.detachEntity(entity);
    }
    
    @Test
    public void findEntities() throws DAOException {
        when(query.getResultList()).thenReturn(Collections.<TestEntity>emptyList());
        List<TestEntity> entities = dao.findEntities();
        assertNotNull(entities);
        verify(query, times(1)).getResultList();
        String queryStr  = "Select x from TestEntity x";
        assertEquals(queryStr, queryString.getValue());
    }
    
    @Test(expected=DAOException.class)
    public void findEntitiesThrowsDAOException() throws DAOException {
        when(query.getResultList()).thenThrow(new PersistenceException());
        
        dao.findEntities();
    }
    
    @Test
    public void findEntitiesThrowsRunTimeException() {
        when(query.getResultList()).thenThrow(new QueryTimeoutException());
        List<TestEntity> result = null;
        try {
            result = dao.findEntities();
        } catch(DAOException pe) {
            System.out.println(pe.getMessage());
        } finally {
            assertNull(result);
        }
    }
    
    @Test  
    public void findEntitiesString() throws DAOException {
        when(query.getResultList()).thenReturn(Collections.<TestEntity>emptyList());
        final String queryStr  = "Select x from TestEntity x where x." + COLUMN +" = ?1";
        ArgumentCaptor<String> valueArgument = ArgumentCaptor.forClass(String.class);
        
        List<TestEntity> entities = dao.findEntities(COLUMN, VALUE);
        
        assertNotNull(entities);
        verify(query, times(1)).getResultList();
        assertEquals(queryStr, queryString.getValue());
        verify(query, times(1)).setParameter(eq(1), valueArgument.capture());
        assertEquals(VALUE, valueArgument.getValue());
    }
    
    @Test(expected=DAOException.class)
    public void findEntitiesStringThrowsDAOException() throws DAOException {
        when(query.getResultList()).thenThrow(new PersistenceException());
        
        dao.findEntities(COLUMN, VALUE);
    }
    
    @Test
    public void findEntitiesStringThrowsRunTimeException() {
        when(query.getResultList()).thenThrow(new QueryTimeoutException());
        List<TestEntity> result = null;
        try {
            result = dao.findEntities(COLUMN, VALUE);
        } catch(DAOException pe) {
            System.out.println(pe.getMessage());
        } finally {
            assertNull(result);
        }
    }
    
    @Test
    public void getEntityTest() throws DAOException {
        String queryStr = "Select x from TestEntity x where x.id = ?1";
        final int ID = 4;
        ArgumentCaptor<String> valueArgument = ArgumentCaptor.forClass(String.class);
        when(query.getSingleResult()).thenReturn(new TestEntity());
        
        TestEntity result = dao.getEntity(ID);
        
        assertNotNull(result);
        assertEquals(queryStr, queryString.getValue());
        verify(query, times(1)).setParameter(eq(1), valueArgument.capture());
        assertEquals(ID , valueArgument.getValue());
        verify(query, times(1)).getSingleResult();
        
    }
    
    @Test(expected=DAOException.class)
    public void getEntityThrowsDAOException() throws DAOException {
        when(query.getSingleResult()).thenThrow(new PersistenceException());
        
        dao.getEntity(4);
    }
    
    @Test
    public void getEntityThrowsRunTimeException() {
        when(query.getSingleResult()).thenThrow(new QueryTimeoutException());
        TestEntity result = null;
        try {
            result = dao.getEntity(4);
        } catch(DAOException pe) {
            System.out.println(pe.getMessage());
        } finally {
            assertNull(result);
        }
    }
    
    @Test 
    public void getEntityThrowsNoResultException() {
        when(query.getSingleResult()).thenThrow(new NoResultException());
        TestEntity result = null;
        try {
            result = dao.getEntity(4);
        } catch(DAOException pe) {
            System.out.println(pe.getMessage());
        } finally {
            assertNull(result);
        }
    }
    
    @Test
    public void getEntityClassTest() {
        assertEquals(TestEntity.class, dao.getEntityClass());
    }
    
    @Test
    public void toStringTest() {
        String toStringString ="DAO<TestEntity>";
        assertEquals(toStringString, dao.toString());
    }
    
    private static class TestEntity {
        
    }
}
