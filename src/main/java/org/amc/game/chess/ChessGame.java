package org.amc.game.chess;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Contains the Rules of Chess
 * 
 * @author Adrian Mclaughlin
 *
 */
@Entity
@Table(name = "chessGames")
public class ChessGame extends AbstractChessGame {
	
    private static final long serialVersionUID = 5323277982974698086L;

    @Transient
    private final KingInCheck kingInCheck = KingInCheck.getInstance();
    
    @Transient
    private final InStalemate inStalemate = InStalemate.getInstance();

    protected ChessGame() {
        super();
    }

    public ChessGame(ChessBoard board, ChessGamePlayer playerWhite, ChessGamePlayer playerBlack) {
        super(board, playerWhite, playerBlack);
    }

    public ChessGame(AbstractChessGame chessGame) {
        super(chessGame);
    }

    @Override
    public void move(ChessGamePlayer player, Move move) throws IllegalMoveException {
        isPlayersTurn(player);
        isNeedToDoAPromotion();
        ChessPiece piece = getChessBoard().get(move.getStart());
        checkItsthePlayersPiece(player, piece);
        moveThePlayersChessPiece(player, getChessBoard(), piece, move);
        
        if (isOpponentsKingInCheck(player, getChessBoard())) {
            isOpponentKingInCheckMate(player);
        } else if (inStalemate.isStalemate(getOpposingPlayer(player), player,
                getChessBoard())) {
                setGameState(GameState.STALEMATE);
        }
    }

    private void isPlayersTurn(Player player) throws IllegalMoveException {
        if (!ComparePlayers.isSamePlayer(getCurrentPlayer(), player)) {
            throw new IllegalMoveException("Not Player's turn");
        }
    }

    private void isNeedToDoAPromotion() throws IllegalMoveException {
        if (getGameState() == GameState.PAWN_PROMOTION) {
            throw new IllegalMoveException("Pawn Promotion need to be completed");
        }
    }

    private void checkItsthePlayersPiece(ChessGamePlayer player, ChessPiece piece)
                    throws IllegalMoveException {
        if (player.getColour() != piece.getColour()) {
            throw new IllegalMoveException("Player can only move their own pieces");
        }
    }

    private void moveThePlayersChessPiece(ChessGamePlayer player, ChessBoard board,
                    ChessPiece piece, Move move) throws IllegalMoveException {
        if (isPlayersKingInCheck(player, board)) {
            doNormalMove(player, piece, move);
        } else if (doesAGameRuleApply(this, move)) {
            thenApplyGameRule(player, move);
        } else {
            doNormalMove(player, piece, move);
        }
    }

    boolean isPlayersKingInCheck(ChessGamePlayer player, ChessBoard board) {
        return kingInCheck.isPlayersKingInCheck(player, getOpposingPlayer(player), board);

    }

    boolean isOpponentsKingInCheck(ChessGamePlayer player, ChessBoard board) {
        ChessGamePlayer opponent = getOpposingPlayer(player);
        boolean inCheck = kingInCheck.isPlayersKingInCheck(opponent, player, board);
        if (inCheck) {
            setGameState(Colour.WHITE == opponent.getColour() ? GameState.WHITE_IN_CHECK
                            : GameState.BLACK_IN_CHECK);
        }
        return inCheck;
    }

    boolean isOpponentKingInCheckMate(ChessGamePlayer player) {
        ChessGamePlayer opponent = getOpposingPlayer(player);
        KingInCheckmate okcc = new KingInCheckmate(opponent, player,
                        getChessBoard());
        if (okcc.isCheckMate()) {
            setGameState(Colour.WHITE == opponent.getColour() ? GameState.WHITE_CHECKMATE
                            : GameState.BLACK_CHECKMATE);
            return true;
        } else {
            return false;
        }
    }

    private void doNormalMove(ChessGamePlayer player, ChessPiece piece, Move move)
                    throws IllegalMoveException {
        if (piece.isValidMove(getChessBoard(), move)) {
            thenMoveChessPiece(player, move);
        } else {
            throw new IllegalMoveException("Not a valid move");
        }
    }

    private void thenMoveChessPiece(ChessGamePlayer player, Move move) throws IllegalMoveException {
        ChessBoard testBoard = new ChessBoard(getChessBoard());
        testBoard.move(move);
        if (isPlayersKingInCheck(player, testBoard)) {
            throw new IllegalMoveException("King is checked");
        } else {
            setGameState(GameState.RUNNING);
            getChessBoard().move(move);
            this.allGameMoves.add(move);
        }
    }

    private void thenApplyGameRule(ChessGamePlayer player, Move move) throws IllegalMoveException {
        for (ChessMoveRule rule : getChessMoveRules()) {
            ChessGame testGame = new ChessGame(this);
            rule.applyRule(testGame, move);
            if (isPlayersKingInCheck(player, testGame.getChessBoard())) {
                throw new IllegalMoveException("King is checked");
            } else {
                rule.applyRule(this, move);
            }
        }
    }
}
