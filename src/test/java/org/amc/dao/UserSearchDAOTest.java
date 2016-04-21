package org.amc.dao;

import static org.junit.Assert.*;

import org.amc.game.chessserver.DatabaseSignUpFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class UserSearchDAOTest {
    
    private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
    private UserSearchDAO dao;
    private static int NO_OF_USERS = 3;
    
    @Before
    public void setUp() throws Exception {
        signUpfixture.setUp();
        dao = new UserSearchDAO();
    }
    
    @After
    public void tearDown() throws Exception {
        signUpfixture.tearDown();
    }
    
    @Test
    public void test() throws Exception {
      List<UserDetails> l = dao.findUsers("%");
      
      for(UserDetails o:l){
          System.out.println(o.getFullName());
      }
      
      assertFalse(l.isEmpty());
      assertEquals(NO_OF_USERS, l.size());
    }
}
