package org.amc.game.chessserver;

import com.google.gson.Gson;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Location;
import org.amc.game.chess.Player;
import org.amc.game.chess.view.ChessPieceTextSymbol;
import org.amc.util.Observer;
import org.amc.util.Subject;
import org.apache.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

public class JsonChessGameView implements Observer {

    private static final Logger logger = Logger.getLogger(JsonChessGameView.class);

    /** 
     * Google gson object to convert to JSON 
     */
    private final Gson gson = new Gson();

    /** 
     * STOMP messaging object to send stomp message to objects
     */
    private SimpMessagingTemplate template;

    /**
     * STOMP message subscription destination
     */
    private static final String MESSAGE_DESTINATION = "/topic/updates";

    /**
     * Constructor for JsonChessBoardView
     * @param chessGame to Observe
     * @param template Stomp message template
     */
    public JsonChessGameView(ServerChessGame chessGame, SimpMessagingTemplate template) {
        this.template = template;
        chessGame.attachObserver(this);
    }

    /**
     * Called by Model(ObservableChessGame) on change
     * 
     * Converts a ChessBoard to a JSON String and sends it
     * to the subscribed clients
     */
    @Override
    public void update(Subject subject, Object message) {
        if (message instanceof ChessGame) {
            String jsonBoard = gson.toJson(new JsonChessGame((ChessGame) message));
            this.template.convertAndSend(MESSAGE_DESTINATION, jsonBoard);
            logger.debug("Message sent to" + MESSAGE_DESTINATION);
        } else {
            // Ignore update notification
        }
    }
    
    /**
     * Helper class for creating JSON representation of a ChessBoard
     * @author Adrian Mclaughlin
     *
     */
    static class JsonChessGame {

        private Map<String, String> squares;
        private Player currentPlayer;
       
        private JsonChessGame(){
            squares = new HashMap<>();
        }
        
        /**
         * Stores the coordinates of squares containing chesspieces in the Map squares 
         * @param board
         */
        private void convertChessBoard(ChessBoard board) {
            
            for (int rank = 1; rank <= ChessBoard.BOARD_WIDTH; rank++) {
                for (Coordinate file : Coordinate.values()) {
                    ChessPiece piece = board.getPieceFromBoardAt(new Location(file, rank));
                    if (piece != null) {
                        squares.put(file.toString() + rank, String.valueOf(ChessPieceTextSymbol
                                        .getChessPieceTextSymbol(piece)));
                    }
                }
            }
        }
        
        public JsonChessGame(ChessGame chessGame){
            this();
            convertChessBoard(chessGame.getChessBoard());
            currentPlayer=chessGame.getCurrentPlayer();
        }
       

        Map<String, String> getSquares() {
            return squares;
        }
        
        Player getCurrentPlayer(){
            return this.currentPlayer;
        }
    }
}
