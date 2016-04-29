package org.amc.game.chessserver

import static org.amc.game.chessserver.StompController.MESSAGE_HEADER_TYPE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.amc.dao.DAO;
import org.amc.dao.EntityManagerCache;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.AbstractChessGame.GameState;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.KingPiece;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.QueenPiece;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chess.StandardChessGameFactory;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.observers.JsonChessGameView;
import org.apache.openjpa.kernel.exps.This;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.SESSION_ATTRIBUTES;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;



@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration([ "/SpringTestConfig.xml", "/GameServerSecurity.xml",
    "/GameServerWebSockets.xml", "/EmailServiceContext.xml" ])
class PromotionStompControllerIT extends StompControllerFixtureIT {
    
    static final String MESSAGE_DESTINATION = "/app/promote/";
    
    static final Long ONEVIEW_GAMEUUID  = 9409409L;

    @Before
    public void setup() throws Exception {
        super.setup();
        clearDAO();
        //createChessGame();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    private void clearDAO() {
        def entityCache = (EntityManagerCache) wac.getBean('myEntityManagerCache');
        entityCache.getEntityManager(gameUUID).close();
    }

    private void createChessGame() {
        def scgfactory = (ServerChessGameFactory) wac.getBean("serverChessGameFactory");
        def chessBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        def board  = chessBoardFactory.getChessBoard("Ke8:ke1:pa7");
        def sGameFactory = new StandardChessGameFactory();

        def scg = scgfactory.getServerChessGame(GameType.NETWORK_GAME, gameUUID, stephen);
        scg.addOpponent(nobby);
        scg.setChessGame(sGameFactory.getChessGame(board, scg.getPlayer(stephen), scg.getPlayer(nobby)));
        
        Move move = new Move("a7:a8");
        scg.move(scg.getPlayer(stephen), move);
        serverChessGameDAO.saveServerChessGame(scg);
    }
    
    private void createOneViewChessGame() {
        def scgfactory = (ServerChessGameFactory) wac.getBean("serverChessGameFactory");
        def chessBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        def board  = chessBoardFactory.getChessBoard("Ke8:ke1:pa7:Pa2");
        def sGameFactory = new StandardChessGameFactory();

        def scg = scgfactory.getServerChessGame(GameType.LOCAL_GAME, ONEVIEW_GAMEUUID, stephen);
        scg.addOpponent(nobby);
        scg.setChessGame(sGameFactory.getChessGame(board, scg.getPlayer(stephen), scg.getPlayer(nobby)));
        
        scg.chessGame.changePlayer();
        Move move = new Move("a2:a1");
        scg.move(scg.getPlayer(nobby), move);
        serverChessGameDAO.saveServerChessGame(scg);
    }

    @Test
    public void testWhitePromote() {
        createChessGame()
        def game = serverChessGameDAO.getServerChessGame(gameUUID);
        assert game.getChessGame().gameState == GameState.PAWN_PROMOTION;

        subscribe();
        promote(stephen, gameUUID, MESSAGE_DESTINATION, "promote qa8");
        consumePromotionStatusUpdate();
        testInfoMessageSent();

        game = serverChessGameDAO.getServerChessGame(gameUUID);
        

        ChessPiece chessPiece =  game.getChessGame().getChessBoard().getPieceFromBoardAt(new Location("a8"));
        assert chessPiece != null;
        assert chessPiece.colour == Colour.WHITE;
        assert chessPiece.getClass() == QueenPiece.class;
        assert game.getChessGame().gameState == GameState.RUNNING;
    }

    @Test
    public void testBlackPromote() {
        createOneViewChessGame();
        def game = serverChessGameDAO.getServerChessGame(ONEVIEW_GAMEUUID);
        assert game.getChessGame().gameState == GameState.PAWN_PROMOTION;

        subscribe();
        promote(nobby, ONEVIEW_GAMEUUID, MESSAGE_DESTINATION, "promote Qa1");
        consumePromotionStatusUpdate();
        testInfoMessageSent();

        game = serverChessGameDAO.getServerChessGame(ONEVIEW_GAMEUUID);
        

        ChessPiece chessPiece =  game.getChessGame().getChessBoard().getPieceFromBoardAt(new Location("a1"));
        assert chessPiece != null;
        assert chessPiece.colour == Colour.BLACK;
        assert chessPiece.getClass() == QueenPiece.class;
        assert game.getChessGame().gameState == GameState.RUNNING;
    }
    
    
    private void consumePromotionStatusUpdate() {
        testStatusMessageSent();
    }
    
    @Test
    public void testInvalidPromote() {
        createChessGame()
        def game = serverChessGameDAO.getServerChessGame(gameUUID);
        assert game.getChessGame().gameState == GameState.PAWN_PROMOTION;

        subscribe();
        promote(stephen, gameUUID, MESSAGE_DESTINATION, "promote qe1");
        consumePromotionStatusUpdate();
        testErrorMessageSent();

        game = serverChessGameDAO.getServerChessGame(gameUUID);
        assert game.getChessGame().gameState == GameState.PAWN_PROMOTION;

        ChessPiece chessPiece =  game.getChessGame().getChessBoard().getPieceFromBoardAt(new Location("e1"));
        assert chessPiece != null;
        assert chessPiece.colour == Colour.WHITE;
        assert chessPiece.getClass() == KingPiece.class;
    }

    private void promote(Player player, long gameUid, String messageDestination, String promoteString)
    throws Exception {
        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SEND);
        headers.setSubscriptionId(SUBSCRIPTION_ID);
        headers.setDestination(messageDestination + gameUid);
        headers.setSessionId(SESSION_ID);
        headers.setUser(getTestPrincipal());
        Map<String, Object> sessionAttributes = new HashMap<String, Object>();
        sessionAttributes.put(SESSION_ATTRIBUTE, player);
        headers.setSessionAttributes(sessionAttributes);
        Message<byte[]> message = MessageBuilder.createMessage(promoteString.getBytes(),
                headers.getMessageHeaders());
        System.out.println(message);
        this.clientInboundChannel.send(message);
    }
    
    @Controller
	public static class PromotionStompControllerHelper extends PromotionStompController {

		static final String TEST_MESSAGE = "Testing message";
	
		@Override
		@MessageMapping("/promote/{gameUUID}")
    	@SendToUser(value = "/queue/updates", broadcast = false)
		public void promotePawnTo(Principal user,
        	    @Header(SESSION_ATTRIBUTES) Map<String, Object> wsSession,
            	@DestinationVariable long gameUUID, @Payload String promotionMessage) {
			super.promotePawnTo(user, wsSession, gameUUID, promotionMessage);
			sendMessageToUser(user, TEST_MESSAGE, MessageType.INFO);
		}

	
}
    
}
