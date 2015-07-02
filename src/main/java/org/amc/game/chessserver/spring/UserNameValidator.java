package org.amc.game.chessserver.spring;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAO;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserNameValidator implements Validator {

	private static final Logger logger = Logger.getLogger(UserNameValidator.class);
	public static final String USERNAME_FIELD = "userName";
	private static Pattern usernamePattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]{4,50}");
	
	private DAO<User> userDAO;
	
	public UserNameValidator(DAO<User> userDAO) {
		this.userDAO = userDAO;
	}
	
	@Override
	public boolean supports(Class<?> arg0) {
		if(arg0.isAssignableFrom(String.class)) {
			return true;
		}
		return false;
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		String userName = (String) arg0;
		
		rejectIfEmptyOrWhitespace(errors, userName, "Username not given");
		
		checkUserNameValidFormat(userName, errors);
	}
	
	private void rejectIfEmptyOrWhitespace(Errors errors, String userName, String message) {
		if("".equals(userName)) {
			errors.rejectValue(USERNAME_FIELD, "Username is empty");
		}
	}
	
	private void checkUserNameValidFormat(String userName, Errors errors) {
		Matcher matcher = usernamePattern.matcher(userName);
		if(matcher.matches()) {
			checkUserNameFree(userName, errors);
		} else {
			errors.rejectValue(USERNAME_FIELD, "invalid username format");
		}
	}
	
	private void checkUserNameFree(String userName, Errors errors) {
		if(isUserNameFree(userName)) {
			
		} else {
			errors.rejectValue(USERNAME_FIELD, "username is already in use");
		}
	}
	
	boolean isUserNameFree(String userName) {
		try {
			return userDAO.findEntities("userName", userName).isEmpty();
		} catch (DAOException de) {
			logger.error("Error on accessing database:" + de.getMessage());
            de.printStackTrace();
			return false;
		}
    }

}
