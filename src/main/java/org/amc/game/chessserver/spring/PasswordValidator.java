package org.amc.game.chessserver.spring;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PasswordValidator implements Validator {

	public static final String PASSWORD_FIELD = "password";
	public static final String INVALID_PASSWORD_ERROR = "invalid password format";
    public static final String NO_PASSWORD_ERROR = "password wasn't given";
	
	private static final Pattern passwordPattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})");
	
	@Override
	public boolean supports(Class<?> arg0) {
		if(arg0.isAssignableFrom(String.class)) {
			return true;
		}
		return false;
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		String password = (String) arg0;
		
		rejectIfEmptyOrWhitespace(errors, password, NO_PASSWORD_ERROR);
		
		checkPasswordValidFormat(password, errors);
	}
	
	private void rejectIfEmptyOrWhitespace(Errors errors, String password, String message) {
		if("".equals(password)) {
			errors.rejectValue(PASSWORD_FIELD, message);
		}
	}
	
	private void checkPasswordValidFormat(String password, Errors errors) {
		Matcher matcher = passwordPattern.matcher(password);
		if(matcher.matches()) {
		
		} else {
			errors.rejectValue(PASSWORD_FIELD, INVALID_PASSWORD_ERROR);
		}
	}
}
