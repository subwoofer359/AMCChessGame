package org.amc.game.chess;


public interface ChessMoveRule {
    
    void applyRule(ChessBoard board,Move move);
    
    void unapplyRule(ChessBoard board,Move move);
    
    boolean isRuleApplicable(ChessBoard board,Move move);

}
