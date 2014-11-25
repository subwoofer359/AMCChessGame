package org.amc.game.chess;

public interface ChessPieceTest {

    /**
     * Simple valid move on an empty chess board
     */
    public void testOnEmptyBoardIsValidMove();

    /**
     * Simple invalid move on an empty chess board
     */
    public void testOnEmptyBoardIsNotValidMove();

    /**
     * A move to capture an enemy piece
     */
    public void testOnBoardIsValidCapture();

    /**
     * A move to a square occupied by a player's own piece
     */
    public void testOnBoardInvalidCapture();

    /**
     * A move through an enemy piece
     */
    public void testOnBoardIsNotValidMove();

}