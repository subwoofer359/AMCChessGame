package org.amc.game.chessserver.spring;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Errors;


public class ValdatorIsUserNameFreeTest {

	private DAO<User> userDAO;
	private UserNameValidator userNameValidator;
	private Errors errors;
	private static final String USER_NAME = "adrian";
	private static final String TAKEN_USER_NAME = "sarah";
	
	@SuppressWarnings("unchecked")
    @Before
	public void setUp() throws Exception {
		this.userDAO = mock(DAO.class);
		this.userNameValidator = new UserNameValidator(userDAO);
		this.errors = mock(Errors.class);
	}
	
	@Test
    public void testIsUserNameFree() throws DAOException {
        String freeUserName = USER_NAME;
        when(userDAO.findEntities("userName", freeUserName)).thenReturn(new ArrayList<User>());
        assertTrue(this.userNameValidator.isUserNameFree(freeUserName, errors));
        verify(userDAO).findEntities("userName", freeUserName);
    }
	
	@Test
    public void testIsUserNameTaken() throws DAOException {
        String takenUserName = TAKEN_USER_NAME;
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(userDAO.findEntities("userName", takenUserName)).thenReturn(users);
        assertFalse(userNameValidator.isUserNameFree(takenUserName, errors));
        verify(userDAO).findEntities("userName", takenUserName);
    }
}
