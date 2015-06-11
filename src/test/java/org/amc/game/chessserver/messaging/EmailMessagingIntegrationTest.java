package org.amc.game.chessserver.messaging;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.amc.User;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.ServerConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "/SpringTestConfig.xml" ,"/GameServerWebSockets.xml", "/EmailServiceContext.xml", "/GameServerSecurity.xml" })
public class EmailMessagingIntegrationTest {

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
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
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
      
        EmailTemplate template = (EmailTemplate)wac.getBean("emailTemplate");
        template.setPlayer(whitePlayer);
        template.setServerChessGame((ServerChessGame)result.getModelAndView().getModel().get("GAME"));
        System.out.println(template.getEmailHtml());
        EmailMessageService service = (EmailMessageService)wac.getBean("messageService");
        User user =new User();
        user.setEmailAddress("subwoofer359@gmail.com");
        user.setUserName("adrian");
        user.setName("Adrian McLaughlin");
        //service.send(user, template);
    }

    
}
