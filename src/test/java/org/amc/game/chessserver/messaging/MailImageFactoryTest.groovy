package org.amc.game.chessserver.messaging;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import javax.servlet.ServletContext

class MailImageFactoryTest {
	
	MailImageFactory factory;
	
	@Mock
	ServletContext servletContext;
	
	static final String CONTENT_ID = "testId";
	
	static final String CONTENT_TYPE = "image/test";
	
	static final String PATH = "/tmp/img";

	@Before
	void setUp() {
		MockitoAnnotations.initMocks(this);
		factory = new MailImageFactory();
	}
	
	@Test
	void testGetServletPathImage() {
		factory.servletContext = servletContext;
		
		EmbeddedServletImage eImg = factory.getServletPathImage(CONTENT_ID, PATH, CONTENT_TYPE);
		checkGetPathImage(eImg);
		assertEquals(servletContext, eImg.servletContext);
		assertFalse("Delete-able should not be set", eImg.toBeDeleted);
	}
	
	private void checkGetPathImage(EmbeddedMailImage eImg) {
		assertEquals(CONTENT_ID, eImg.contentId);
		assertEquals(CONTENT_TYPE, eImg.contentType);
		assertEquals(PATH, eImg.path);
		
	}
	
	@Test
	void testGetTempServletPathImage() {
		factory.servletContext = servletContext;
		
		EmbeddedServletImage eImg = factory.getTempServletPathImage(CONTENT_ID, PATH, CONTENT_TYPE);
		checkGetPathImage(eImg);
		assertEquals(servletContext, eImg.servletContext);
		assertTrue("Delete-able should be set", eImg.toBeDeleted);
	}
	

	@Test
	void testFailOnNoServletContextSet() {
		try {
			factory.getServletPathImage("", "", "");
			fail("No null pointer exception thrown");
		} catch (NullPointerException npe) {
			String message = npe.getMessage();
			assertEquals(String.format(MailImageFactory.ERROR_NO_CONTEXT, 
				MailImageFactory.class.simpleName), message);
		}
	}
	
	@Test
	void testFailOnNoServletContextSet2() {
		try {
			factory.getTempServletPathImage("", "", "",);
			fail("No null pointer exception thrown");
		} catch (NullPointerException npe) {
			String message = npe.getMessage();
			assertEquals(String.format(MailImageFactory.ERROR_NO_CONTEXT,
				MailImageFactory.class.simpleName), message);
		}
	}
	
	@Test
	void testGetTempRootPathImage() {
		RootMailImage image = factory.getTempRootPathImage(CONTENT_ID, PATH, CONTENT_TYPE);
		checkGetPathImage(image);
		assertTrue("Delete-able should be set", image.toBeDeleted);
	}

	@Test
	void testGetRootPathImage() {
		RootMailImage image = factory.getRootPathImage(CONTENT_ID, PATH, CONTENT_TYPE);
		checkGetPathImage(image);
		assertFalse("Delete-able should not be set", image.toBeDeleted);
	}
}
