package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;
import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.view.ChessBoardView;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

public class EnPassantTest {
    private ChessGame chessGame;
    private ChessBoard board;
    private EnPassantRule enPassantRule;
    private ChessBoardFactory factory;
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;

    @Before
    public void setUp() {
        whitePlayer = new ChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        blackPlayer = new ChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        board = new ChessBoard();
        enPassantRule = new EnPassantRule();
        factory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        chessGame = new ChessGame(board, whitePlayer, blackPlayer);
    }

    @Test
    public void testIsEnPassantCapture() {
        PawnPiece whitePawn = new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn = new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition = new Location(Coordinate.E, 5);
        Location blackPawnStartPosition = new Location(Coordinate.F, 7);
        Location blackPawnEndPosition = new Location(Coordinate.F, 5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);

        Move blackMove = new Move(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = new Move(whitePawnStartPosition, new Location(Coordinate.F, 6));

        chessGame.allGameMoves.add(blackMove);
        assertTrue(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void testIsNotEnPassantCapture() {
        PawnPiece whitePawn = new PawnPiece(Colour.WHITE);
        BishopPiece blackBishop = new BishopPiece(Colour.BLACK);
        Location whitePawnStartPosition = new Location(Coordinate.E, 5);
        Location blackPawnStartPosition = new Location(Coordinate.F, 7);
        Location blackPawnEndPosition = new Location(Coordinate.F, 5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackBishop, blackPawnEndPosition);

        Move blackMove = new Move(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = new Move(whitePawnStartPosition, new Location(Coordinate.F, 6));

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void testIsNotEnPassantCaptureMoveLessTwo() {
        PawnPiece whitePawn = new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn = new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition = new Location(Coordinate.E, 5);
        Location blackPawnStartPosition = new Location(Coordinate.F, 7);
        Location blackPawnEndPosition = new Location(Coordinate.F, 6);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);

        Move blackMove = new Move(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = new Move(whitePawnStartPosition, new Location(Coordinate.F, 6));

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void testIsNotEnPassantCaptureAfterTwoSquareMove() {
        PawnPiece whitePawn = new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn = new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition = new Location(Coordinate.E, 5);
        Location blackPawnStartPosition = new Location(Coordinate.F, 7);
        Location blackPawnEndPosition = new Location(Coordinate.F, 5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);

        Move blackMove = new Move(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = new Move(whitePawnStartPosition, new Location(Coordinate.D, 6));

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void testIsNotEnPassantCaptureAfterTwoSeparateSquareMove() {
        PawnPiece whitePawn = new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn = new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition = new Location(Coordinate.E, 5);
        Location blackPawnStartPosition = new Location(Coordinate.F, 6);
        Location blackPawnEndPosition = new Location(Coordinate.F, 5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);

        Move blackMove = new Move(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = new Move(whitePawnStartPosition, new Location(Coordinate.D, 6));

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void TestWhiteEnPassantCapture() throws IllegalMoveException {
        PawnPiece whitePawn = new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn = new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition = new Location(E, 5);
        Location whitePawnEndPosition = new Location(F, 6);
        Location blackPawnStartPosition = new Location(F, 7);
        Location blackPawnEndPosition = new Location(F, 5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);

        Move blackMove = new Move(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = new Move(whitePawnStartPosition, new Location(F, 6));

        chessGame.allGameMoves.add(blackMove);

        enPassantRule.applyRule(chessGame, whiteEnPassantMove);
        assertTrue(board.getPieceFromBoardAt(whitePawnEndPosition).equals(whitePawn));
        assertNull(board.getPieceFromBoardAt(blackPawnEndPosition));
    }

    @Test
    public void TestBlackEnPassantCapture() throws IllegalMoveException {
        PawnPiece whitePawn = new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn = new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition = new Location(F, 2);
        Location whitePawnEndPosition = new Location(F, 4);
        Location blackPawnStartPosition = new Location(G, 4);
        Location blackPawnEndPosition = new Location(F, 3);
        board.putPieceOnBoardAt(blackPawn, blackPawnStartPosition);
        board.putPieceOnBoardAt(whitePawn, whitePawnEndPosition);

        Move blackEnPassantMove = new Move(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteMove = new Move(whitePawnStartPosition, whitePawnEndPosition);

        chessGame.allGameMoves.add(whiteMove);

        enPassantRule.applyRule(chessGame, blackEnPassantMove);
        assertTrue(board.getPieceFromBoardAt(blackPawnEndPosition).equals(blackPawn));
        assertNull(board.getPieceFromBoardAt(whitePawnEndPosition));
    }

    @Test
    public void notMoveEnPassantCapture() {
        BishopPiece bishop = new BishopPiece(Colour.WHITE);
        Location startSquare = new Location(A, 2);
        Location endSquare = new Location(B, 3);
        Move move = new Move(startSquare, endSquare);

        board.putPieceOnBoardAt(bishop, startSquare);
        EnPassantRule rule = new EnPassantRule();

        assertFalse(rule.isEnPassantCapture(chessGame, move));

    }

    @Test
    public void enpassantCaptureNotPossibleAsKingWouldBeInCheck() throws ParseException,
                    IllegalMoveException {
        board = factory.getChessBoard("Ke8:Rd8:Rf8:Pd7:Pf7:Pe4:qe1:kd1:pd2");
        ChessGame game = new ChessGame(board, whitePlayer, blackPlayer);
        ChessBoardView view = new ChessBoardView(board);
        try {
            game.move(whitePlayer, new Move(new Location(D, 2), new Location(D, 4)));
            game.move(blackPlayer, new Move(new Location(E, 4), new Location(D, 3)));
        } catch (IllegalMoveException e) {
            view.displayTheBoard();
        }
        assertTrue(board.getPieceFromBoardAt(new Location(E, 4)) instanceof PawnPiece);
        assertTrue(board.getPieceFromBoardAt(new Location(D, 4)) instanceof PawnPiece);
    }

}
