package org.amc.game.chessserver.messaging;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;

import org.amc.User;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.DatabaseSignUpFixture;
import org.amc.game.chessserver.ServerChessGame;
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

	//private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
	
    private static final String SESSION_ATTRIBUTE = "PLAYER";

    private static final String MESSAGE_DESTINATION = "/app/move/";

    private ChessGamePlayer whitePlayer = new ChessGamePlayer(new HumanPlayer("Stephen"),
                    Colour.WHITE);

    private ChessGamePlayer blackPlayer = new ChessGamePlayer(new HumanPlayer("Chris"),
                    Colour.BLACK);

    private long gameUUID;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SimpMessagingTemplate template;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
    	//this.signUpfixture.setUp();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    @After
    public void tearDown() {
        //this.signUpfixture.tearDown();
    }

    @Test
    public void testMove() throws Exception {
        MvcResult result = this.mockMvc
                        .perform(post("/createGame").sessionAttr(SESSION_ATTRIBUTE, whitePlayer))
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
        EmailTemplate playerJoinedGameEmail = factory.getEmailTemplate(Player.class);
        template.setPlayer(whitePlayer);
        
        playerJoinedGameEmail.setPlayer(blackPlayer);
        ServerChessGame scg = (ServerChessGame)result.getModelAndView().getModel().get("GAME");
        
        //scg.move(whitePlayer, new Move(new Location(Coordinate.A,2), new Location(Coordinate.A,3)));
        template.setServerChessGame(scg);
        playerJoinedGameEmail.setServerChessGame(scg);
        
        EmailMessageService service = (EmailMessageService)wac.getBean("messageService");
        User user =new User();
        user.setEmailAddress("subwoofer359@gmail.com");
        user.setUserName("adrian");
        user.setName("Adrian McLaughlin");
        Future<String> mailresult = service.send(user, template);
        Future<String> mailresult2 = service.send(user, playerJoinedGameEmail);
        assertEquals(SendMessage.SENT_SUCCESS, mailresult.get());
        assertEquals(SendMessage.SENT_SUCCESS, mailresult2.get());
    }

    
}
