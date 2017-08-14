package org.amc.game.chessserver;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.SESSION_ATTRIBUTES;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Player;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.apache.log4j.Logger;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.Map;

@Controller
public class PromotionStompController extends StompController {
    private static final Logger logger = Logger.getLogger(PromotionStompController.class);
    
    static final String PROMOTION_ERROR_MSG = "Player can only promote their own pawn";
    
    static final String PARSE_ERROR = "No promote header";
    
    @MessageMapping("/promote/{gameUUID}")
    @SendToUser(value = "/queue/updates", broadcast = false)
    public void promotePawnTo(Principal user,
                    @Header(SESSION_ATTRIBUTES) Map<String, Object> wsSession,
                    @DestinationVariable long gameUUID, @Payload String promotionMessage) {
        logger.debug(String.format("USER(%s)'s move received for game:%d", user.getName(), gameUUID));

        AbstractServerChessGame scGame = getServerChessGame(gameUUID);
        
        Player player;
        
        if(scGame instanceof OneViewServerChessGame) {
        	player = scGame.getChessGame().getCurrentPlayer();
        } else {
        	player = (Player) wsSession.get("PLAYER");
        }
 
        String message = checkPlayerCanPromotePawn(player, scGame, promotionMessage);
        
        if(isMessageNotEmpty(message)) {
            sendMessageToUser(user, message, MessageType.ERROR);
        }
    }
    
    private String checkPlayerCanPromotePawn(Player player, AbstractServerChessGame scGame, String promotionMessage) {
    	String message;
    	if(player != null && ComparePlayers.isSamePlayer(player, scGame.getChessGame().getCurrentPlayer())){
        	message = tryToPromotionPawn(scGame, promotionMessage);
        } else {
        	message = PROMOTION_ERROR_MSG;
        }
    	return message;
    }
    
    private String tryToPromotionPawn(AbstractServerChessGame scGame, String promotionMessage) {
    	String message = "";
    	try {
    		ChessPieceLocation newPiece = parsePromotionString(promotionMessage);
    		scGame.promotePawnTo(newPiece.getPiece(), newPiece.getLocation());
    	} catch(IllegalMoveException | ParseException ime) {
    		message = ime.getMessage();
    	}
    	return message;
    }

    ChessPieceLocation parsePromotionString(String promotionString) throws ParseException {
        String[] d = promotionString.split("\\s");
        String promote = d[0];
        if("promote".equals(promote)) {
            String chessPieceLocationStr = d[1]; 
            SimpleChessBoardSetupNotation sbsn = new SimpleChessBoardSetupNotation();
            return sbsn.getChessPieceLocations(chessPieceLocationStr).get(0);
        } else {
            throw new ParseException(PARSE_ERROR, 0);
        }
    }
}
