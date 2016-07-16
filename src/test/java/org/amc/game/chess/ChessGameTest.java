package org.amc.game.chess;

import static org.amc.game.chess.StartingSquare.*;
import static org.junit.Assert.*;

import org.amc.game.chess.AbstractChessGame.GameState;
import org.junit.*;
import java.util.List;

public class ChessGameTest {

    private ChessGameFixture chessGameFixture;
    private ChessBoardUtilities cbUtils;
    private static final String END_LOCATION = "B7";
    private static final String START_LOCATION = "A8";
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;

    @Before
    public void setUp() throws Exception {
        chessGameFixture = new ChessGameFixture();

        whitePlayer = chessGameFixture.getWhitePlayer();
        blackPlayer = chessGameFixture.getBlackPlayer();
        
        cbUtils = new ChessBoardUtilities(chessGameFixture.getBoard());
        
        cbUtils.addChessPieceToBoard(KingPiece.getKingPiece(Colour.WHITE),
                        WHITE_KING.getLocationStr());
        cbUtils.addChessPieceToBoard(KingPiece.getKingPiece(Colour.BLACK),
                        BLACK_KING.getLocationStr());
        
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
        chessGameFixture.move(whitePlayer, cbUtils.createMove(START_LOCATION, END_LOCATION));
    }

    @Test(expected = IllegalMoveException.class)
    public void testPlayerCantMoveOtherPlayersPiece() throws IllegalMoveException {
        BishopPiece bishop = BishopPiece.getBishopPiece(Colour.WHITE);
        cbUtils.addChessPieceToBoard(bishop, START_LOCATION);
        chessGameFixture.move(blackPlayer, cbUtils.createMove(START_LOCATION, "B7"));
    }

    @Test
    public void testPlayerCanMoveTheirOwnPiece() throws IllegalMoveException {
        BishopPiece bishop = BishopPiece.getBishopPiece(Colour.WHITE);
        cbUtils.addChessPieceToBoard(bishop, START_LOCATION);
        chessGameFixture.move(whitePlayer, cbUtils.createMove(START_LOCATION, END_LOCATION));
        assertEquals(bishop.moved(), cbUtils.getPieceOnBoard(END_LOCATION));
        assertNull(cbUtils.getPieceOnBoard(START_LOCATION));
    }

    @Test
    public void doesGameRuleApply() {
        RookPiece rook = RookPiece.getRookPiece(Colour.WHITE);
        final String rookStartPosition = "H1";
        Move move = cbUtils.createMove(WHITE_KING.getLocationStr(), "G1");
        cbUtils.addChessPieceToBoard(rook, rookStartPosition);
        assertTrue(chessGameFixture.doesAGameRuleApply(chessGameFixture, move));
    }

    @Test
    public void doesNotGameRuleApply() {
        RookPiece rook = RookPiece.getRookPiece(Colour.WHITE);
        final String rookStartPosition = "H1";
        Move move = cbUtils.createMove(WHITE_KING.getLocationStr(), "F1");
        cbUtils.addChessPieceToBoard(rook, rookStartPosition);
        assertFalse(chessGameFixture.doesAGameRuleApply(chessGameFixture, move));
    }

    @Test
    public void gameRuleAppliedTest() throws IllegalMoveException {
        RookPiece rook = RookPiece.getRookPiece(Colour.WHITE);
        String rookStartPosition = "H1";
        Move move = cbUtils.createMove(WHITE_KING.getLocationStr(), "G1");
        cbUtils.addChessPieceToBoard(rook, rookStartPosition);
        assertTrue(chessGameFixture.doesAGameRuleApply(chessGameFixture, move));
    }
    
    @Test
    public void testMovesAreSaved() throws IllegalMoveException {
        BishopPiece bishop = BishopPiece.getBishopPiece(Colour.BLACK);
        cbUtils.addChessPieceToBoard(bishop, START_LOCATION);
        chessGameFixture.changePlayer();
        chessGameFixture.move(blackPlayer, cbUtils.createMove(START_LOCATION, END_LOCATION));
        
        Move lastMove = chessGameFixture.getTheLastMove();
        
		assertEquals(lastMove.getStart().asString(), START_LOCATION);
        assertEquals(lastMove.getEnd().asString(), END_LOCATION);
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
        Move move = cbUtils.createMove(BLACK_KING.getLocationStr(), "E7");
        chessGameFixture.move(blackPlayer, move);
        assertEquals(whitePlayer, chessGameFixture.getCurrentPlayer());
    }
}
