package org.amc.game.chessserver;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.SESSION_ATTRIBUTES;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.PawnPromotionRule;
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
    
    
    @MessageMapping("/promote/{gameUUID}")
    @SendToUser(value = "/queue/updates", broadcast = false)
    public void promotePawnTo(Principal user,
                    @Header(SESSION_ATTRIBUTES) Map<String, Object> wsSession,
                    @DestinationVariable long gameUUID, @Payload String promotionMessage) {
        logger.debug(String.format("USER(%s)'s move received for game:%d", user.getName(), gameUUID));
        
        AbstractServerChessGame scGame = getServerChessGame(gameUUID);
        
        PawnPromotionRule promotionRule = PawnPromotionRule.getInstance();

        String message = "";
        
        try {
            ChessPieceLocation newPiece = parsePromotionString(promotionMessage);
            promotionRule.promotePawnTo(scGame.getChessGame(), newPiece.getLocation(), newPiece.getPiece());
        } catch(IllegalMoveException | ParseException ime) {
            message = ime.getMessage();
        }
        
        MessageType type = "".equals(message) ? MessageType.INFO : MessageType.ERROR;
        sendMessageToUser(user, message, type);
    }

    ChessPieceLocation parsePromotionString(String promotionString) throws ParseException {
        String[] d = promotionString.split("\\s");
        String promote = d[0];
        if("promote".equals(promote)) {
            String chessPieceLocationStr = d[1]; 
            SimpleChessBoardSetupNotation sbsn = new SimpleChessBoardSetupNotation();
            return sbsn.getChessPieceLocations(chessPieceLocationStr).get(0);
        } else {
            throw new ParseException("No promote header", 0);
        }
    }
}
