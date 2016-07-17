package org.amc.game.chessserver.messaging;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.TwoViewServerChessGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

public class MoveUpdateEmailTest {

    private MoveUpdateEmail template;
    private SpringTemplateEngine templateEngine;
    private Player player;
    private Player opponent;
    private ServerChessGame scg;
    private final long GAME_UID = 20202l;
    
    @Before
    public void setUp() throws Exception {
        player = new HumanPlayer("Adrian McLaughlin");
        scg = new TwoViewServerChessGame(GAME_UID, player);
        opponent = new HumanPlayer("Player 2");
        scg.setChessGameFactory(new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        });
        scg.addOpponent(opponent);
        
        FileTemplateResolver emailTemplateResolver = new FileTemplateResolver();
        emailTemplateResolver.setPrefix("src/main/resources/mail/");
        emailTemplateResolver.setTemplateMode("HTML5");
        emailTemplateResolver.setCharacterEncoding("UTF-8");
        
        templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(emailTemplateResolver);
        
        emailTemplateResolver.initialize();
        templateEngine.initialize();
     
        initialiseTemplate(scg.getPlayer(), scg);
        
    }
    
    private void initialiseTemplate(Player player, ServerChessGame scg) throws ParserConfigurationException {
        ChessBoardSVGFactory cbsi = new ChessBoardSVGFactory(scg);
        
        template = new MoveUpdateEmail(player, scg);
        template.setTemplateEngine(templateEngine);
        template.setChessBoardSVGFactory(cbsi);
        template.setTemplateEngine(templateEngine);
        
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
        assertEquals(imageBackground.getImageSource().getPath(), EmailTemplate.backgroundImagePath);
        
        EmbeddedMailImage imageChessboard = images.get(MoveUpdateEmail.CHESSBOARD_IMAGE_RESOURCE);
        assertEquals(imageChessboard.getContentId(), MoveUpdateEmail.CHESSBOARD_IMAGE_RESOURCE);
        assertEquals(imageChessboard.getContentType(), "image/jpg");
        assertNotNull(imageChessboard.getImageSource().getPath());
    }
    
    @Test
    public void testBlackKingCKMWhitePlayer() throws Exception {
        testBlackKingStatus(player, "Player 2&#39;s king has been checkmated", "Ka6:kc6:qb1");
        
    }
    
    @Test
    public void testBlackKingCKMBlackPlayer() throws Exception {
        testBlackKingStatus(opponent, "Your king has been checkmated", "Ka6:kc6:qb1");  
    }

    @Test
    public void testBlackKingCheckWhitePlayer() throws Exception {
        testBlackKingStatus(player, "Player 2&#39;s king is in check", "Ka6:ke6:qb1");
        
    }
    
    @Test
    public void testBlackKingCheckBlackPlayer() throws Exception {
        testBlackKingStatus(opponent, "Your king is in check", "Ka6:ke6:qb1");
        
    }
    
    @Test
    public void testWhiteKingCKMWhitePlayer() throws Exception {
        testWhiteKingStatus(player, "Your king has been checkmated", "ka2:Kc2:Qb8"); 
    }
    
    @Test
    public void testWhiteKingCKMBlackPlayer() throws Exception {
        testWhiteKingStatus(opponent, player.getName() + "&#39;s king has been checkmated", "ka2:Kc2:Qb8"); 
    }
    
    @Test
    public void testWhiteKingCheckWhitePlayer() throws Exception {
        testWhiteKingStatus(player, "Your king is in check", "ka2:Ke2:Qb8"); 
    }
    
    @Test
    public void testWhiteKingCheckBlackPlayer() throws Exception {
        testWhiteKingStatus(opponent, player.getName() + "&#39;s king is in check", "ka2:Ke2:Qb8"); 
    }
    
    /**
     * In running state there should be no yellow box in the email
     * @throws Exception
     */
    @Test
    public void testRunningState() throws Exception {       
        Pattern pattern = Pattern.compile("id=\"statusBox\"");
        
        String emailHtml = template.getEmailHtml();
        
        Matcher matcher = pattern.matcher(emailHtml);
        
        assertFalse(matcher.find());
        
    }
    
    private void testBlackKingStatus(Player receivingPlayer, String patternStr, String boardSetup) throws Exception {
        ChessBoard board = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation()).getChessBoard(boardSetup);
        Move queenMove = new Move(new Location("B1"), new Location("B6"));
        
        scg.getChessGame().setChessBoard(board);
        
        moveAndInitialiseTemplate(player, receivingPlayer, queenMove);
        
        checkEmailOutput(patternStr);
        
    }
    
    private void testWhiteKingStatus(Player receivingPlayer, String patternStr, String boardSetup) throws Exception {
        ChessBoard board = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation()).getChessBoard(boardSetup);
        Move queenMove = new Move(new Location("B8"), new Location("B2"));
        
        scg.getChessGame().setChessBoard(board);
        scg.getChessGame().changePlayer();
        
        moveAndInitialiseTemplate(opponent, receivingPlayer, queenMove);
        
        checkEmailOutput(patternStr);
        
    }
    
    private void moveAndInitialiseTemplate(Player player, Player receivingPlayer, Move move) throws IllegalMoveException, ParserConfigurationException{
        scg.move(scg.getPlayer(player), move);
        initialiseTemplate(scg.getPlayer(receivingPlayer), scg);
    }
    
    private void checkEmailOutput(String patternStr) {
        Pattern pattern = Pattern.compile(patternStr);
        
        String emailHtml = template.getEmailHtml();
        
        Matcher matcher = pattern.matcher(emailHtml);
        
        assertTrue(matcher.find());
    }
    
}
