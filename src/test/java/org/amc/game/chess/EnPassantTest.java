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
        chessGame.move(blackPlayer, createMove("f7", "f5"));
        chessGame.changePlayer();

        assertTrue(enPassantRule.isEnPassantCapture(chessGame, createMove("e5", "f6")));
    }

    @Test
    public void testIsNotEnPassantCapture() {
        final String whitePawnStartPosition = "E5";
        final String blackBishopStartPosition = "F7";
        final String blackBishopEndPosition = "F5";
        
        addPawnPieceToBoard(Colour.WHITE, whitePawnStartPosition);
        addChessPieceToBoard(BishopPiece.getBishopPiece(Colour.BLACK), blackBishopEndPosition);

        Move blackMove = createMove(blackBishopStartPosition, blackBishopEndPosition);
        Move whiteEnPassantMove = createMove(whitePawnStartPosition, "F6");

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    private void addPawnPieceToBoard(Colour colour, String location) {
        addChessPieceToBoard(PawnPiece.getPawnPiece(colour), location);
    }
    
    private void addChessPieceToBoard(ChessPiece piece, String location) {
        board.putPieceOnBoardAt(piece, new Location(location));
    }
    
    private Move createMove(String start, String end) {
        return new Move(start + Move.MOVE_SEPARATOR + end);
    }
    
    private ChessPiece getPieceOnBoard(String location) {
        return board.getPieceFromBoardAt(new Location(location));
    }
    
    @Test
    public void testIsNotEnPassantCaptureMoveLessTwo() {
        final String whitePawnStartPosition = "E5";
        final String blackPawnStartPosition = "F7";
        final String blackPawnEndPosition = "F6";
        addPawnPieceToBoard(Colour.WHITE, whitePawnStartPosition);
        addPawnPieceToBoard(Colour.BLACK, blackPawnEndPosition);

        Move blackMove = createMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = createMove(whitePawnStartPosition, "F6");

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void testIsNotEnPassantCaptureAfterTwoSquareMove() {
        final String whitePawnStartPosition = "E5";
        final String blackPawnStartPosition = "F7";
        final String blackPawnEndPosition = "F5";
        addPawnPieceToBoard(Colour.WHITE, whitePawnStartPosition);
        addPawnPieceToBoard(Colour.BLACK, blackPawnEndPosition);

        Move blackMove = createMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = createMove(whitePawnStartPosition, "D6");

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void testIsNotEnPassantCaptureAfterTwoSeparateSquareMove() {
        final String whitePawnStartPosition = "E5";
        final String blackPawnStartPosition = "F6";
        final String blackPawnEndPosition = "F5";
        addPawnPieceToBoard(Colour.WHITE, whitePawnStartPosition);
        addPawnPieceToBoard(Colour.BLACK, blackPawnEndPosition);

        Move blackMove = createMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = createMove(whitePawnStartPosition, "D6");

        chessGame.allGameMoves.add(blackMove);
        assertFalse(enPassantRule.isEnPassantCapture(chessGame, whiteEnPassantMove));
    }

    @Test
    public void TestWhiteEnPassantCapture() throws IllegalMoveException {
        final String whitePawnStartPosition = "E5";
        final String whitePawnEndPosition = "F6";
        final String blackPawnStartPosition = "F7";
        final String blackPawnEndPosition = "F5";
        
        final PawnPiece whitePawn = PawnPiece.getPawnPiece(Colour.WHITE);
        addChessPieceToBoard(whitePawn, whitePawnStartPosition);
        addPawnPieceToBoard(Colour.BLACK, blackPawnEndPosition);

        Move blackMove = createMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteEnPassantMove = createMove(whitePawnStartPosition, "F6");

        chessGame.allGameMoves.add(blackMove);

        enPassantRule.applyRule(chessGame, whiteEnPassantMove);
        assertTrue(getPieceOnBoard(whitePawnEndPosition)
                        .equals(whitePawn.moved()));
        assertNull(getPieceOnBoard(blackPawnEndPosition));
    }

    @Test
    public void TestBlackEnPassantCapture() throws IllegalMoveException {
        PawnPiece blackPawn = PawnPiece.getPawnPiece(Colour.BLACK);
        final String whitePawnStartPosition = "F2";
        final String whitePawnEndPosition = "F4";
        final String blackPawnStartPosition = "G4";
        final String blackPawnEndPosition = "F3";
        addChessPieceToBoard(blackPawn, blackPawnStartPosition);
        addPawnPieceToBoard(Colour.WHITE, whitePawnEndPosition);

        Move blackEnPassantMove = createMove(blackPawnStartPosition, blackPawnEndPosition);
        Move whiteMove = createMove(whitePawnStartPosition, whitePawnEndPosition);

        chessGame.allGameMoves.add(whiteMove);

        enPassantRule.applyRule(chessGame, blackEnPassantMove);
        assertTrue(getPieceOnBoard(blackPawnEndPosition).equals(blackPawn.moved()));
        assertNull(getPieceOnBoard(whitePawnEndPosition));
    }

    @Test
    public void notMoveEnPassantCapture() {
        BishopPiece bishop = BishopPiece.getBishopPiece(Colour.WHITE);
        final String startSquare = "A2";
        final String endSquare = "B3";
        Move move = createMove(startSquare, endSquare);

        addChessPieceToBoard(bishop, startSquare);

        assertFalse(enPassantRule.isEnPassantCapture(chessGame, move));

    }

    @Test
    public void enpassantCaptureNotPossibleAsKingWouldBeInCheck() throws ParseException,
                    IllegalMoveException {
        chessGame = new StandardChessGameFactory().getChessGame(
                        factory.getChessBoard("Ke8:Rd8:Rf8:Pd7:Pf7:Pe4:qe1:kd1:pd2"), 
                        whitePlayer, blackPlayer);
        board = chessGame.getChessBoard();
        ChessBoardView view = new ChessBoardView(board);
        try {
            chessGame.move(whitePlayer, createMove("D2", "D4"));
            chessGame.move(blackPlayer, createMove("E4", "D3"));
        } catch (IllegalMoveException e) {
            view.displayTheBoard();
        }
        assertEquals(getPieceOnBoard("E4").getClass(), PawnPiece.class);
        assertEquals(getPieceOnBoard("D4").getClass(), PawnPiece.class);
    }

}
