package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

public class CacheFilterTest {
	
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterConfig fConfig;
    private MockFilterChain chain;
    private static final String CACHE_HEADER = "Cache-Control";
    private static final String CACHE_HEADER_VALUE = "max-age=86400";
    
	@Before
	public void setUp() throws Exception {
    	request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        MockServletContext context = new MockServletContext();
        fConfig = new MockFilterConfig(context);
        chain = new MockFilterChain();
	}

	@Test
	public void test() throws Exception {
		CacheFilter filter = new CacheFilter();
		filter.init(fConfig);
		
		filter.doFilter(request, response, chain);
		
		assertEquals(CACHE_HEADER_VALUE, response.getHeader(CACHE_HEADER));
		
		filter.destroy();		
	}

}
