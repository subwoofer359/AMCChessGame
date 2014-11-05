package org.amc.game.chess;

public class ChessGame {
    private ChessBoard board;
    private ChessBoardView view;
    private Controller controller;
    
    public ChessGame() {
        
    }

    public final void setBoard(ChessBoard board) {
        this.board = board;
    }

    public final void setView(ChessBoardView view) {
        this.view = view;
    }

    public final void setController(Controller controller) {
        this.controller = controller;
    }

    
}
