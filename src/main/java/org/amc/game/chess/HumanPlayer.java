package org.amc.game.chess;

/**
 * Represents a Human player in a game of chess
 * 
 * @author adrian
 *
 */
public class HumanPlayer implements Player {
    private String name;
    private Colour colour;

    public HumanPlayer(String name, Colour colour) {
        this.name = name;
        this.colour = colour;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Colour getColour() {
        return this.colour;
    }
}
