package org.amc.game.chessserver.messaging;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.TwoViewServerChessGame;

import org.junit.Before;
import org.junit.Test;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

public class PlayerJoinedChessGameEmailTest {

    private PlayerJoinedChessGameEmail template;
    private Player player;
    private ServerChessGame scg;
    private final long GAME_UID = 20202l;
    
    @Before
    public void setUp() throws Exception {
        player = new HumanPlayer("Adrian McLaughlin");
        scg = new TwoViewServerChessGame(GAME_UID, player);
        scg.setChessGameFactory(new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        });
        scg.addOpponent(new HumanPlayer("Player 2"));
        template = new PlayerJoinedChessGameEmail(player, scg);
        
        FileTemplateResolver emailTemplateResolver = new FileTemplateResolver();
        emailTemplateResolver.setPrefix("src/main/resources/mail/");
        emailTemplateResolver.setTemplateMode("HTML5");
        emailTemplateResolver.setCharacterEncoding("UTF-8");
        
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(emailTemplateResolver);
        
        emailTemplateResolver.initialize();
        templateEngine.initialize();
     
        template.setTemplateEngine(templateEngine);
    }

    @Test
    public void test() throws Exception {
        assertEquals(template.getEmailSubject(), PlayerJoinedChessGameEmail.DEFAULT_EMAIL_SUBJECT);
        assertTrue(ComparePlayers.isSamePlayer(template.getPlayer(),player));
        assertEquals(template.getServerChessGame(), scg);
        template.getEmailHtml();  
        
    }
}
