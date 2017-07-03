package org.amc.game.chess;

import static org.amc.game.chess.NoChessPiece.NO_CHESSPIECE;
import static org.junit.Assert.*;

import org.amc.game.chess.view.ChessBoardView;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

public class EnPassantTest {
    private ChessGame chessGame;
    private ChessBoard board;
    private EnPassantRule enPassantRule;
    private ChessBoardFactory factory;
    private ChessGameFactory chessGamefactory;
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    
    private ChessBoardUtilities cbUtils;

    @Before
    public void setUp() {
        chessGamefactory = new StandardChessGameFactory();
        
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        
        enPassantRule = EnPassantRule.getInstance();
        factory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        
        chessGame = chessGamefactory.getChessGame(new ChessBoard(), whitePlayer, blackPlayer);
        board = chessGame.getChessBoard();
        cbUtils = new ChessBoardUtilities(board);
        
    }

    @Test
    public void testIsEnPassantCapture() throws Exception {
        board = factory.getChessBoard("ke1:Ke8:pe5:Pf7");
        chessGame = chessGamefactory.getChessGame(board, whitePlayer, blackPlayer);
        chessGame.changePlayer();
        chessGame.move(blackPlayer, cbUtils.createMove("f7", "f5"));
        chessGame.changePlayer();

        assertTrue(enPassantRule.isEnPassantCapture(chessGame, cbUtils.createMove("e5", "f6")));
    }

    @Test
    public void testIsNotEnPassantCapture() {
        final String whitePawnStartPosition = "E5";
        final String blackBishopStartPosition = "F7";
        final String blackBishopEndPosition = "F5";
        
        cbUtils.addPawnPieceToBoard(Colour.WHITE, whitePawnStartPosition);
        cbUtils.addChessPieceToBoard(BishopPiece.getPiece(Colour.BLACK), blackBishopEndPosition);

        Move blackMove = cbUtils.createMove(blackBishopStartPosition, blackBishopEndPosition);
        Move whiteEnPassantMove = cbUtils.createMove(whitePawnStartPosition, "F6");

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }
    
    @Test
    public void testIsNotEnPassantCaptureMoveLessTwo() {
        final String whitePawnStartPosition = "E5";
        final String blackPawnStartPosition = "F7";
        final String blackPawnEndPosition = "F6";
        cbUtils.addPawnPieceToBoard(Colour.WHITE, whitePawnStartPosition);
        cbUtils.addPawnPieceToBoard(Colour.BLACK, blackPawnEndPosition);

        Move blackMove = cbUtils.createMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = cbUtils.createMove(whitePawnStartPosition, "F6");

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void testIsNotEnPassantCaptureAfterTwoSquareMove() {
        final String whitePawnStartPosition = "E5";
        final String blackPawnStartPosition = "F7";
        final String blackPawnEndPosition = "F5";
        cbUtils.addPawnPieceToBoard(Colour.WHITE, whitePawnStartPosition);
        cbUtils.addPawnPieceToBoard(Colour.BLACK, blackPawnEndPosition);

        Move blackMove = cbUtils.createMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = cbUtils.createMove(whitePawnStartPosition, "D6");

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void testIsNotEnPassantCaptureAfterTwoSeparateSquareMove() {
        final String whitePawnStartPosition = "E5";
        final String blackPawnStartPosition = "F6";
        final String blackPawnEndPosition = "F5";
        cbUtils.addPawnPieceToBoard(Colour.WHITE, whitePawnStartPosition);
        cbUtils.addPawnPieceToBoard(Colour.BLACK, blackPawnEndPosition);

        Move blackMove = cbUtils.createMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = cbUtils.createMove(whitePawnStartPosition, "D6");

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
        cbUtils.addChessPieceToBoard(whitePawn, whitePawnStartPosition);
        cbUtils.addPawnPieceToBoard(Colour.BLACK, blackPawnEndPosition);

        Move blackMove = cbUtils.createMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = cbUtils.createMove(whitePawnStartPosition, "F6");

        chessGame.allGameMoves.add(blackMove);

        enPassantRule.applyRule(chessGame, whiteEnPassantMove);
        assertTrue(cbUtils.getPieceOnBoard(whitePawnEndPosition)
                        .equals(whitePawn.moved()));
        assertEquals(NO_CHESSPIECE, cbUtils.getPieceOnBoard(blackPawnEndPosition));
    }

    @Test
    public void testBlackEnPassantCapture() throws IllegalMoveException {
        PawnPiece blackPawn = PawnPiece.getPiece(Colour.BLACK);
        final String whitePawnStartPosition = "F2";
        final String whitePawnEndPosition = "F4";
        final String blackPawnStartPosition = "G4";
        final String blackPawnEndPosition = "F3";
        cbUtils.addChessPieceToBoard(blackPawn, blackPawnStartPosition);
        cbUtils.addPawnPieceToBoard(Colour.WHITE, whitePawnEndPosition);

        Move blackEnPassantMove = cbUtils.createMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteMove = cbUtils.createMove(whitePawnStartPosition, whitePawnEndPosition);

        chessGame.allGameMoves.add(whiteMove);

        enPassantRule.applyRule(chessGame, blackEnPassantMove);
        assertTrue(cbUtils.getPieceOnBoard(blackPawnEndPosition).equals(blackPawn.moved()));
        assertEquals(NO_CHESSPIECE, cbUtils.getPieceOnBoard(whitePawnEndPosition));
    }

    @Test
    public void notMoveEnPassantCapture() {
        BishopPiece bishop = BishopPiece.getPiece(Colour.WHITE);
        final String startSquare = "A2";
        final String endSquare = "B3";
        Move move = cbUtils.createMove(startSquare, endSquare);

        cbUtils.addChessPieceToBoard(bishop, startSquare);

        assertFalse(enPassantRule.isEnPassantCapture(chessGame, move));

    }

    @Test
    public void enpassantCaptureNotPossibleAsKingWouldBeInCheck() throws ParseException,
                    IllegalMoveException {
        chessGame = new StandardChessGameFactory().getChessGame(
                        factory.getChessBoard("Ke8:Rd8:Rf8:Pd7:Pf7:Pe4:qe1:kd1:pd2"), 
                        whitePlayer, blackPlayer);
        board = chessGame.getChessBoard();
        cbUtils = new ChessBoardUtilities(board);
        ChessBoardView view = new ChessBoardView(board);
        try {
            chessGame.move(whitePlayer, cbUtils.createMove("D2", "D4"));
            chessGame.move(blackPlayer, cbUtils.createMove("E4", "D3"));
        } catch (IllegalMoveException e) {
            view.displayTheBoard();
        }
        assertEquals(cbUtils.getPieceOnBoard("E4").getClass(), PawnPiece.class);
        assertEquals(cbUtils.getPieceOnBoard("D4").getClass(), PawnPiece.class);
    }

}
