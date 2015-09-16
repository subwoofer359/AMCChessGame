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
     * @param chessGame ChessBoard
     * @param move Move
     */
    void applyRule(ChessGame chessGame,Move move);

    /**
     * Is the rule valid for the move and current chess board configuartion
     * 
     * @param game ChessBoard
     * @param move Move
     * @return Boolean
     */
    boolean isRuleApplicable(ChessGame game,Move move);

}
