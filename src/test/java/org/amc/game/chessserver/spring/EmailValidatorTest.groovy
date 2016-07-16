package org.amc.game.chessserver.spring;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.Errors;

class EmailValidatorTest {
    
    EmailValidator validator;
    
    @Mock
    Errors errors;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        validator = new EmailValidator();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSupports() {
        assertTrue('Should support String class', validator.supports(String.class));
    }
    
    @Test
    public void testDoesNotSupports() {
        assertFalse('Shouldn\'t support Object class',validator.supports(Object.class));
    }
    
    @Test
    public void testValidate() {
        Object obj = new PasswordValidator();
        validator.validate(obj, errors);
    }

}
