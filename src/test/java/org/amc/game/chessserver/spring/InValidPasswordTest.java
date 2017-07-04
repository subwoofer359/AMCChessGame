package org.amc.game.chessserver.spring;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class InValidPasswordTest {
    
    private String password;
    private PasswordValidator validator = new PasswordValidator();
    private Errors errors;

    public InValidPasswordTest(String password) {
        this.password = password;
    }
    
    @Parameters
    public static Collection<?> addedFullNames() {
        return Arrays.asList(new Object[][] { { "Adri1" },
                        { "carla$ae#stephenson2" },
                        { "ABCD.212" }
        });
    }
    
    @Before
    public void setUp() throws Exception {
        Map<String, Object> errorMap = new HashMap<String, Object>();
        errors = new MapBindingResult(errorMap, "fullName");
    }

    @Test
    public void test() {
        validator.validate(password, errors);
        assertTrue(errors.hasErrors());
    }
}
