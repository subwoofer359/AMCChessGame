package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chess.view.ChessBoardView;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.List;

import static org.amc.game.chess.ChessBoard.Coordinate.*;

public class IsKingCheckmatedTest {
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ChessBoard board;
    private ChessGame chessGame;
    private ChessBoardFactory chessBoardFactory;

    @Before
    public void setUp() throws Exception {
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("Teddy"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Robin"), Colour.BLACK);
        board = new ChessBoard();
        chessGame = new ChessGame(board, whitePlayer, blackPlayer);
        chessBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
    }

    @Test
    public void checkmateWithARookTest() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kh5:kf5:ra1");
        Move rookMove = new Move(new Location(A, 1), new Location(H, 1));
        board.move(rookMove);
        assertCheckMate(blackPlayer, whitePlayer, board);
    }

    private void assertCheckMate(ChessGamePlayer playerOne, ChessGamePlayer playerTwo, ChessBoard board) {
        PlayersKingCheckmateCondition pkcc = new PlayersKingCheckmateCondition(
                        playerOne, playerTwo, board);
        assertTrue(pkcc.isCheckMate());
    }
    
    private void assertNotCheckMate(ChessGamePlayer playerOne, ChessGamePlayer playerTwo, ChessBoard board) {
        PlayersKingCheckmateCondition pkcc = new PlayersKingCheckmateCondition(
                        playerOne, playerTwo, board);
        assertFalse(pkcc.isCheckMate());
    }
    
    @Test
    public void getAllPiecesAttackingTheKingTest() throws ParseException {
        board = chessBoardFactory.getChessBoard("Ke8:bc6:qe1:nf6");
        PlayersKingCheckmateCondition pkicc = new PlayersKingCheckmateCondition(blackPlayer,
                        whitePlayer, board);
        List<ChessPieceLocation> attackers = pkicc.getAllPiecesAttackingTheKing();
        assertTrue(attackers.size() == 3);

        board = chessBoardFactory.getChessBoard("Ke8:bc6:qe1:nf6:nd6");
        pkicc = new PlayersKingCheckmateCondition(blackPlayer, whitePlayer, board);
        attackers = pkicc.getAllPiecesAttackingTheKing();
        assertTrue(attackers.size() == 4);

        // pawn blocking queen
        board = chessBoardFactory.getChessBoard("Ke8:bc6:qe1:pe2:nf6:nd6");
        pkicc = new PlayersKingCheckmateCondition(blackPlayer, whitePlayer, board);
        attackers = pkicc.getAllPiecesAttackingTheKing();
        assertTrue(attackers.size() == 3);
    }

    @Test
    public void checkmateFoolsMate() {
        board.initialise();
        board.remove(new Location(D, 8));
        board.remove(new Location(E, 7));
        board.remove(new Location(F, 2));
        board.remove(new Location(G, 2));
        board.put(PawnPiece.getPawnPiece(Colour.BLACK), new Location(E, 5));
        board.put(QueenPiece.getQueenPiece(Colour.BLACK), new Location(D, 8));
        board.put(PawnPiece.getPawnPiece(Colour.WHITE), new Location(G, 4));
        board.put(PawnPiece.getPawnPiece(Colour.WHITE), new Location(F, 3));
        Move queenMove = new Move(new Location(D, 8), new Location(H, 4));
        board.move(queenMove);
        PlayersKingCheckmateCondition pkcc = new PlayersKingCheckmateCondition(
                        whitePlayer, blackPlayer, board);
        
        assertTrue(pkcc.isCheckMate());
    }

    @Test
    public void checkmateSupportMate() throws ParseException {
        board = chessBoardFactory.getChessBoard("Ka6:kc6:qb1");
        Move queenMove = new Move(new Location(B, 1), new Location(B, 6));
        board.move(queenMove);
        assertCheckMate(blackPlayer, whitePlayer, board);
    }

    @Test
    public void notCheckmateSupportMate() throws ParseException {
        board = chessBoardFactory.getChessBoard("Ka6:kc6:Bc7:qb1");
        Move move = new Move(new Location(B, 1), new Location(B, 6));
        board.move(move);
        assertNotCheckMate(blackPlayer, whitePlayer, board);

    }

    @Test
    public void checkmateRightTriangleMate() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kh6:kf6:qa1");
        Move queenMove = new Move(new Location(A, 1), new Location(H, 1));
        board.move(queenMove);

