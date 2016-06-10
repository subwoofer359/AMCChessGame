package org.amc.game.chessserver.spring;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
/**
 * This class doesn't validate an email address to the RFC standard.
 * Just checks to see if the email address in the general form 
 * word/number/special characters@word/number/special characters.domain
 * 
 * @author Adrian McLaughlin
 *
 */
public class EmailValidator implements Validator {

	private static final Pattern emailAddrPattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,10}\\b");
	public static final String EMAIL_ADDR_FIELD = "emailAddress";
	public static final String INVALID_EMAIL_ERROR = "Not a valid email address"; 
	
	@Override
	public boolean supports(Class<?> arg0) {
		return String.class.equals(arg0);
	}

	@Override
	public void validate(Object arg0, Errors errors) {
	    if(arg0 == null) {
	        return;
	    }
	    
	    if(supports(arg0.getClass())) {
	        validateEmailAddress(arg0, errors);
	    } else {
	        setEmailAddressFieldInvalid(errors);
	    }
	}
	
	private void validateEmailAddress(Object arg0, Errors errors) {
	    String emailAddress = (String)arg0;
        
        if(isInvalidEmailladdress(emailAddress)) {
            setEmailAddressFieldInvalid(errors);
        }
	}
	
	private boolean isInvalidEmailladdress(String emailAddress) {
	    Matcher matcher = emailAddrPattern.matcher(emailAddress);
	    return !matcher.matches();
	}
	
	private void setEmailAddressFieldInvalid(Errors errors) {
	    errors.rejectValue(EMAIL_ADDR_FIELD, INVALID_EMAIL_ERROR);
	}

}
