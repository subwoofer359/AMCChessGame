package org.amc.game.chessserver.observers;

import static org.amc.game.chess.NoChessPiece.NO_CHESSPIECE;

import com.google.gson.Gson;

import org.amc.game.GameObserver;
import org.amc.game.chess.AbstractChessGame.GameState;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.AbstractChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Location;
import org.amc.game.chess.view.ChessPieceTextSymbol;
import org.amc.game.chessserver.MessageType;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.StompController;
import org.amc.util.Subject;
import org.apache.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

public class JsonChessGameView extends GameObserver {

    private static final Logger logger = Logger.getLogger(JsonChessGameView.class);

    /** 
     * STOMP messaging object to send stomp message to objects
     */
    private final SimpMessagingTemplate template;

    /**
     * STOMP message subscription destination
     */
    static final String MESSAGE_DESTINATION = "/topic/updates";

    /**
     * Constructor for JsonChessBoardView
     * @param template Stomp message template
     */
    public JsonChessGameView(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Called by Model(ObservableChessGame) on change
     * 
     * Converts a ChessBoard to a JSON String and sends it
     * to the subscribed clients
     */
    @Override
    public void update(Subject subject, Object message) {
        if (message instanceof AbstractChessGame && subject instanceof AbstractServerChessGame) {
            AbstractServerChessGame serverChessGame = (AbstractServerChessGame) subject;
            String jsonBoard = convertChessGameToJson((AbstractChessGame)message);
       
            this.template.convertAndSend(getMessageDestination(serverChessGame), jsonBoard, getDefaultHeaders());
            logger.debug("Message sent to " + getMessageDestination(serverChessGame));
        }
    }
    
    private String getMessageDestination(AbstractServerChessGame serverChessGame) {
        return String.format("%s/%d", MESSAGE_DESTINATION, serverChessGame.getUid());
    }
    
    private Map<String,Object> getDefaultHeaders(){
        Map<String,Object> headers = new HashMap<>();
        headers.put(StompController.MESSAGE_HEADER_TYPE, MessageType.UPDATE);
        return headers;
    }
    
    public static String convertChessGameToJson(AbstractChessGame chessGame) {
        final Gson gson = new Gson();
        return gson.toJson(new JsonChessGame(chessGame));
    }
    
    /**
     * Helper class for creating JSON representation of a ChessBoard
     * @author Adrian Mclaughlin
     *
     */
    public static class JsonChessGame {

        private Map<String, String> squares;
        
        private ChessGamePlayer currentPlayer;
        
        private GameState gameState;
        
        private String lastMove;
       
        private JsonChessGame(){
            squares = new HashMap<>();
        }
        
        /**
         * Stores the coordinates of squares containing chesspieces in the Map squares 
         * @param board {@link ChessBoard}
         */
        private void convertChessBoard(ChessBoard board) {
            
            for (int rank = 1; rank <= ChessBoard.BOARD_WIDTH; rank++) {
                for (Coordinate file : Coordinate.values()) {
                    ChessPiece piece = board.get(new Location(file, rank));
                    if (piece != NO_CHESSPIECE) {
                        squares.put(file.toString() + rank, String.valueOf(ChessPieceTextSymbol
                                        .getChessPieceTextSymbol(piece)));
                    }
                }
            }
        }
        
        public JsonChessGame(AbstractChessGame chessGame) {
            this();
            convertChessBoard(chessGame.getChessBoard());
            currentPlayer = chessGame.getCurrentPlayer();
            gameState = chessGame.getGameState();
            lastMove = chessGame.getTheLastMove().asString();
        }
       

        Map<String, String> getSquares() {
            return squares;
        }
        
        ChessGamePlayer getCurrentPlayer(){
            return this.currentPlayer;
        }
        
        GameState getGameState() {
            return this.gameState;
        }
        
        String getLastMove() {
        	return this.lastMove;
        }
        
    }
}
