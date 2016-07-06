package org.amc.game.chess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.*;

import org.amc.game.chess.AbstractChessGame.GameState;
import org.junit.*;
import java.util.List;

public class ChessGameTest {

    private ChessGameFixture chessGameFixture;
    private ChessBoardUtilities cbUtils;
    private String endLocation;
    private String startLocation;
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;

    @Before
    public void setUp() throws Exception {
        chessGameFixture = new ChessGameFixture();
        whitePlayer = chessGameFixture.getWhitePlayer();
        blackPlayer = chessGameFixture.getBlackPlayer();
        chessGameFixture.putPieceOnBoardAt(KingPiece.getKingPiece(Colour.WHITE),
                        StartingSquare.WHITE_KING.getLocation());
        chessGameFixture.putPieceOnBoardAt(KingPiece.getKingPiece(Colour.BLACK),
                        StartingSquare.BLACK_KING.getLocation());
        startLocation = "A8";
        endLocation = "B7";
        
        cbUtils = new ChessBoardUtilities(chessGameFixture.getBoard());
    }

    @Test
    public void testChangePlayer() {    	
        assertEquals(whitePlayer, chessGameFixture.getCurrentPlayer());
        chessGameFixture.changePlayer();
        assertEquals(blackPlayer, chessGameFixture.getCurrentPlayer());
        chessGameFixture.changePlayer();
        assertEquals(whitePlayer, chessGameFixture.getCurrentPlayer());
    }

    @Test(expected = IllegalMoveException.class)
    public void testMoveWithAnEmptySquare() throws IllegalMoveException {
        chessGameFixture.move(whitePlayer, cbUtils.createMove(startLocation, endLocation));
    }

    @Test(expected = IllegalMoveException.class)
    public void testPlayerCantMoveOtherPlayersPiece() throws IllegalMoveException {
        BishopPiece bishop = BishopPiece.getBishopPiece(Colour.WHITE);
        cbUtils.addChessPieceToBoard(bishop, startLocation);
        chessGameFixture.move(blackPlayer, cbUtils.createMove(startLocation, "B7"));
    }

    @Test
    public void testPlayerCanMoveTheirOwnPiece() throws IllegalMoveException {
        BishopPiece bishop = BishopPiece.getBishopPiece(Colour.WHITE);
        cbUtils.addChessPieceToBoard(bishop, startLocation);
        chessGameFixture.move(whitePlayer, cbUtils.createMove(startLocation, endLocation));
        assertEquals(bishop.moved(), cbUtils.getPieceOnBoard(endLocation));
        assertNull(cbUtils.getPieceOnBoard(startLocation));
    }

    @Test
    public void doesGameRuleApply() {
        RookPiece rook = RookPiece.getRookPiece(Colour.WHITE);
        final String rookStartPosition = "H1";
        Move move = cbUtils.createMove(StartingSquare.WHITE_KING.getLocation().asString(), "G1");
        cbUtils.addChessPieceToBoard(rook, rookStartPosition);
        assertTrue(chessGameFixture.doesAGameRuleApply(chessGameFixture, move));
    }

    @Test
    public void doesNotGameRuleApply() {
        RookPiece rook = RookPiece.getRookPiece(Colour.WHITE);
        final String rookStartPosition = "H1";
        Move move = cbUtils.createMove(StartingSquare.WHITE_KING.getLocation().asString(), "F1");
        cbUtils.addChessPieceToBoard(rook, rookStartPosition);
        assertFalse(chessGameFixture.doesAGameRuleApply(chessGameFixture, move));
    }

    @Test
    public void gameRuleAppliedTest() throws IllegalMoveException {
        RookPiece rook = RookPiece.getRookPiece(Colour.WHITE);
        String rookStartPosition = "H1";
        Move move = cbUtils.createMove(StartingSquare.WHITE_KING.getLocation().asString(), "G1");
        cbUtils.addChessPieceToBoard(rook, rookStartPosition);
        assertTrue(chessGameFixture.doesAGameRuleApply(chessGameFixture, move));
    }
    
    @Test
    public void testMovesAreSaved() throws IllegalMoveException {
        BishopPiece bishop = BishopPiece.getBishopPiece(Colour.BLACK);
        cbUtils.addChessPieceToBoard(bishop, startLocation);
        chessGameFixture.changePlayer();
        chessGameFixture.move(blackPlayer, cbUtils.createMove(startLocation, endLocation));
        
        Move lastMove = chessGameFixture.getTheLastMove();
        
        assertEquals(lastMove.getStart().asString(), startLocation);
        assertEquals(lastMove.getEnd().asString(), endLocation);
    }

    @Test
    public void getEmptyMove() {
        Move move = chessGameFixture.getTheLastMove();
        assertTrue(move instanceof EmptyMove);
    }
    
    @Test
    public void cloneConstuctorMoveListCopyTest() {
        chessGameFixture.initialise();
        ChessGame clone = new ChessGame(chessGameFixture.getChessGame());
        assertTrue(chessGameFixture.getTheLastMove().equals(clone.getTheLastMove()));
        assertEquals(GameState.RUNNING, clone.getGameState());
    }
    
    @Test
    public void cloneConstuctorRuleListCopyTest() {
        chessGameFixture.initialise();
        ChessGame clone = new ChessGame(chessGameFixture.getChessGame());
        clone.addChessMoveRule(EnPassantRule.getInstance());
        assertEquals(3, chessGameFixture.getChessGame().getChessMoveRules().size());
        assertEquals(4, clone.getChessMoveRules().size());
        assertTrue(clone.getChessMoveRules().contains(EnPassantRule.getInstance()));
    }
    
    @Test 
    public void testCopyConstructorForEmptyConstructor() {
        ChessGame game = new ChessGame();
        game.addChessMoveRule(EnPassantRule.getInstance());
        
        ChessGame copy = new ChessGame(game);
        testForChessRules(copy);
        assertEquals(game.getBlackPlayer(), copy.getBlackPlayer());
        assertEquals(game.getWhitePlayer(), copy.getWhitePlayer());
        assertNotNull(copy.getChessBoard());
        assertFalse(game.getChessBoard() == copy.getChessBoard());
        ChessBoardUtilities.compareBoards(game.getChessBoard(), copy.getChessBoard());
        assertEquals(GameState.NEW, copy.getGameState());
    }
    
    private void testForChessRules(ChessGame game) {
        List<ChessMoveRule> rules = game.getChessMoveRules();
        assertFalse(rules.isEmpty());
        assertEquals(1, rules.size());
        assertTrue(rules.contains(EnPassantRule.getInstance()));
    }

    
    
    /**
     * JIRA CG-33 Player can make a move out of turn
     * 
     * @throws IllegalMoveException
     */
    @Test(expected = IllegalMoveException.class)
    public void notPlayersTurn() throws IllegalMoveException {
        Move move = new Move(StartingSquare.BLACK_KING.getLocation(), new Location("E7"));
        chessGameFixture.move(blackPlayer, move);
        assertEquals(whitePlayer, chessGameFixture.getCurrentPlayer());
    }
}
