package org.amc.game.chess;

/**
 * Encapsulate a special chess move rule
 * 
 * @author Adrian Mclaughlin
 *
 */
public interface ChessMoveRule {
    
    /**
     * Apply the rule to the board
     * 
     * @param board ChessBoard
     * @param move Move
     */
    void applyRule(ChessBoard board,Move move);

    /**
     * Is the rule valid for the move and current chess board configuartion
     * 
     * @param board ChessBoard
     * @param move Move
     * @return Boolean
     */
    boolean isRuleApplicable(ChessBoard board,Move move);

}
