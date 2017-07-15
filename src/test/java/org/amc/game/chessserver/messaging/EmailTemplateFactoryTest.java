package org.amc.game.chessserver.messaging;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Player;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.messaging.EmailTemplateFactory.FactoryInstantinationException;
import org.amc.util.SpringHostInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.servlet.ServletContext;

public class EmailTemplateFactoryTest {
	
	@Mock
	private MailImageFactory mailImageFactory;
	
	@Mock
	private ServletContext servletContext;
	
    private EmailTemplateFactory factory;
    private SpringTemplateEngine stEngine;
    private ChessBoardSVGFactory cbsImageFactory;
    
    private static final String CONTEXT_PATH = "/AMCChessGame";
    
    @Before
    public void setUp() throws Exception {
    	MockitoAnnotations.initMocks(this);
    	
        cbsImageFactory = new ChessBoardSVGFactory();
        factory = new EmailTemplateFactory(){
            @Override
            public ChessBoardSVGFactory getChessBoardSVGFactory() {
                return cbsImageFactory;
            }
        };
        
        stEngine = mock(SpringTemplateEngine.class);
        factory.setTemplateEngine(stEngine);
        
        when(servletContext.getContextPath()).thenReturn(CONTEXT_PATH);
    }

    @Test
    public void testPlayerJoinedChessGameEmailCreation() {
    	factory.setMailImageFactory(mailImageFactory);
    	EmailTemplate email = factory.getEmailTemplate(Player.class);
        
        assertTrue(email instanceof PlayerJoinedChessGameEmail);
        assertFalse(email instanceof MoveUpdateEmail);
        assertNotNull("Should not be null", email.getMailImageFactory());
        assertNotNull("Should not be null", email.getTemplateEngine());
        
    }
    
    
    @Test
    public void testMoveUpdateEmailCreation() {
    	factory.setMailImageFactory(mailImageFactory);
        EmailTemplate email = factory.getEmailTemplate(ChessGame.class);
        
        assertTrue(email instanceof MoveUpdateEmail);
        assertFalse(email instanceof PlayerJoinedChessGameEmail);
        assertNotNull("Should not be null", email.getTemplateEngine());
        assertNotNull("Should not be null", email.getMailImageFactory());
        assertEquals(((MoveUpdateEmail)email).getChessBoardSVGFactory(), cbsImageFactory);
        
    }
    
    @Test(expected= FactoryInstantinationException.class)
    public void testFactoryInstantinationException() {
        factory.getEmailTemplate(Object.class);
    }
    
    @Test
    public void testGetEmailTemplateStatusFinished() {
    	factory.setMailImageFactory(mailImageFactory);
    	
    	EmailTemplate email = factory.getEmailTemplate(Player.class, ServerGameStatus.FINISHED);
    	
    	assertTrue("Should instance of email PlayerQuitChessGameEmail",
    			email instanceof PlayerQuitChessGameEmail);
    	assertNotNull("Should not be null", email.getTemplateEngine());
    	assertNotNull("Should not be null", email.getMailImageFactory());
    }
    
    @Test
    public void testGetEmailTemplateStatusOther() {
    	try {
    		factory.getEmailTemplate(Player.class, ServerGameStatus.AWAITING_PLAYER);
    		failOnNoFactoryInstantinationException();
    	} catch (FactoryInstantinationException fe) {
    		return;
    	}
    	failOnNoFactoryInstantinationException();
    }
    
    private void failOnNoFactoryInstantinationException() {
    	fail("Should have thrown FactoryInstantinationException");
    }
    
    @Test
    public void testGetEmailTemplateNotPlayerClass() {
    	try {
    		factory.getEmailTemplate(RealChessGamePlayer.class, ServerGameStatus.FINISHED);
    		failOnNoFactoryInstantinationException();
    	} catch (FactoryInstantinationException fe) {
    		return;
    	}
    	failOnNoFactoryInstantinationException();
    }
    
    @Test
    public void testSetSpringHostInfo() {
    	
    	SpringHostInfo info = new SpringHostInfo(servletContext, "192.168.1.1", 8080);
    	factory.setSpringHostInfo(info);
    	
    	assertEquals(EmailTemplate.getUrlRoot(), 
    			info.getHostUrl() + EmailTemplateFactory.URL_APP_PATH);

    }

}
