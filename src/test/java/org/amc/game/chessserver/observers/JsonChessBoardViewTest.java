package org.amc.game.chessserver.observers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.ChessBoardFactory;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chess.StandardChessGameFactory;
import org.amc.game.chess.view.ChessPieceTextSymbol;
import org.amc.game.chessserver.MessageType;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.StompController;
import org.amc.game.chessserver.TwoViewServerChessGame;
import org.amc.game.chessserver.observers.JsonChessGameView;
import org.amc.game.chessserver.observers.JsonChessGameView.JsonChessGame;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Test Case for JsonChessBoardView
 * 
 * @author Adrian Mclaughlin
 *
 */
public class JsonChessBoardViewTest {

    private ServerChessGame serverGame;
    private ChessGame chessGame;
    private static ChessBoardFactory chBoardFactory;
    private ChessGamePlayer whitePlayer;
    private SimpMessagingTemplate template;
    private GsonBuilder gson;
    
    @SuppressWarnings("rawtypes")
    private ArgumentCaptor<Map> headersArgument;
    private ArgumentCaptor<String> destinationArgument;
    ArgumentCaptor<String> messageArgument;
    
    private static final long GAME_UID = 1234l;

    @BeforeClass
    public static void setupFactory() {
        chBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
    }

    @Before
    public void setUp() throws Exception {
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        ChessGamePlayer blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        ChessBoard board = chBoardFactory.getChessBoard("Ke8:Qf8:Pe7:Pf7:ke1:qd1:pe2:pd2:pg4");
        chessGame = new StandardChessGameFactory().getChessGame(board, whitePlayer, blackPlayer);

        template = mock(SimpMessagingTemplate.class);
        headersArgument = ArgumentCaptor.forClass(Map.class);
        messageArgument = ArgumentCaptor.forClass(String.class);
        destinationArgument = ArgumentCaptor.forClass(String.class);
        
        serverGame = new TwoViewServerChessGame(GAME_UID, chessGame);
        chessGame = serverGame.getChessGame();
        
        gson = new GsonBuilder();
        gson.registerTypeAdapter(Player.class, new PlayerDeserializer());
        gson.registerTypeHierarchyAdapter(ChessGamePlayer.class, new ChessGamePlayerDeserializer());
        
        new JsonChessGameView(template).setGameToObserver(serverGame);
        
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Moves piece on chessboard to trigger an update to JsonChessBoardView
     * Compares both the ChessBoard configuration to the JSON representation for
     * correctness.
     * 
     * @throws IllegalMoveException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void test() throws IllegalMoveException {
        serverGame.move(whitePlayer, new Move("E2-E3"));
        
        verify(template).convertAndSend(destinationArgument.capture(), messageArgument.capture(), headersArgument.capture());

        JsonChessGame jBoard = gson.create().fromJson(messageArgument.getValue(), JsonChessGame.class);

        compareChessBoardAndJsonBoard(jBoard);

        compareChessGamePlayerAndJsonPlayer(jBoard);
        
        checkForUpdateMessageHeader();
        
        verifyMessageDestination(destinationArgument.getValue());
    }

    private void compareChessBoardAndJsonBoard(JsonChessGame jBoard) {
        for (String squareName : jBoard.getSquares().keySet()) {
            String file = squareName.substring(0, 1);
            String rank = squareName.substring(1, 2);
            String expectedChessSymbol = getChessPieceSymbol(file, rank);
            String actualChessSymbol = jBoard.getSquares().get(squareName);

            assertEquals(expectedChessSymbol, actualChessSymbol);
        }
    }

    private void compareChessGamePlayerAndJsonPlayer(JsonChessGame game) {
        assertEquals(this.chessGame.getCurrentPlayer().getName(), game.getCurrentPlayer().getName());
        assertEquals(this.chessGame.getCurrentPlayer().getColour(), game.getCurrentPlayer()
                        .getColour());
    }

    private String getChessPieceSymbol(String file, String rank) {
        return String.valueOf(ChessPieceTextSymbol.getChessPieceTextSymbol(chessGame.getChessBoard()
                        .getPieceFromBoardAt(getLocation(file, rank))));
    }

    private Location getLocation(String file, String rank) {
        return new Location(Coordinate.valueOf(file), Integer.parseInt(rank));
    }

    private void checkForUpdateMessageHeader(){
        assertEquals(MessageType.UPDATE,headersArgument.getValue().get(StompController.MESSAGE_HEADER_TYPE));
    }
    
    private void verifyMessageDestination(String destination) {
        assertEquals(String.format("%s/%d", JsonChessGameView.MESSAGE_DESTINATION, GAME_UID), destination);
    }
    
    /**
     * GSON Deserialiser required to deserialise Player objects
     * 
     * @author Adrian Mclaughlin
     *
     */
    private class PlayerDeserializer implements JsonDeserializer<Player> {

        @Override
        public Player deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
                        throws JsonParseException {
            JsonObject object = arg0.getAsJsonObject();
            return new HumanPlayer(object.get("name").getAsString());
        }
    }
    
    private class ChessGamePlayerDeserializer implements JsonDeserializer<ChessGamePlayer> {

        @Override
        public ChessGamePlayer deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
                        throws JsonParseException {
            JsonObject object = arg0.getAsJsonObject();
            JsonObject playerJson = object.getAsJsonObject("player");
            Colour colour = Colour.valueOf(object.get("colour").getAsString());
            return new RealChessGamePlayer(new HumanPlayer(playerJson.get("name").getAsString()), colour);
        }
    }

}
