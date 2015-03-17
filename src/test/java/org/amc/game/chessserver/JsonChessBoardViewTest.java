package org.amc.game.chessserver;

import com.google.gson.Gson;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.amc.game.chess.ChessBoard.Coordinate.E;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.ChessBoardFactory;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.InvalidMoveException;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.ObservableChessGame;
import org.amc.game.chess.Player;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chess.view.ChessPieceTextSymbol;
import org.amc.game.chessserver.JsonChessBoardView.JsonChessBoard;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Test Case for JsonChessBoardView
 * 
 * @author Adrian Mclaughlin
 *
 */
public class JsonChessBoardViewTest {

    private ObservableChessGame chessGame;
    private ChessBoard board;
    private static ChessBoardFactory chBoardFactory;
    private Player whitePlayer;
    private Player blackPlayer;
    private SimpMessagingTemplate template;
    private Gson gson;

    @BeforeClass
    public static void setupFactory() {
        chBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
    }

    @Before
    public void setUp() throws Exception {
        whitePlayer = new HumanPlayer("White Player", Colour.WHITE);
        blackPlayer = new HumanPlayer("Black Player", Colour.BLACK);
        board = chBoardFactory.getChessBoard("Ke8:Qf8:Pe7:Pf7:ke1:qd1:pe2:pd2:pg4");
        chessGame = new ObservableChessGame(board, whitePlayer, blackPlayer);
        template = mock(SimpMessagingTemplate.class);
        gson = new Gson();
        new JsonChessBoardView(chessGame, template);
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Moves piece on chessboard to trigger an update to JsonChessBoardView
     * Compares both the ChessBoard configuration to the JSON representation
     * for correctness.
     * 
     * @throws InvalidMoveException
     */
    @Test
    public void test() throws InvalidMoveException {
        chessGame.move(whitePlayer, new Move(new Location(E, 2), new Location(E, 3)));
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(template).convertAndSend(anyString(), argument.capture());

        JsonChessBoard jBoard = gson.fromJson(argument.getValue(), JsonChessBoard.class);

        compareChessBoardAndJsonBoard(jBoard);
    }

    private void compareChessBoardAndJsonBoard(JsonChessBoard jBoard) {
        for (String squareName : jBoard.getSquares().keySet()) {
            String file = squareName.substring(0, 1);
            String rank = squareName.substring(1, 2);
            String expectedChessSymbol = getChessPieceSymbol(file, rank);
            String actualChessSymbol = jBoard.getSquares().get(squareName);

            assertEquals(expectedChessSymbol, actualChessSymbol);
        }
    }

    private String getChessPieceSymbol(String file, String rank) {
        return String.valueOf(ChessPieceTextSymbol.getChessPieceTextSymbol(board
                        .getPieceFromBoardAt(getLocation(file, rank))));
    }

    private Location getLocation(String file, String rank) {
        return new Location(Coordinate.valueOf(file), Integer.parseInt(rank));
    }

}
