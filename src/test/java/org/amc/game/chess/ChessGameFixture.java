package org.amc.game.chess;

public class ChessGameFixture {

    private ChessBoard board;;
    private ChessGame chessGame;
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;

    public ChessGameFixture() {
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        ChessGameFactory factory = new StandardChessGameFactory();
        chessGame = factory.getChessGame(new ChessBoard(), whitePlayer, blackPlayer);
        board = chessGame.getChessBoard();
    }

    public void putPieceOnBoardAt(ChessPiece piece, Location location) {
        if (board != null) {
            board.putPieceOnBoardAt(piece, location);
        }
    }

    public void removePieceOnBoardAt(Location location) {
        if (board != null) {
            board.removePieceOnBoardAt(location);
        }
    }

    public ChessPiece getPieceFromBoardAt(Location location) {
        if (board != null) {
            return board.getPieceFromBoardAt(location);
        } else {
            return null;
        }
    }

    public void move(ChessGamePlayer player, Move move) throws IllegalMoveException {
        chessGame.move(player, move);
    }

    public void changePlayer() {
        chessGame.changePlayer();
    }

    public ChessGamePlayer getCurrentPlayer() {
        return chessGame.getCurrentPlayer();
    }

    public boolean isCheckMate(ChessGamePlayer player, ChessBoard board) {
        return chessGame.isCheckMate(player, board);
    }

    public ChessBoard getBoard() {
        return board;
    }

    public ChessGame getChessGame() {
        return chessGame;
    }

    public ChessGamePlayer getWhitePlayer() {
        return whitePlayer;
    }

    public ChessGamePlayer getBlackPlayer() {
        return blackPlayer;
    }

    public void setBoard(ChessBoard board) {
        this.board = board;
        this.chessGame = new ChessGame(board, whitePlayer, blackPlayer);
    }

    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    boolean doesAGameRuleApply(ChessGame game, Move move) {
        return this.chessGame.doesAGameRuleApply(game, move);
    }

    boolean doesAGameRuleApply(ChessGameFixture game, Move move) {
        return this.chessGame.doesAGameRuleApply(game.getChessGame(), move);
    }

    public Move getTheLastMove() {
        return chessGame.getTheLastMove();
    }

    public void initialise() {
        this.board.initialise();
    }
}
