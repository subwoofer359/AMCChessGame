package org.amc.game.chess;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@Ignore
public class ChessGameMoveTest {
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ChessPiece chessPiece;
    private ChessMoveRule mockRule;
    private ChessBoard board;
    private ChessGame chessGame;
    private Move move;

    @Before
    public void setUp() throws Exception {
        whitePlayer = new ChessGamePlayer(new HumanPlayer("Teddy"), Colour.WHITE);
        blackPlayer = new ChessGamePlayer(new HumanPlayer("Robin"), Colour.BLACK);

        move = mock(Move.class);
        board = mock(ChessBoard.class);

        chessPiece = mock(ChessPiece.class);
        when(chessPiece.getColour()).thenReturn(Colour.WHITE);

        when(board.getPieceFromBoardAt(any(Location.class))).thenReturn(chessPiece);
        when(board.getTheLastMove()).thenReturn(move);

        chessGame = new ChessGame(board, whitePlayer, blackPlayer);

        mockRule = mock(ChessMoveRule.class);
        List<ChessMoveRule> rules = new ArrayList<>();
        rules.add(mockRule);
        chessGame.setGameRules(rules);
        chessGame.setChessBoard(board);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMoveNotInCheckNoSpecialMove() throws IllegalMoveException {
        ChessGame spyChessGame = spy(chessGame);

        when(chessPiece.isValidMove(board, move)).thenReturn(true);
        doReturn(false).when(spyChessGame).isPlayersKingInCheck(whitePlayer, board);
        doReturn(false).when(spyChessGame).doesAGameRuleApply(any(ChessBoard.class),
                        any(Move.class));

        spyChessGame.move(whitePlayer, move);

        verify(spyChessGame, times(2)).isPlayersKingInCheck(whitePlayer, board);
        verify(spyChessGame, times(1)).doesAGameRuleApply(board, move);
        verify(chessPiece, times(1)).isValidMove(board, move);

    }

    @Test
    public void testMoveAfterCheckNoSpecialMove() throws IllegalMoveException {
        ChessGame spyChessGame = spy(chessGame);

        when(chessPiece.isValidMove(board, move)).thenReturn(true);
        doReturn(true).doReturn(false).when(spyChessGame).isPlayersKingInCheck(whitePlayer, board);
        doReturn(false).when(spyChessGame).doesAGameRuleApply(any(ChessBoard.class),
                        any(Move.class));

        spyChessGame.move(whitePlayer, move);

        verify(spyChessGame, times(2)).isPlayersKingInCheck(whitePlayer, board);
        verify(spyChessGame, times(0)).doesAGameRuleApply(board, move);
        verify(chessPiece, times(1)).isValidMove(board, move);
        verify(board, times(1)).move(move);
    }

    @Test(expected = IllegalMoveException.class)
    public void testMoveIntoCheckNoSpecialMove() throws IllegalMoveException {
        ChessGame spyChessGame = spy(chessGame);

        when(chessPiece.isValidMove(board, move)).thenReturn(true);
        doReturn(false).doReturn(true).when(spyChessGame).isPlayersKingInCheck(whitePlayer, board);
        doReturn(false).when(spyChessGame).doesAGameRuleApply(any(ChessBoard.class),
                        any(Move.class));

        spyChessGame.move(whitePlayer, move);

        verify(spyChessGame, times(2)).isPlayersKingInCheck(whitePlayer, board);
        verify(spyChessGame, times(1)).doesAGameRuleApply(board, move);
        verify(chessPiece, times(1)).isValidMove(board, move);
        verify(board, times(0)).move(move);
    }

    @Test
    public void testMoveIntoCheckFromCheckNoSpecialMove() throws IllegalMoveException {
        ChessGame spyChessGame = spy(chessGame);

        when(chessPiece.isValidMove(board, move)).thenReturn(true);
        doReturn(true).doReturn(true).when(spyChessGame).isPlayersKingInCheck(whitePlayer, board);
        doReturn(false).when(spyChessGame).doesAGameRuleApply(any(ChessBoard.class),
                        any(Move.class));

        try {
            spyChessGame.move(whitePlayer, move);
        } catch (IllegalMoveException ie) {
            assertTrue(ie instanceof IllegalMoveException);
        }

        verify(spyChessGame, times(2)).isPlayersKingInCheck(whitePlayer, board);
        verify(spyChessGame, times(0)).doesAGameRuleApply(board, move);
        verify(chessPiece, times(1)).isValidMove(board, move);
        verify(board, times(1)).move(move);
    }

    @Test(expected = IllegalMoveException.class)
    public void testInValidMoveIntoCheckFromCheckNoSpecialMove() throws IllegalMoveException {
        ChessGame spyChessGame = spy(chessGame);

        when(chessPiece.isValidMove(board, move)).thenReturn(false);
        doReturn(true).doReturn(true).when(spyChessGame).isPlayersKingInCheck(whitePlayer, board);
        doReturn(false).when(spyChessGame).doesAGameRuleApply(any(ChessBoard.class),
                        any(Move.class));

        spyChessGame.move(whitePlayer, move);
    }

    @Test
    public void testMoveNotCheckSpecialMove() throws IllegalMoveException {
        ChessGame spyChessGame = spy(chessGame);

        when(chessPiece.isValidMove(board, move)).thenReturn(true);
        doReturn(false).doReturn(false).when(spyChessGame).isPlayersKingInCheck(whitePlayer, board);
        doReturn(true).when(spyChessGame)
                        .doesAGameRuleApply(any(ChessBoard.class), any(Move.class));

        spyChessGame.move(whitePlayer, move);

        verify(spyChessGame, times(2)).isPlayersKingInCheck(whitePlayer, board);
        verify(spyChessGame, times(1)).doesAGameRuleApply(board, move);
        verify(chessPiece, times(0)).isValidMove(board, move);
        verify(board, times(0)).move(move);
    }
}