package org.amc.game.chessserver.messaging;

import static org.amc.game.chess.NoPlayer.NO_PLAYER;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.game.chessserver.AbstractServerChessGame
import org.amc.game.chess.Player
import org.amc.game.chessserver.ServerChessGame
import org.junit.*;
import org.mockito.*;
import org.thymeleaf.context.Context;

import javax.servlet.ServletContext

public class EmailTemplateTest {

	EmailTemplate email;

	String contentId = 'IMG';

	String contentType = 'image/jpeg';

	String path ='/tmp/test.jpg';
	
	def emImage;
	
	@Mock
	TemplateEngineAdapter templateEngine;

	@Mock
	ServletContext servletContext;

	@Mock
	Player player;

	@Mock
	AbstractServerChessGame aSCGame;

	MailImageFactory mailImageFactory;

	@Before
	void setup() {
		MockitoAnnotations.initMocks(this);

		email = new EmailTemplate() {
					public void addContextVariables() {}

					public void addImages() {}
				};

		mailImageFactory = new MailImageFactory();
		mailImageFactory.servletContext = servletContext;
		
		email.mailImageFactory = mailImageFactory;
		emImage = email.getMailImageFactory().getRootPathImage(
		contentId, path, contentType);
	}

	@Test
	void testDefaultEmail() {
		assertNotNull('Should contain an email address', email.getEmailSubject());
		assertNotEquals('Should not be empty string', '', email.getEmailSubject());
	}

	@Test
	void testDefaultConstructor() {
		assertEquals('Player should be NO PLAYER', NO_PLAYER, email.getPlayer());
		assertNull('Game has not been set', email.getServerChessGame());
		assertEquals('Template should be set to the empty string', '', email.getEmailTemplateName());
	}

	@Test
	void testConstructor() {

		email = new EmailTemplate(player, aSCGame) {
					public void addContextVariables() {}

					public void addImages() {}
				};

		assertEquals(player, email.getPlayer());

		assertEquals(aSCGame, email.getServerChessGame());
	}

	@Test
	void testURLRoot() {
		assertNotNull('Should be empty string or another value', email.getUrlRoot());

		final String URL_ROOT = '/tmp';

		email.setUrlRoot(URL_ROOT);

		assertEquals('Should be a value', URL_ROOT, email.getUrlRoot());
		

		email.setUrlRoot('ANOTHER/VALUE');

		assertNotEquals('Value can only be set once', 'ANOTHER/VALUE', email.getUrlRoot());
	}

	@Test
	void testAddContextVariable() {
		String name = 'Ted';
		Object testObject = 'Ralph';

		email.addContextVariable(name, testObject);

		Object result = email.getContextVariable(name);

		assertEquals('Should be the same object', testObject, result);

		assertNull('Should be no value', email.getContextVariable('Ralph'));
	}

	@Test
	void testAddEmbeddedImage() {
		email.mailImageFactory = mailImageFactory;

		email.addEmbeddedImage(emImage);

		Map m = email.getEmbeddedImages();

		def image = m[contentId];

		assertEquals('Map size should be 1', 1, m.size());

		assertEquals(contentId, image.contentId);

		assertEquals(path, image.path);

		assertFalse('Not to be deleted', image.isToBeDeleted());
	}
	
	@Test
	void testMailImageFactoryNull() {
		email.mailImageFactory = null;
		try {
			email.getMailImageFactory();
			fail('Should have thrown a null pointer exception');
		} catch (NullPointerException npe) {
			
		}
	}
	
	@Test
	void testGetEmailHtml() {
		email.mailImageFactory = mailImageFactory;
		email.templateEngine = templateEngine;
		email.addContextVariable('Test', 'Test');
		
		def result = '<HTML>test</HTML>';
		
		when(templateEngine.process(eq(''), any(Context))).thenReturn(result);
		
		ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context);
		
		
		assertEquals(result, email.emailHtml);
		
		verify(templateEngine, times(1)).process(eq(''), contextCaptor.capture());
		
		Context ctx = contextCaptor.getValue();
		assertEquals('Should only be one variable stored', 1, ctx.getVariables().size());
	}
}
