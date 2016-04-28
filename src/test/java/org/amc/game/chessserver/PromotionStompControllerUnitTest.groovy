package org.amc.game.chessserver;


import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Move;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chess.StandardChessGameFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

class PromotionStompControllerUnitTest extends StompControllerFixture {

    PromotionStompController controller;
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        controller = new PromotionStompController();
        controller.setTemplate(template);
        controller.setServerChessDAO(serverChessGameDAO);
        scg.addOpponent(blackPlayer);
        
        def chessBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        def board  = chessBoardFactory.getChessBoard("Ke8:ke1:pa7:pa6");
        def factory = new StandardChessGameFactory();
        def chessGame = factory.getChessGame(board, whitePlayer, blackPlayer);
        scg.setChessGame(chessGame);
        scg.move(whitePlayer, new Move("a7-a8")); 
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        def message = 'promote qa8';
        
        controller.promotePawnTo(principal, sessionAttributes, gameUUID, message);
        this.verifySimpMessagingTemplateCallToUser();
        assert headersArgument.value[StompController.MESSAGE_HEADER_TYPE] == MessageType.INFO;
    }
    
    @Test
    public void testNoPromoteHeaderToString() {
        def message = 'prom qa8';
        
        controller.promotePawnTo(principal, sessionAttributes, gameUUID, message);
        this.verifySimpMessagingTemplateCallToUser();
        assert headersArgument.value[StompController.MESSAGE_HEADER_TYPE] == MessageType.ERROR;
    }
    
    @Test
    public void testPromotionOfNonEligiblePiece() {
        def message = 'promote qe1';
        controller.promotePawnTo(principal, sessionAttributes, gameUUID, message);
        this.verifySimpMessagingTemplateCallToUser();
        assert headersArgument.value[StompController.MESSAGE_HEADER_TYPE] == MessageType.ERROR;
    }
}
