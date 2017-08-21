package org.amc.game.chess;

public class ChessGameFixture {

    private AbstractChessGame chessGame;
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;

    public ChessGameFixture() {
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        ChessGameFactory factory = new StandardChessGameFactory();
        chessGame = factory.getChessGame(new ChessBoard(), whitePlayer, blackPlayer);
    }

    public AbstractChessGame getChessGame() {
        return chessGame;
    }

    public ChessGamePlayer getWhitePlayer() {
        return whitePlayer;
    }

    public ChessGamePlayer getBlackPlayer() {
        return blackPlayer;
    }

    public void setBoard(ChessBoard board) {
        this.chessGame = new ChessGame(board, whitePlayer, blackPlayer);
    }

    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }
}
