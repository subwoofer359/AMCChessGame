package org.amc.game.chessserver.spring;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(Parameterized.class)
public class ValidEmailAddrTest {

	private String emailAddr;
	private EmailValidator validator = new EmailValidator();
	private Errors errors;

	public ValidEmailAddrTest(String emailAddr) {
		this.emailAddr = emailAddr;
	}

	@Before
	public void setUp() throws Exception {
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errors = new MapBindingResult(errorMap, "userName");
	}

	@Parameters
	public static Collection<?> addedUserNames() {
		return Arrays.asList(new Object[][] { { "test@testdomain.ie" },
				{ "test@testdomain.museum" }, { "email@example.com" },
				{ "firstname.lastname@example.com" },
				{ "email@subdomain.example.com" },
				{ "firstname+lastname@example.com" },
				{ "1234567890@example.com" }, { "email@example-one.com" },
				{ "_______@example.com" }, { "email@example.name" },
				{ "email@example.museum" }, { "email@example.co.jp" },
				{ "firstname-lastname@example.com" } });
	}

	@Test
	public void test() {
		validator.validate(emailAddr, errors);
		assertFalse(errors.hasErrors());
	}

}
