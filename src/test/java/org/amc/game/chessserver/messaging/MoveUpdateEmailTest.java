package org.amc.game.chessserver.messaging;

import static org.amc.game.chess.ChessBoard.Coordinate.B;
import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chessserver.ServerChessGame;
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
    private FileTemplateResolver emailTemplateResolver;
    
    @Before
    public void setUp() throws Exception {
        player = new HumanPlayer("Adrian McLaughlin");
        scg = new ServerChessGame(GAME_UID, player);
        opponent = new HumanPlayer("Player 2");
        scg.addOpponent(opponent);
        
        emailTemplateResolver = new FileTemplateResolver();
        emailTemplateResolver.setPrefix("src/main/resources/mail/");
        emailTemplateResolver.setTemplateMode("HTML5");
        emailTemplateResolver.setCharacterEncoding("UTF-8");
        
        templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(emailTemplateResolver);
        
        emailTemplateResolver.initialize();
        templateEngine.initialize();
     
        initialiseTemplate(player, scg);
        
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
        assertEquals(imageBackground.getImageSource().getPath(), template.backgroundImagePath);
        
        EmbeddedMailImage imageChessboard = images.get(MoveUpdateEmail.CHESSBOARD_IMAGE_RESOURCE);
        assertEquals(imageChessboard.getContentId(), MoveUpdateEmail.CHESSBOARD_IMAGE_RESOURCE);
        assertEquals(imageChessboard.getContentType(), "image/jpg");
        assertNotNull(imageChessboard.getImageSource().getPath());
    }
    
    @Test
    public void testBlackKingCheckMated() throws Exception {
        ChessBoard board = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation()).getChessBoard("Ka6:kc6:qb1");
        Move queenMove = new Move(new Location(B, 1), new Location(B, 6));
        
        scg.getChessGame().setChessBoard(board);
        
        scg.move(scg.getPlayer(player), queenMove);
        
        Pattern pattern = Pattern.compile("\\<span\\>Player 2s king has been checkmated\\<\\/span\\>");
        
        String emailHtml = template.getEmailHtml();
        
        Matcher matcher = pattern.matcher(emailHtml);
        
        assertTrue(matcher.find());
        
    }
    
    @Test
    public void testBlackKingIsCheck() throws Exception {
        ChessBoard board = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation()).getChessBoard("Ka6:ke6:qb1");
        Move queenMove = new Move(new Location(B, 1), new Location(B, 6));
        
        scg.getChessGame().setChessBoard(board);
        
        scg.move(scg.getPlayer(player), queenMove);
        
        Pattern pattern = Pattern.compile("\\<span\\>Player 2s king is in check\\<\\/span\\>");
        
        String emailHtml = template.getEmailHtml();
        
        Matcher matcher = pattern.matcher(emailHtml);
        
        assertTrue(matcher.find());
        
    }
    
    @Test
    public void testWhiteKingCheckMated() throws Exception {
        ChessBoard board = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation()).getChessBoard("ka2:Kc2:Qb8");
        Move queenMove = new Move(new Location(B, 8), new Location(B, 2));
        
        scg.getChessGame().setChessBoard(board);
        scg.getChessGame().changePlayer();
        
        scg.move(scg.getPlayer(opponent), queenMove);
        
        Pattern pattern = Pattern.compile("\\<span\\>Your king has been checkmated\\<\\/span\\>");
        
        String emailHtml = template.getEmailHtml();
        
        Matcher matcher = pattern.matcher(emailHtml);
        
        assertTrue(matcher.find());
        
    }
    
    @Test
    public void testWhiteKingIsCheck() throws Exception {
        ChessBoard board = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation()).getChessBoard("ka2:Ke2:Qb8");
        Move queenMove = new Move(new Location(B, 8), new Location(B, 2));
        
        scg.getChessGame().setChessBoard(board);
        scg.getChessGame().changePlayer();
        
        scg.move(scg.getPlayer(opponent), queenMove);
        
        Pattern pattern = Pattern.compile("\\<span\\>Your king is in check\\<\\/span\\>");
        Pattern statusBoxPattern = Pattern.compile("id=\"statusBox\"");
        
        String emailHtml = template.getEmailHtml();
        
        Matcher matcher = pattern.matcher(emailHtml);
        Matcher statusBoxMatcher = statusBoxPattern.matcher(emailHtml);
        
        assertTrue(matcher.find());
        assertTrue(statusBoxMatcher.find());
        
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
    
}
