package org.amc.game.chess.controller;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Move;
import org.amc.game.chess.SimpleInputParser;

import java.io.Console;
import java.text.ParseException;

/**
 * Creates a Controller which uses the System.Console
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ConsoleController implements Controller {
    private UserConsole console = new GameTextConsole();
    private InputParser parser = new SimpleInputParser();
    private final ChessGame chessGame;

    public ConsoleController(ChessGame chessGame) {
        this.chessGame=chessGame;
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
    public void takeTurn() throws IllegalMoveException {
        String input = console.readLine("Player(%s) move:", chessGame.getCurrentPlayer().getName());
        try {
            Move move = getInputParser().parseMoveString(input);
            chessGame.move(chessGame.getCurrentPlayer(), move);
        } catch (ParseException pe) {
            throw new IllegalMoveException(pe);
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
