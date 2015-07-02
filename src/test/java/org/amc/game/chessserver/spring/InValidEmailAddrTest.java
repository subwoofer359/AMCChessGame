package org.amc.game.chessserver.spring;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(Parameterized.class)
public class InValidEmailAddrTest {

	private String emailAddr;
	private EmailValidator validator = new EmailValidator();
	private Errors errors;

	public InValidEmailAddrTest(String emailAddr) {
		this.emailAddr = emailAddr;
	}

	@Before
	public void setUp() throws Exception {
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errors = new MapBindingResult(errorMap, "userName");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Parameters
	public static Collection<?> addedUserNames() {
		return Arrays.asList(new Object[][] {
				{ "#@%^%#$@#$@#.com" },
				{ "@example.com" },
				{ "Joe Smith <email@example.com>" },
				{ "email.example.com" },
				{ "email@example@example.com" },
				{ ".email@example.com" },
				{ "email@example.com (Joe Smith)" }
		});
	}

	@Test
	public void test() {
		validator.validate(emailAddr, errors);
		assertTrue(errors.hasErrors());
	}

}
