package org.amc.game.chessserver.spring;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAOInterface;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserNameValidator implements Validator {

	private static final Logger logger = Logger.getLogger(UserNameValidator.class);
	public static final String USERNAME_FIELD = "userName";
	public static final String INVALID_USERNAME_ERROR = "Use lowercase letters and numbers (no spaces)";
	public static final String USERNAME_TAKEN_ERROR = "Username is already in use";
	public static final String DATABASE_ERROR = "Can't connect to the Database";
	
	private static final Pattern usernamePattern = Pattern.compile("[a-z][a-z0-9_]{4,50}");
	private DAOInterface<User> userDAO;
	
	public UserNameValidator(DAOInterface<User> userDAO) {
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
			errors.rejectValue(USERNAME_FIELD, message);
		}
	}
	
	private void checkUserNameValidFormat(String userName, Errors errors) {
		Matcher matcher = usernamePattern.matcher(userName);
		if(matcher.matches()) {
			checkUserNameFree(userName, errors);
		} else {
			errors.rejectValue(USERNAME_FIELD, INVALID_USERNAME_ERROR);
		}
	}
	
    private void checkUserNameFree(String userName, Errors errors) {

        if (isUserNameFree(userName, errors)) {

        } else {
            errors.rejectValue(USERNAME_FIELD, USERNAME_TAKEN_ERROR );
        }
    }
	
    boolean isUserNameFree(String userName, Errors errors) {
        try {
            return userDAO.findEntities("userName", userName).isEmpty();
        } catch (DAOException de) {
            errors.rejectValue(USERNAME_FIELD, DATABASE_ERROR);
            logger.error(de);
            return true;
        }
    }

}
