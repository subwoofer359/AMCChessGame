package org.amc.game.chess;

import java.io.Console;
import java.text.ParseException;

/**
 * Creates a Controller which uses the System.Console
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ConsoleController implements Controller {

    private ChessBoard board;
    private Player currentPlayer;
    private Player playerOne;
    private Player playerTwo;
    private UserConsole console = new GameTextConsole();
    private InputParser parser = new SimpleInputParser();

    public ConsoleController(ChessBoard board, Player playerOne, Player playerTwo) {
        this.board = board;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.currentPlayer = this.playerOne;
    }

    /**
     * @see Controller#getCurrentPlayer()
     */
    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @see Controller#changePlayer()
     */
    @Override
    public void changePlayer() {
        if (currentPlayer.equals(playerOne)) {
            currentPlayer = playerTwo;
        } else {
            currentPlayer = playerOne;
        }
    }

    /**
     * @see Controller#getInputParser()
     */
    @Override
    public InputParser getInputParser() {
        return this.parser;
    }

    /**
     * @see Controller#setInputParser(InputParser)
     */
    public void setInputParser(InputParser parser) {
        this.parser = parser;
    }

    /**
     * @see Controller#takeTurn()
     */
    public void takeTurn() throws InvalidMoveException {
        String input = console.readLine("Player(%s) move:", currentPlayer.getName());
        try {
            Move move = getInputParser().parseMoveString(input);
            board.move(currentPlayer, move);
        } catch (ParseException pe) {
            throw new InvalidMoveException(pe);
        }
    }

    /**
     * Used by Test Cases to swap in a mock UserConsole object
     */
    void setConsole(UserConsole console) {
        this.console = console;
    }

    /**
     * 
     * 
     * @author Adrian Mclaughlin
     *
     */
    static interface UserConsole {
        public String readLine(String fmt, Object... args);

        public String readLine();
    }

    /**
     * A UserConsole that uses a System.Console
     * 
     * @author Adrian Mclaughlin
     *
     */
    static class GameTextConsole implements UserConsole {
        Console console;

        public GameTextConsole() {
            console = System.console();
            // Todo add error msg if console is null
        }

        @Override
        public String readLine(String fmt, Object... args) {
            return console.readLine(fmt, args);
        }

        @Override
        public String readLine() {
            return console.readLine();
        }

    }
}
