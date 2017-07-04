package org.amc.game.chessserver.spring;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class CsrfMatcherTest {

    private CsrfSecurityRequestMatcher matcher;
    private MockHttpServletRequest request;
    private String requestPath;
    private boolean passTest;
    
    public CsrfMatcherTest(String requestPath, boolean passTest) {
        this.requestPath = requestPath;
        this.passTest = passTest;
    }
    
    @Parameters
    public static Collection<?> addRequests() {

        return Arrays.asList(new Object[][] {
                { "/app/chessgame/info", true },
                { "/app/chessgame/chessgame/info", false },
                { "/signup", true },
                { "/Login.jsp", true },
                { "/index.jsp", true },
                { "/app/chessgame/chessgame", false }
        });
    }
    
    
    @Before
    public void setUp() throws Exception {
        matcher = new CsrfSecurityRequestMatcher();
        request = new MockHttpServletRequest();
        request.setPathInfo(requestPath);
    }

    @Test
    public void test() {
    	assertEquals(passTest, matcher.matches(request));
    }

}
