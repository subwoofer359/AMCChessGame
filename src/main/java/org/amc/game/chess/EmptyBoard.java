package org.amc.game.chess;

final class EmptyChessBoard extends ChessBoard {

    public static final ChessBoard EMPTY_CHESSBOARD = new EmptyChessBoard();

    EmptyChessBoard() {
    }

    @Override
    public void initialise() {
    	//do nothing
    }

    @Override
    public void move(Move move) {
    	//do nothing
    }

    @Override
    public void put(ChessPiece piece, Location location) {
    	//do nothing
    }
}