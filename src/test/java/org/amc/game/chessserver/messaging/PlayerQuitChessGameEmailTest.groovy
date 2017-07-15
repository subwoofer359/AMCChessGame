package org.amc.game.chessserver.messaging

import static org.junit.Assert.*;

import java.io.File;

import javax.servlet.ServletContext

import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.web.context.support.ServletContextAttributeExporter



class PlayerQuitChessGameEmailTest {
	
	final static Long UID = 23330L;
	
	PlayerQuitChessGameEmail emailTemplate;
	
	Player player = [name: "adrian", userName: "adrian"] as HumanPlayer;
	
	Player chessPlayer = new RealChessGamePlayer(player, Colour.WHITE);
	
	AbstractServerChessGame scg = [
		getPlayer: {
			return chessPlayer;
		},
		getUid: {
			return UID;
		}
		
		
	] as AbstractServerChessGame;
	
	
	@Mock
	ServletContext servletContext;
	
	MailImageFactory mailImageFactory;
	
	@Before
	void setup() {
		MockitoAnnotations.initMocks(this);
		mailImageFactory = new MailImageFactory();
		mailImageFactory.servletContext = servletContext;
		
		emailTemplate = new PlayerQuitChessGameEmail(player, scg);
		emailTemplate.mailImageFactory = mailImageFactory;
	}

	@Test
	public void testConstructor() {
		emailTemplate = new PlayerQuitChessGameEmail();
		assert emailTemplate.emailTemplateName == PlayerQuitChessGameEmail.EMAIL_TEMPLATE;
		assert emailTemplate.emailSubject == PlayerChessGameEmail.DEFAULT_EMAIL_SUBJECT;
	}
	
	@Test
	public void testConstructorWithParameters() {
		PlayerQuitChessGameEmail email = new PlayerQuitChessGameEmail(player, scg);
		
		assert email.emailTemplateName == PlayerQuitChessGameEmail.EMAIL_TEMPLATE;
		assert email.emailSubject == PlayerChessGameEmail.DEFAULT_EMAIL_SUBJECT;
		assert email.player == player;
		assert email.serverChessGame == scg;
	}
	
	@Test
	public void testAddContextVariables() {
		emailTemplate.addContextVariables();
		
		assert emailTemplate.getContextVariable('name') == scg.player.name;
		assert emailTemplate.getContextVariable('player') == player.name;
		assert emailTemplate.getContextVariable('GAME_UUID') == String.valueOf(scg.uid);
		assertNotNull(emailTemplate.getContextVariable(EmailTemplate.TEMPLATE_BACKGROUND_TAG)); 
		
	}
	
	@Test
	public void testAddImages() {
		emailTemplate.addImages();
		
		assertTrue(emailTemplate.getEmbeddedImages().containsKey(EmailTemplate.BACKGROUND_IMAGE_RESOURCE));
		
	}

}
