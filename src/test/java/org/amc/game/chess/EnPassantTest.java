package org.amc.game.chess;

import static org.amc.game.chess.NoChessPiece.NO_CHESSPIECE;
import static org.junit.Assert.*;

import org.amc.game.chess.view.ChessBoardView;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

public class EnPassantTest {
    private AbstractChessGame chessGame;
    private ChessBoard board;
    private EnPassantRule enPassantRule;
    private ChessBoardFactory factory;
    private ChessGameFactory chessGamefactory;
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    
    private ChessBoardUtil cbUtils;

    @Before
    public void setUp() {
        chessGamefactory = new StandardChessGameFactory();
        
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        
        enPassantRule = EnPassantRule.getInstance();
        factory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        
        chessGame = chessGamefactory.getChessGame(new ChessBoard(), whitePlayer, blackPlayer);
        board = chessGame.getChessBoard();
        cbUtils = new ChessBoardUtil(board);
        
    }

    @Test
    public void testIsEnPassantCapture() throws Exception {
        board = factory.getChessBoard("ke1:Ke8:pe5:Pf7");
        chessGame = chessGamefactory.getChessGame(board, whitePlayer, blackPlayer);
        chessGame.changePlayer();
        chessGame.move(blackPlayer, cbUtils.newMove("f7", "f5"));
        chessGame.changePlayer();

        assertTrue(enPassantRule.isEnPassantCapture(chessGame, cbUtils.newMove("e5", "f6")));
    }

    @Test
    public void testIsNotEnPassantCapture() {
        final String whitePawnStartPosition = "E5";
        final String blackBishopStartPosition = "F7";
        final String blackBishopEndPosition = "F5";
        
        cbUtils.addPawnPiece(Colour.WHITE, whitePawnStartPosition);
        cbUtils.add(BishopPiece.getPiece(Colour.BLACK), blackBishopEndPosition);

        Move blackMove = cbUtils.newMove(blackBishopStartPosition, blackBishopEndPosition);
        Move whiteEnPassantMove = cbUtils.newMove(whitePawnStartPosition, "F6");

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }
    
    @Test
    public void testIsNotEnPassantCaptureMoveLessTwo() {
        final String whitePawnStartPosition = "E5";
        final String blackPawnStartPosition = "F7";
        final String blackPawnEndPosition = "F6";
        cbUtils.addPawnPiece(Colour.WHITE, whitePawnStartPosition);
        cbUtils.addPawnPiece(Colour.BLACK, blackPawnEndPosition);

        Move blackMove = cbUtils.newMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = cbUtils.newMove(whitePawnStartPosition, "F6");

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void testIsNotEnPassantCaptureAfterTwoSquareMove() {
        final String whitePawnStartPosition = "E5";
        final String blackPawnStartPosition = "F7";
        final String blackPawnEndPosition = "F5";
        cbUtils.addPawnPiece(Colour.WHITE, whitePawnStartPosition);
        cbUtils.addPawnPiece(Colour.BLACK, blackPawnEndPosition);

        Move blackMove = cbUtils.newMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = cbUtils.newMove(whitePawnStartPosition, "D6");

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void testIsNotEnPassantCaptureAfterTwoSeparateSquareMove() {
        final String whitePawnStartPosition = "E5";
        final String blackPawnStartPosition = "F6";
        final String blackPawnEndPosition = "F5";
        cbUtils.addPawnPiece(Colour.WHITE, whitePawnStartPosition);
        cbUtils.addPawnPiece(Colour.BLACK, blackPawnEndPosition);

        Move blackMove = cbUtils.newMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = cbUtils.newMove(whitePawnStartPosition, "D6");

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void testWhiteEnPassantCapture() throws IllegalMoveException {
        final String whitePawnStartPosition = "E5";
        final String whitePawnEndPosition = "F6";
        final String blackPawnStartPosition = "F7";
        final String blackPawnEndPosition = "F5";
        
        final PawnPiece whitePawn = PawnPiece.getPiece(Colour.WHITE);
        cbUtils.add(whitePawn, whitePawnStartPosition);
        cbUtils.addPawnPiece(Colour.BLACK, blackPawnEndPosition);

        Move blackMove = cbUtils.newMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = cbUtils.newMove(whitePawnStartPosition, "F6");

        chessGame.allGameMoves.add(blackMove);

        enPassantRule.applyRule(chessGame, whiteEnPassantMove);
        assertTrue(cbUtils.getPiece(whitePawnEndPosition)
                        .equals(whitePawn.moved()));
        assertEquals(NO_CHESSPIECE, cbUtils.getPiece(blackPawnEndPosition));
    }

    @Test
    public void testBlackEnPassantCapture() throws IllegalMoveException {
        PawnPiece blackPawn = PawnPiece.getPiece(Colour.BLACK);
        final String whitePawnStartPosition = "F2";
        final String whitePawnEndPosition = "F4";
        final String blackPawnStartPosition = "G4";
        final String blackPawnEndPosition = "F3";
        cbUtils.add(blackPawn, blackPawnStartPosition);
        cbUtils.addPawnPiece(Colour.WHITE, whitePawnEndPosition);

        Move blackEnPassantMove = cbUtils.newMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteMove = cbUtils.newMove(whitePawnStartPosition, whitePawnEndPosition);

        chessGame.allGameMoves.add(whiteMove);

        enPassantRule.applyRule(chessGame, blackEnPassantMove);
        assertTrue(cbUtils.getPiece(blackPawnEndPosition).equals(blackPawn.moved()));
        assertEquals(NO_CHESSPIECE, cbUtils.getPiece(whitePawnEndPosition));
    }

    @Test
    public void notMoveEnPassantCapture() {
        BishopPiece bishop = BishopPiece.getPiece(Colour.WHITE);
        final String startSquare = "A2";
        final String endSquare = "B3";
        Move move = cbUtils.newMove(startSquare, endSquare);

        cbUtils.add(bishop, startSquare);

        assertFalse(enPassantRule.isEnPassantCapture(chessGame, move));

    }

    @Test
    public void enpassantCaptureNotPossibleAsKingWouldBeInCheck() throws ParseException,
                    IllegalMoveException {
        chessGame = new StandardChessGameFactory().getChessGame(
                        factory.getChessBoard("Ke8:Rd8:Rf8:Pd7:Pf7:Pe4:qe1:kd1:pd2"), 
                        whitePlayer, blackPlayer);
        board = chessGame.getChessBoard();
        cbUtils = new ChessBoardUtil(board);
        ChessBoardView view = new ChessBoardView(board);
        try {
            chessGame.move(whitePlayer, cbUtils.newMove("D2", "D4"));
            chessGame.move(blackPlayer, cbUtils.newMove("E4", "D3"));
        } catch (IllegalMoveException e) {
            view.displayTheBoard();
        }
        assertEquals(cbUtils.getPiece("E4").getClass(), PawnPiece.class);
        assertEquals(cbUtils.getPiece("D4").getClass(), PawnPiece.class);
    }

}
