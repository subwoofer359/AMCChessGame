package org.amc.game.chess;

import static org.amc.game.chess.NoChessPiece.NO_CHESSPIECE;
import static org.amc.game.chess.StartingSquare.*;
import static org.junit.Assert.*;

import org.amc.game.chess.AbstractChessGame.GameState;
import org.junit.*;
import java.util.List;

public class ChessGameTest {
    private ChessBoardUtil cbUtils;
    private static final String END_LOCATION = "B7";
    private static final String START_LOCATION = "A8";
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    
    private AbstractChessGame chessGame;

    @Before
    public void setUp() throws Exception {
    	chessGame = new ChessGameFixture().getChessGame();

        whitePlayer = chessGame.getWhitePlayer();
        blackPlayer = chessGame.getBlackPlayer();
        
        
        
        cbUtils = new ChessBoardUtil(chessGame.getChessBoard());
        
        cbUtils.add(KingPiece.getPiece(Colour.WHITE), WHITE_KING);
        cbUtils.add(KingPiece.getPiece(Colour.BLACK), BLACK_KING); 
    }

    @Test
    public void testChangePlayer() {    	
        assertEquals(whitePlayer, chessGame.getCurrentPlayer());
        chessGame.changePlayer();
        assertEquals(blackPlayer, chessGame.getCurrentPlayer());
        chessGame.changePlayer();
        assertEquals(whitePlayer, chessGame.getCurrentPlayer());
    }

    @Test(expected = IllegalMoveException.class)
    public void testMoveWithAnEmptySquare() throws IllegalMoveException {
        chessGame.move(whitePlayer, cbUtils.newMove(START_LOCATION, END_LOCATION));
    }

    @Test(expected = IllegalMoveException.class)
    public void testPlayerCantMoveOtherPlayersPiece() throws IllegalMoveException {
        BishopPiece bishop = BishopPiece.getPiece(Colour.WHITE);
        cbUtils.add(bishop, START_LOCATION);
        chessGame.move(blackPlayer, cbUtils.newMove(START_LOCATION, "B7"));
    }

    @Test
    public void testPlayerCanMoveTheirOwnPiece() throws IllegalMoveException {
        BishopPiece bishop = BishopPiece.getPiece(Colour.WHITE);
        cbUtils.add(bishop, START_LOCATION);
        chessGame.move(whitePlayer, cbUtils.newMove(START_LOCATION, END_LOCATION));
        assertEquals(bishop.moved(), cbUtils.getPiece(END_LOCATION));
        assertEquals(NO_CHESSPIECE, cbUtils.getPiece(START_LOCATION));
    }

    @Test
    public void doesGameRuleApply() {
    	final String rookStartPosition = "H1";
        final String kingEndPosition = "G1";
    	
        Move castlingMove = cbUtils.newMove(WHITE_KING.toString(), kingEndPosition);
        cbUtils.add(RookPiece.getPiece(Colour.WHITE), rookStartPosition);
        assertTrue(chessGame.doesAGameRuleApply(castlingMove));
    }

    @Test
    public void doesNotGameRuleApply() {
        RookPiece rook = RookPiece.getPiece(Colour.WHITE);
        final String rookStartPosition = "H1";
        Move move = cbUtils.newMove(WHITE_KING.toString(), "F1");
        cbUtils.add(rook, rookStartPosition);
        assertFalse(chessGame.doesAGameRuleApply(move));
    }

    @Test
    public void gameRuleAppliedTest() throws IllegalMoveException {
        RookPiece rook = RookPiece.getPiece(Colour.WHITE);
        String rookStartPosition = "H1";
        Move move = cbUtils.newMove(WHITE_KING.toString(), "G1");
        cbUtils.add(rook, rookStartPosition);
        assertTrue(chessGame.doesAGameRuleApply(move));
    }
    
    @Test
    public void testMovesAreSaved() throws IllegalMoveException {
        BishopPiece bishop = BishopPiece.getPiece(Colour.BLACK);
        cbUtils.add(bishop, START_LOCATION);
        chessGame.changePlayer();
        chessGame.move(blackPlayer, cbUtils.newMove(START_LOCATION, END_LOCATION));
        
        Move lastMove = chessGame.getTheLastMove();
        
		assertEquals(lastMove.getStart().asString(), START_LOCATION);
        assertEquals(lastMove.getEnd().asString(), END_LOCATION);
    }
    
    @Test
    public void testMovesAreSavedOnRuleApplied() throws IllegalMoveException {
    	final RookPiece rook = RookPiece.getPiece(Colour.WHITE);
    	cbUtils.add(rook, WHITE_ROOK_RIGHT);
    	String startLocation = "E1";
    	String endLocation = "G1"; 
    	
    	chessGame.move(whitePlayer, cbUtils.newMove(startLocation, endLocation));
        
        Move lastMove = chessGame.getTheLastMove();
        
        assertEquals("Should only be one move saved", 
        		1, chessGame.allGameMoves.size());
        
		assertEquals(lastMove.getStart().asString(), startLocation);
        assertEquals(lastMove.getEnd().asString(), endLocation);
    }

    @Test
    public void getEmptyMove() {
        Move move = chessGame.getTheLastMove();
        assertTrue(move instanceof EmptyMove);
    }
    
    @Test
    public void cloneConstuctorMoveListCopyTest() {
        chessGame.getChessBoard().initialise();
        ChessGame clone = new ChessGame(chessGame);
        assertTrue(chessGame.getTheLastMove().equals(clone.getTheLastMove()));
        assertEquals(GameState.RUNNING, clone.getGameState());
    }
    
    @Test
    public void cloneConstuctorRuleListCopyTest() {
        chessGame.getChessBoard().initialise();
        ChessGame clone = new ChessGame(chessGame);
        clone.addChessMoveRule(EnPassantRule.getInstance());
        assertEquals(3, chessGame.getChessMoveRules().size());
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
        
        ChessBoardUtil.compareBoards(game.getChessBoard(), copy.getChessBoard());
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
        Move move = cbUtils.newMove(BLACK_KING, "E7");
        chessGame.move(blackPlayer, move);
        assertEquals(whitePlayer, chessGame.getCurrentPlayer());
    }
}
