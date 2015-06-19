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

public class PlayerJoinedChessGameEmailTest {

    private PlayerJoinedChessGameEmail template;
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
        template = new PlayerJoinedChessGameEmail(player, scg);
        
        emailTemplateResolver = new FileTemplateResolver();
        emailTemplateResolver.setPrefix("src/main/resources/mail/");
        emailTemplateResolver.setTemplateMode("HTML5");
        emailTemplateResolver.setCharacterEncoding("UTF-8");
        
        templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(emailTemplateResolver);
        
        emailTemplateResolver.initialize();
        templateEngine.initialize();
     
        template.setTemplateEngine(templateEngine);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        String html = template.getEmailHtml();  
        
    }
}
