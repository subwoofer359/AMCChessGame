package org.amc.game.chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidMovements {

    private static final Location[] arrayOfDiagonalLocationsFromD5 = {
            // moving diagonal to the top right
            new Location("E6"),
            new Location("F7"),
            new Location("G8"),

            // moving to the top left
            new Location("C6"),
            new Location("B7"),
            new Location("A8"),

            // moving to the bottom left
            new Location("C4"), new Location("B3"),
            new Location("A2"),

            // moving to the bottom right
            new Location("E4"), new Location("F3"),
            new Location("G2"), new Location("H1"),

    };

    private static final Location[] arrayOfDUpAndDownLocationsFromD5 = {
            // moving to the top of the board
            new Location("D6"),
            new Location("D7"),
            new Location("D8"),

            // moving to the bottom of the board
            new Location("D4"), new Location("D3"),
            new Location("D2"),
            new Location("D1"),

            // moving to the right of the board
            new Location("E5"), new Location("F5"),
            new Location("G5"), new Location("H5"),

            // moving to the left of the board
            new Location("C5"), new Location("B5"),
            new Location("A5")

    };

    private final static List<Location> diagonalLocationsFromD5;
    private final static List<Location> upAndDownLocationsFromD5;

    static {
        diagonalLocationsFromD5 = new ArrayList<>();
        upAndDownLocationsFromD5 = new ArrayList<>();

        for (Location l : arrayOfDiagonalLocationsFromD5) {
            diagonalLocationsFromD5.add(l);
        }
        for (Location l : arrayOfDUpAndDownLocationsFromD5) {
            upAndDownLocationsFromD5.add(l);
        }
    }

    private ValidMovements() {
        throw new RuntimeException();
    }

    public static List<Location> getListOfDiagonalLocationsFromD5() {
        return Collections.unmodifiableList(diagonalLocationsFromD5);
    }

    public static List<Location> getListOfUpAndDownLocationsFromD5() {
        return Collections.unmodifiableList(upAndDownLocationsFromD5);
    }

}
