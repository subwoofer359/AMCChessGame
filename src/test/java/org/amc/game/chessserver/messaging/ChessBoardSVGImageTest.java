package org.amc.game.chessserver.messaging;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.FileInputStream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;

public class ChessBoardSVGImageTest {

    private ServletContext servletContext;
    private ChessBoardSVGImage cbsi;
    private Player player;
    private ServerChessGame scg;
    private ScriptEngine engine;
    private final long GAME_UID = 20202l;
    
    @Before
    public void setUp() throws Exception {
        servletContext = mock(ServletContext.class);
        when(servletContext.getResourceAsStream(anyString()))
        .thenReturn(new FileInputStream("src/main/webapp/js/chesspieces.js"))
        .thenReturn(new FileInputStream("src/main/webapp/js/chessboard.js"));
        player = new HumanPlayer("Adrian McLaughlin");
        scg = new ServerChessGame(GAME_UID, player);
        scg.addOpponent(new HumanPlayer("Player 2"));
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        
        cbsi = new ChessBoardSVGImage(scg);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        cbsi.getChessBoardImage();
    }
    
    @Test
    public void testNullChessGame() throws Exception {
        ServerChessGame game = new ServerChessGame(GAME_UID, player);
        cbsi.setServerChessGame(game);
        cbsi.getChessBoardImage();
    }

}
