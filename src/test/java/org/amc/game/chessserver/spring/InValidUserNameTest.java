package org.amc.game.chessserver.spring;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.amc.User;
import org.amc.dao.DAO;
import org.amc.dao.DAOInterface;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(Parameterized.class)
public class InValidUserNameTest {

	private String userName;
	private Errors errors;
	private DAOInterface<User> userDAO;
	
	
	public InValidUserNameTest(String userName) {
		this.userName = userName;
	}
	
	@SuppressWarnings("unchecked")
    @Before
	public void setUp() throws Exception {
		this.userDAO = mock(DAO.class);
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errors = new MapBindingResult(errorMap, "userName");
	}
	
	@Parameters
    public static Collection<?> addedUserNames() {
        return Arrays.asList(new Object[][] { 
        		{ "Tom" }, { "_a29394" }, { "adrian-McLaughin" }, { "\"adrian_mclaughlin\"" },
        		{ "adrian mclaughlin" }, { "for(i=0;i<length;i++)\nconsole.log(i)" },
        		{ "_a29394" }, { "TestingAFullUserNameOfLengthOfOver50PaddingPaddingPadding" }, 
        		{"Breda234"}, {"1adrianmclaughlin"}
        });
    }

	@Test
	public void test() {
		UserNameValidator userNameValidator = new UserNameValidator(userDAO);
		userNameValidator.validate(userName, errors);
		assertTrue(errors.hasErrors());
	}

}
