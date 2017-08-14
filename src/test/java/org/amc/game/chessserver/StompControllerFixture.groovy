package org.amc.game.chessserver

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

import org.amc.dao.SCGDAOInterface;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.RealChessGamePlayer;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import groovy.transform.CompileStatic;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

class StompControllerFixture {
    ChessGamePlayer whitePlayer = new RealChessGamePlayer(new HumanPlayer("Stephen"), Colour.WHITE);

    ChessGamePlayer blackPlayer = new RealChessGamePlayer(new HumanPlayer("Chris"), Colour.BLACK);

    long gameUUID = 1234L;

    AbstractServerChessGame scg;

    Map<String, Object> sessionAttributes;

    SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);

    ArgumentCaptor<String> userArgument;

    ArgumentCaptor<String> destinationArgument;

    ArgumentCaptor<String> payloadArgument;

    ArgumentCaptor<Map> headersArgument;

    Principal principal = new Principal() {

        @Override
        public String getName() {
            return "Stephen";
        }
    };

    @Mock
    SCGDAOInterface serverChessGameDAO;
    
    @Before
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        scg = new TwoViewServerChessGame(gameUUID, whitePlayer);
        scg.setChessGameFactory(new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        });
        
        sessionAttributes = new HashMap<String, Object>();

        
        userArgument = ArgumentCaptor.forClass(String.class);
        destinationArgument = ArgumentCaptor.forClass(String.class);
        payloadArgument = ArgumentCaptor.forClass(String.class);
        headersArgument = ArgumentCaptor.forClass(Map.class);
        
        
        
        when(serverChessGameDAO.getServerChessGame(eq(gameUUID))).thenReturn(scg);
    }
    
    @CompileStatic
    void verifySimpMessagingTemplateCallToUser() {
        verify(template).convertAndSendToUser(userArgument.capture(),
                        destinationArgument.capture(), payloadArgument.capture(),
                        headersArgument.capture());
    }
}
