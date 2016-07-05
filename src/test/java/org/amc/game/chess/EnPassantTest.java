package org.amc.game.chess;

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
    

    @Before
    public void setUp() {
        chessGamefactory = new StandardChessGameFactory();
        
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        
        enPassantRule = EnPassantRule.getInstance();
        factory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        
        chessGame = chessGamefactory.getChessGame(new ChessBoard(), whitePlayer, blackPlayer);
        board = chessGame.getChessBoard();
        
    }

    @Test
    public void testIsEnPassantCapture() throws Exception {
        board = factory.getChessBoard("ke1:Ke8:pe5:Pf7");
        chessGame = chessGamefactory.getChessGame(board, whitePlayer, blackPlayer);
        chessGame.changePlayer();
        chessGame.move(blackPlayer, new Move("f7-f5"));
        chessGame.changePlayer();

        assertTrue(enPassantRule.isEnPassantCapture(chessGame, new Move("e5-f6")));
    }

    @Test
    public void testIsNotEnPassantCapture() {
        PawnPiece whitePawn = PawnPiece.getPawnPiece(Colour.WHITE);
        BishopPiece blackBishop = BishopPiece.getBishopPiece(Colour.BLACK);
        Location whitePawnStartPosition = new Location("E5");
        Location blackPawnStartPosition = new Location("F7");
        Location blackPawnEndPosition = new Location("F5");
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackBishop, blackPawnEndPosition);

        Move blackMove = new Move(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = new Move(whitePawnStartPosition, new Location("F6"));

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void testIsNotEnPassantCaptureMoveLessTwo() {
        PawnPiece whitePawn = PawnPiece.getPawnPiece(Colour.WHITE);
        PawnPiece blackPawn = PawnPiece.getPawnPiece(Colour.BLACK);
        Location whitePawnStartPosition = new Location("E5");
        Location blackPawnStartPosition = new Location("F7");
        Location blackPawnEndPosition = new Location("F6");
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);

        Move blackMove = new Move(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = new Move(whitePawnStartPosition, new Location("F6"));

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void testIsNotEnPassantCaptureAfterTwoSquareMove() {
        PawnPiece whitePawn = PawnPiece.getPawnPiece(Colour.WHITE);
        PawnPiece blackPawn = PawnPiece.getPawnPiece(Colour.BLACK);
        Location whitePawnStartPosition = new Location("E5");
        Location blackPawnStartPosition = new Location("F7");
        Location blackPawnEndPosition = new Location("F5");
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);

        Move blackMove = new Move(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = new Move(whitePawnStartPosition, new Location("D6"));

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void testIsNotEnPassantCaptureAfterTwoSeparateSquareMove() {
        PawnPiece whitePawn = PawnPiece.getPawnPiece(Colour.WHITE);
        PawnPiece blackPawn = PawnPiece.getPawnPiece(Colour.BLACK);
        Location whitePawnStartPosition = new Location("E5");
        Location blackPawnStartPosition = new Location("F6");
        Location blackPawnEndPosition = new Location("F5");
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);

        Move blackMove = new Move(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = new Move(whitePawnStartPosition, new Location("D6"));

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void TestWhiteEnPassantCapture() throws IllegalMoveException {
        PawnPiece whitePawn = PawnPiece.getPawnPiece(Colour.WHITE);
        PawnPiece blackPawn = PawnPiece.getPawnPiece(Colour.BLACK);
        Location whitePawnStartPosition = new Location("E5");
        Location whitePawnEndPosition = new Location("F6");
        Location blackPawnStartPosition = new Location("F7");
        Location blackPawnEndPosition = new Location("F5");
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);

        Move blackMove = new Move(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = new Move(whitePawnStartPosition, new Location("F6"));

        chessGame.allGameMoves.add(blackMove);

        enPassantRule.applyRule(chessGame, whiteEnPassantMove);
        assertTrue(board.getPieceFromBoardAt(whitePawnEndPosition).equals(whitePawn.moved()));
        assertNull(board.getPieceFromBoardAt(blackPawnEndPosition));
    }

    @Test
    public void TestBlackEnPassantCapture() throws IllegalMoveException {
        PawnPiece whitePawn = PawnPiece.getPawnPiece(Colour.WHITE);
        PawnPiece blackPawn = PawnPiece.getPawnPiece(Colour.BLACK);
        Location whitePawnStartPosition = new Location("F2");
        Location whitePawnEndPosition = new Location("F4");
        Location blackPawnStartPosition = new Location("G4");
        Location blackPawnEndPosition = new Location("F3");
        board.putPieceOnBoardAt(blackPawn, blackPawnStartPosition);
        board.putPieceOnBoardAt(whitePawn, whitePawnEndPosition);

        Move blackEnPassantMove = new Move(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteMove = new Move(whitePawnStartPosition, whitePawnEndPosition);

        chessGame.allGameMoves.add(whiteMove);

        enPassantRule.applyRule(chessGame, blackEnPassantMove);
        assertTrue(board.getPieceFromBoardAt(blackPawnEndPosition).equals(blackPawn.moved()));
        assertNull(board.getPieceFromBoardAt(whitePawnEndPosition));
    }

    @Test
    public void notMoveEnPassantCapture() {
        BishopPiece bishop = BishopPiece.getBishopPiece(Colour.WHITE);
        final String startSquare = "A2";
        final String endSquare = "B3";
        Move move = new Move(startSquare + Move.LOCATION_SEPARATOR + endSquare);

        board.putPieceOnBoardAt(bishop, new Location(startSquare));

        assertFalse(enPassantRule.isEnPassantCapture(chessGame, move));

    }

    @Test
    public void enpassantCaptureNotPossibleAsKingWouldBeInCheck() throws ParseException,
                    IllegalMoveException {
        ChessGame game = new StandardChessGameFactory().getChessGame(
                        factory.getChessBoard("Ke8:Rd8:Rf8:Pd7:Pf7:Pe4:qe1:kd1:pd2"), 
                        whitePlayer, blackPlayer);
        ChessBoard board = game.getChessBoard();
        ChessBoardView view = new ChessBoardView(board);
        try {
            game.move(whitePlayer, new Move("D2-D4"));
            game.move(blackPlayer, new Move("E4-D3"));
        } catch (IllegalMoveException e) {
            view.displayTheBoard();
        }
        assertTrue(board.getPieceFromBoardAt(new Location("E4")) instanceof PawnPiece);
        assertTrue(board.getPieceFromBoardAt(new Location("D4")) instanceof PawnPiece);
    }

}
