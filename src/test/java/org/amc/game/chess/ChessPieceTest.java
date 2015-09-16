package org.amc.game.chess;

public abstract class ChessPieceTest {
    
    /**
     * Simple valid move on an empty chess board
     */
    public abstract void testOnEmptyBoardIsValidMove() throws Exception;

    /**
     * Simple invalid move on an empty chess board
     */
    public abstract void testOnEmptyBoardIsNotValidMove() throws Exception;

    /**
     * A move to capture an enemy piece
     */
    public abstract void testOnBoardIsValidCapture() throws Exception;

    /**
     * A move to a square occupied by a player's own piece
     */
    public abstract void testOnBoardInvalidCapture() throws Exception;

    /**
     * A move through an enemy piece
     */
    public abstract void testOnBoardIsNotValidMove() throws Exception;
    
    /**
     * Test if piece can slide
     */
    public abstract void testCanSlide() throws Exception;
}