        assertCheckMate(blackPlayer, whitePlayer, board);
    }

    @Test
    public void checkmateOuterRowMate() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kh3:kf2:qa5");
        Move queenMove = new Move(new Location(A, 5), new Location(H, 5));
        board.move(queenMove);
        
        assertCheckMate(blackPlayer, whitePlayer, board);
    }

    @Test
    public void notCheckmateOuterRowMate() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kh3:kf2:Ra4:qh4");
        board.move(new Move(new Location(H, 4), new Location(H, 5)));
        assertNotCheckMate(blackPlayer, whitePlayer, board);
    }

    @Test
    public void checkmateWithTwoBishops() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kb8:kb6:bb7:be7");
        Move move = new Move(new Location(E, 7), new Location(D, 6));
        board.move(move);

        assertCheckMate(blackPlayer, whitePlayer, board);
    }

    @Test
    public void checkmateWithTwoBishopsCanBeBlocked() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kb8:kb6:bb7:be7:Rh7");
        Move move = new Move(new Location(E, 7), new Location(D, 6));
        board.move(move);

        assertNotCheckMate(blackPlayer, whitePlayer, board);
        PlayersKingCheckmateCondition pkicc = new PlayersKingCheckmateCondition(blackPlayer,
                        whitePlayer, board);
        assertFalse(pkicc.canAttackingPieceNotBeBlocked());
    }

    @Test
    public void checkmateWithTwoBishopsCanBeCaptured() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kb8:kb6:bb7:be7:Rh6");
        Move move = new Move(new Location(E, 7), new Location(D, 6));
        board.move(move);
        PlayersKingCheckmateCondition pkicc = new PlayersKingCheckmateCondition(blackPlayer,
                        whitePlayer, board);
        assertFalse(pkicc.canAttackingPieceNotBeCaptured());
        assertNotCheckMate(blackPlayer, whitePlayer, board);
    }

    @Test
    public void notCheckMate() {
        board.initialise();
        assertNotCheckMate(whitePlayer, blackPlayer, board);
        assertNotCheckMate(blackPlayer, whitePlayer, board);
    }

    @Test
    public void testCannotAttackingPieceBeBlocked() throws ParseException {
        board = chessBoardFactory.getChessBoard("qh8:Kh3:kf2");
        Move move = new Move(new Location(H, 8), new Location(H, 5));
        board.move(move);
        PlayersKingCheckmateCondition pkicc = new PlayersKingCheckmateCondition(blackPlayer,
                        whitePlayer, board);
        assertTrue(pkicc.canAttackingPieceNotBeBlocked());
    }

    @Test
    public void testAttackingPieceCanNotBeBlockedDueToCheck() throws ParseException {
        board = chessBoardFactory.getChessBoard("Ke8:ke1:qe4:bh5:Rg6");
        PlayersKingCheckmateCondition pkicc = new PlayersKingCheckmateCondition(blackPlayer,
                        whitePlayer, board);
        assertTrue(pkicc.canAttackingPieceNotBeBlocked());
    }

    @Test
    public void testCanAttackingPieceBeBlocked() throws ParseException {
        board = chessBoardFactory.getChessBoard("qh8:Kh3:Ra4:kf2");
        Move move = new Move(new Location(H, 8), new Location(H, 5));
        board.move(move);
        ChessBoardView view = new ChessBoardView(board);
        view.displayTheBoard();
        PlayersKingCheckmateCondition pkicc = new PlayersKingCheckmateCondition(blackPlayer,
                        whitePlayer, board);
        assertFalse(pkicc.canAttackingPieceNotBeBlocked());
    }

    @Test
    public void testTwoAttackingPiecesCanNotBeCaptured() throws ParseException {
        board = chessBoardFactory.getChessBoard("Ke8:bc6:bg6:Ne5");
        PlayersKingCheckmateCondition pkicc = new PlayersKingCheckmateCondition(blackPlayer,
                        whitePlayer, board);
        assertTrue(pkicc.canAttackingPieceNotBeCaptured());
    }

    @Test
    public void testTwoAttackingPiecesCanBeCaptured() throws ParseException {
        board = chessBoardFactory.getChessBoard("Ke8:bg6:Ne5");
        PlayersKingCheckmateCondition pkicc = new PlayersKingCheckmateCondition(blackPlayer,
                        whitePlayer, board);
        assertFalse(pkicc.canAttackingPieceNotBeCaptured());
    }

    @Test
    public void testTwoAttackingPiecesCanNotBeCapturedDueToCheck() throws ParseException {
        board = chessBoardFactory.getChessBoard("Ke8:bg6:Ne5:qe4");
        PlayersKingCheckmateCondition pkicc = new PlayersKingCheckmateCondition(blackPlayer,
                        whitePlayer, board);
        assertTrue(pkicc.canAttackingPieceNotBeCaptured());
    }

    @Test
    public void testDiscoveryCheckMate() throws ParseException, IllegalMoveException {
        board = chessBoardFactory.getChessBoard("Ke8:Rd8:Rf8:Pd7:Pf7:ka1:qe1:be2");
        chessGame = new ChessGame(board, whitePlayer, blackPlayer);
        chessGame.move(whitePlayer, new Move(new Location(E, 2), new Location(D, 3)));
    }

    @Test
    public void testIsThereMoreThanOneAttacker() throws ParseException {
        board = chessBoardFactory.getChessBoard("Ke8:bc6:bg6:Ne5");
        PlayersKingCheckmateCondition pkicc = new PlayersKingCheckmateCondition(blackPlayer,
                        whitePlayer, board);
        assertTrue(pkicc.canAttackingPieceNotBeBlocked());
    }

    @Test
    public void testCG52PawnCheckMate() throws ParseException {
        board = chessBoardFactory.getChessBoard("kc1:nc8:qc7:pb6:re1:Kd8");
        PlayersKingCheckmateCondition pkicc = new PlayersKingCheckmateCondition(blackPlayer,
                        whitePlayer, board);
        assertTrue(pkicc.isKingNotAbleToMoveOutOfCheck());
    }

}
