package org.amc.game.chessserver.messaging;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.messaging.EmailTemplateFactory.FactoryInstantinationException;
import org.junit.Before;
import org.junit.Test;
import org.thymeleaf.spring4.SpringTemplateEngine;

public class EmailTemplateFactoryTest {

    private EmailTemplateFactory factory;
    private SpringTemplateEngine stEngine;
    private ChessBoardSVGFactory cbsImageFactory;
    
    @Before
    public void setUp() throws Exception {
        cbsImageFactory = new ChessBoardSVGFactory();
        factory = new EmailTemplateFactory(){
            @Override
            public ChessBoardSVGFactory getChessBoardSVGFactory() {
                return cbsImageFactory;
            }
        };
        
        stEngine = mock(SpringTemplateEngine.class);
        factory.setTemplateEngine(stEngine);
    }

    @Test
    public void testPlayerJoinedChessGameEmailCreation() {
        EmailTemplate email = factory.getEmailTemplate(Player.class);
        assertTrue(email instanceof PlayerJoinedChessGameEmail);
        assertFalse(email instanceof MoveUpdateEmail);
        assertEquals(email.getTemplateEngine(), stEngine);
        
    }
    
    
    @Test
    public void testMoveUpdateEmailCreation() {
        EmailTemplate email = factory.getEmailTemplate(ChessGame.class);
        assertTrue(email instanceof MoveUpdateEmail);
        assertFalse(email instanceof PlayerJoinedChessGameEmail);
        assertEquals(email.getTemplateEngine(), stEngine);
        assertEquals(((MoveUpdateEmail)email).getChessBoardSVGFactory(), cbsImageFactory);
        
    }
    
    @Test(expected= FactoryInstantinationException.class)
    public void testFactoryInstantinationException() {
        factory.getEmailTemplate(Object.class);
    }

}
