package org.amc.game.chessserver.spring;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FullNameValidator implements Validator {

    private static final Pattern fullNameAddrPattern = Pattern.compile("\\b[a-zA-Z .']{1,50}\\b");
    public static final String FULL_NAME_FIELD = "fullName";
    public static final String INVALID_FULLNAME_ERROR = "Use letters, spaces and dots only (max:50)";
    public static final String NO_FULLNAME_ERROR = "No name given";
    private final String fieldName;
    
    public FullNameValidator() {
        this.fieldName = FULL_NAME_FIELD;
    }
    
    public FullNameValidator(String fieldName) {
        this.fieldName = fieldName;
    }
    
    @Override
    public boolean supports(Class<?> arg0) {
        // TODO Auto-generated method stub
        return String.class.equals(arg0);
    }

    @Override
    public void validate(Object arg0, Errors errors) {
        if(arg0 == null) {
            errors.rejectValue(fieldName, INVALID_FULLNAME_ERROR);
        } else {
            String fullName = (String) arg0;
        
            rejectIfEmptyOrWhitespace(errors, fullName, NO_FULLNAME_ERROR);
        
            Matcher matcher = fullNameAddrPattern.matcher(fullName);
            if(matcher.matches()) {
                
            } else {
                errors.rejectValue(fieldName, INVALID_FULLNAME_ERROR);
            }
        }
    }
    
    private void rejectIfEmptyOrWhitespace(Errors errors, String userName, String message) {
        if("".equals(userName)) {
            errors.rejectValue(fieldName, message);
        }
    }

}
