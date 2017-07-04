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
public class InValidFullNameTest {
    
    private String fullName;
    private FullNameValidator validator = new FullNameValidator();
    private Errors errors;

    public InValidFullNameTest(String fullName) {
        this.fullName = fullName;
    }
    
    @Parameters
    public static Collection<?> addedFullNames() {
        return Arrays.asList(new Object[][] { { "Adrian-McLaughlin" },
                        { "carla_rae_stephenson" }, { "Paula#S. Carlson" },
                        { "Adrian McLaughlinMcLaughlinMcLaughlinMcLaughlin BSC" },
                        { "<input type='text'/>" }
        });
    }
    
    @Before
    public void setUp() throws Exception {
        Map<String, Object> errorMap = new HashMap<String, Object>();
        errors = new MapBindingResult(errorMap, "fullName");
    }

    @Test
    public void test() {
        validator.validate(fullName, errors);
        assertTrue(errors.hasErrors());
    }
}
