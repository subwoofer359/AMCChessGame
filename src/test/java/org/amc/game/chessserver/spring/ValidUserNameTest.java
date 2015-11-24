package org.amc.game.chessserver.spring;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import org.amc.User;
import org.amc.dao.DAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class ValidUserNameTest {

	private String userName;
	private Errors errors;
	private DAO<User> userDAO;
	private UserNameValidator userNameValidator;
	
	public ValidUserNameTest(String userName) {
		this.userName = userName;
	}
	
	@SuppressWarnings("unchecked")
    @Before
	public void setUp() throws Exception {
		this.userDAO = mock(DAO.class);
		this.userNameValidator = new UserNameValidator(userDAO);
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errors = new MapBindingResult(errorMap, "userName");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Parameters
    public static Collection<?> addedUserNames() {
        return Arrays.asList(new Object[][] { 
        		{ "adrian" }, { "a29394" }, { "adrianmclaughin" }, { "adrian_mclaughlin" }, 
        		{ "testingafullusernameoflengthofover50paddingpadding" }
        });
    }

	@Test
	public void test() {
		userNameValidator.validate(userName, errors);
		assertFalse(errors.hasErrors());
	}
}
