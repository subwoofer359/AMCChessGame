package org.amc.game.chessserver.messaging;

import static org.amc.game.chess.ChessBoard.Coordinate.B;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;

import org.amc.User;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chessserver.DatabaseSignUpFixture;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.StartPageController;
import org.amc.game.chessserver.ServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.ServerConstants;
import org.amc.game.chessserver.messaging.EmailMessageService.SendMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.Future;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "/SpringTestConfig.xml" ,"/GameServerWebSockets.xml", "/EmailServiceContext.xml", "/GameServerSecurity.xml" })
public class EmailMessagingIntegrationTest {

	private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
	
    private static final String SESSION_ATTRIBUTE = "PLAYER";

    private static final String MESSAGE_DESTINATION = "/app/move/";

    private ChessGamePlayer whitePlayer;

    private ChessGamePlayer blackPlayer;

    private long gameUUID;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SimpMessagingTemplate template;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
    	this.signUpfixture.setUp();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        Player stephen = new HumanPlayer("Stephen");
        stephen.setUserName("stephen");
        Player nobby = new HumanPlayer("Nobby");
        nobby.setUserName("nobby");
        whitePlayer = new ChessGamePlayer(stephen, Colour.WHITE);
        
        blackPlayer = new ChessGamePlayer(nobby, Colour.BLACK);
        
        
    }
    
    @After
    public void tearDown() {
        this.signUpfixture.tearDown();
    }

    @Test
    public void testMove() throws Exception {
        MvcResult result = this.mockMvc
                        .perform(post("/createGame/").sessionAttr(SESSION_ATTRIBUTE, whitePlayer)
                        .param("gameType", GameType.NETWORK_GAME.name()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(model()
                        .attributeExists(ServerConstants.GAME_UUID))
                        .andReturn();
        
        this.gameUUID = (long) result.getModelAndView().getModelMap()
                        .get(ServerConstants.GAME_UUID);
        
        this.mockMvc.perform(
                        post("/joinGame").param("gameUUID", String.valueOf(this.gameUUID))
                        .sessionAttr(ServerConstants.PLAYER, blackPlayer))
                        .andExpect(status()
                        .isOk())
                        .andDo(print());
        
        result = this.mockMvc.perform(
                        post("/joinGame").param("gameUUID", String.valueOf(this.gameUUID))
                        .sessionAttr(ServerConstants.PLAYER, whitePlayer))
                        .andExpect(status()
                        .isOk())
                        .andDo(print())
                        .andReturn();
      
        
        EmailTemplateFactory factory = (EmailTemplateFactory)wac.getBean("emailTemplateFactory");
        EmailTemplate template = factory.getEmailTemplate(ChessGame.class);
        template.setPlayer(whitePlayer);
        
        EmailTemplate playerJoinedGameEmail = factory.getEmailTemplate(Player.class);
        EmailTemplate playerQuitGameEmail = factory.getEmailTemplate(Player.class, ServerGameStatus.FINISHED);
        
        playerJoinedGameEmail.setPlayer(blackPlayer);
        playerQuitGameEmail.setPlayer(blackPlayer);
        
        ChessBoard board = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation()).getChessBoard("Ka6:kc6:qb1");
        Move queenMove = new Move(new Location(B, 1), new Location(B, 6));
        
        ServerChessGame scg = (ServerChessGame)result.getModelAndView().getModel().get("GAME");
        
        scg.getChessGame().setChessBoard(board);
        
        scg.move(whitePlayer, queenMove);
        
        template.setServerChessGame(scg);
        playerJoinedGameEmail.setServerChessGame(scg);
        playerQuitGameEmail.setServerChessGame(scg);
        
        EmailMessageService service = (EmailMessageService)wac.getBean("messageService");
        User user =new User();
        user.setEmailAddress("subwoofer359@gmail.com");
        user.setUserName("adrian");
        user.setName("Adrian McLaughlin");
        
        Future<String> mailresult = service.send(user, template);
        Future<String> mailresult2 = service.send(user, playerJoinedGameEmail);
        Future<String> mailresult3 = service.send(user, playerQuitGameEmail);
        
        assertEquals(SendMessage.SENT_SUCCESS, mailresult.get());
        assertEquals(SendMessage.SENT_SUCCESS, mailresult2.get());
        assertEquals(SendMessage.SENT_SUCCESS, mailresult3.get());
    }

    
}
