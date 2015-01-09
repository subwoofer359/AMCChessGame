package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StartPageControllerJoinGameTest {
    private MockServletContext servletContext;
    private Model model;
    private ConcurrentMap<Long, ChessGame> gameMap;
    private StartPageController controller;
    private Player whitePlayer;
    private Player blackPlayer;
    private long gameUUID=1234L;
    
    @Before
    public void setUp() throws Exception {
        servletContext=new MockServletContext();
        model=new ExtendedModelMap();
        gameMap =new ConcurrentHashMap<>();
        servletContext.setAttribute(ServerConstants.GAMEMAP.toString(), gameMap);
        controller=new StartPageController();
        controller.setServletContext(servletContext);
        whitePlayer=new HumanPlayer("Ted", Colour.WHITE);
        blackPlayer=new HumanPlayer("Chris", Colour.WHITE);
        ChessGame chessGame=new ChessGame(new ChessBoard(),whitePlayer,blackPlayer);
        gameMap.put(gameUUID, chessGame);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        model.addAttribute(ServerConstants.PLAYER.toString(), whitePlayer);
        controller.joinGame(blackPlayer, gameUUID);
        
    }

}
