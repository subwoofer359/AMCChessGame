package org.amc.game.chessserver.spring;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FullNameValidator implements Validator {

    private static final Pattern fullNameAddrPattern = Pattern.compile("\\b[a-zA-Z .']{1,50}\\b");
    public static final String FULL_NAME_FIELD = "fullName";
    public static final String INVALID_FULLNAME_ERROR = "Not valid Name format";
    public static final String NO_FULLNAME_ERROR = "No name given";
    
    @Override
    public boolean supports(Class<?> arg0) {
        // TODO Auto-generated method stub
        return String.class.equals(arg0);
    }

    @Override
    public void validate(Object arg0, Errors errors) {
        String fullName = (String) arg0;
        
        rejectIfEmptyOrWhitespace(errors, fullName, NO_FULLNAME_ERROR);
        
        Matcher matcher = fullNameAddrPattern.matcher(fullName);
        if(matcher.matches()) {
            
        } else {
            errors.rejectValue(FULL_NAME_FIELD, INVALID_FULLNAME_ERROR);
        }
        

    }
    
    private void rejectIfEmptyOrWhitespace(Errors errors, String userName, String message) {
        if("".equals(userName)) {
            errors.rejectValue(FULL_NAME_FIELD, message);
        }
    }

}
