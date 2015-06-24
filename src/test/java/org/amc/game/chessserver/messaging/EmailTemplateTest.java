package org.amc.game.chessserver.messaging;

import static org.junit.Assert.*;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.util.Map;

public class EmailTemplateTest {

    private MoveUpdateEmail template;
    private SpringTemplateEngine templateEngine;
    private Player player;
    private ServerChessGame scg;
    private final long GAME_UID = 20202l;
    private FileTemplateResolver emailTemplateResolver;
    
    @Before
    public void setUp() throws Exception {
        player = new HumanPlayer("Adrian McLaughlin");
        scg = new ServerChessGame(GAME_UID, player);
        scg.addOpponent(new HumanPlayer("Player 2"));
        template = new MoveUpdateEmail(player, scg);
        
        ChessBoardSVGFactory cbsi = new ChessBoardSVGFactory(scg);
        
        emailTemplateResolver = new FileTemplateResolver();
        emailTemplateResolver.setPrefix("src/main/resources/mail/");
        emailTemplateResolver.setTemplateMode("HTML5");
        emailTemplateResolver.setCharacterEncoding("UTF-8");
        
        templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(emailTemplateResolver);
        
        emailTemplateResolver.initialize();
        templateEngine.initialize();
     
        template.setTemplateEngine(templateEngine);
        template.setChessBoardSVGImage(cbsi);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        template.getEmailHtml();  
        Map<String, EmbeddedMailImage> images = template.getEmbeddedImages();
        assertTrue(images.size() == 2);
        
        EmbeddedMailImage imageBackground = images.get(MoveUpdateEmail.BACKGROUND_IMAGE_RESOURCE);
        assertEquals(imageBackground.getContentId(), MoveUpdateEmail.BACKGROUND_IMAGE_RESOURCE);
        assertEquals(imageBackground.getContentType(), "image/jpg");
        assertEquals(imageBackground.getImageSource().getPath(), template.backgroundImagePath);
        
        EmbeddedMailImage imageChessboard = images.get(MoveUpdateEmail.CHESSBOARD_IMAGE_RESOURCE);
        assertEquals(imageChessboard.getContentId(), MoveUpdateEmail.CHESSBOARD_IMAGE_RESOURCE);
        assertEquals(imageChessboard.getContentType(), "image/jpg");
        assertNotNull(imageChessboard.getImageSource().getPath());
    }
}
