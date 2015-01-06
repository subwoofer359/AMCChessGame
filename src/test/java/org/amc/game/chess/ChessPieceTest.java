package org.amc.game.chess;

public abstract class ChessPieceTest {
    
    /**
     * Simple valid move on an empty chess board
     */
    public abstract void testOnEmptyBoardIsValidMove();

    /**
     * Simple invalid move on an empty chess board
     */
    public abstract void testOnEmptyBoardIsNotValidMove();

    /**
     * A move to capture an enemy piece
     */
    public abstract void testOnBoardIsValidCapture();

    /**
     * A move to a square occupied by a player's own piece
     */
    public abstract void testOnBoardInvalidCapture();

    /**
     * A move through an enemy piece
     */
    public abstract void testOnBoardIsNotValidMove();
    
    /**
     * Test if piece can slide
     */
    public abstract void testCanSlide();
}