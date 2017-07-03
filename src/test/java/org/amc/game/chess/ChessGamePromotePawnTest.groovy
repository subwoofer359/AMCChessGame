package org.amc.game.chess;

import static org.amc.game.chess.NoChessPiece.NO_CHESSPIECE;

import static org.junit.Assert.*;

import org.amc.game.chess.AbstractChessGame.GameState;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

class ChessGamePromotePawnTest {

    ChessBoardFactory chessBoardFactory;
    
    ChessGame chessGame;
    ChessBoard board;
    static PawnPromotionRule pawnPromotionRule = PawnPromotionRule.getInstance();
    static ChessGamePlayer playerWhite;
    static ChessGamePlayer playerBlack;
    
    @BeforeClass
    static void setUpBeforeClass() {
        playerWhite = new RealChessGamePlayer(new HumanPlayer("playerOne"), Colour.WHITE);
        playerBlack = new RealChessGamePlayer(new HumanPlayer("playerTwo"), Colour.BLACK);
    }
    
    @Before
    void setup() throws Exception {
        chessBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        board  = chessBoardFactory.getChessBoard("Ke8:ke1:pa7:pb7:ng8");
        StandardChessGameFactory sGameFactory = new StandardChessGameFactory();
        chessGame = sGameFactory.getChessGame(board, playerWhite, playerBlack);
        Move move = new Move("a7:a8");
        
        chessGame.move(playerWhite, move);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        assert chessGame.getGameState() == GameState.PAWN_PROMOTION;
        Location promotionLocation = new Location("a8");
        ChessPiece pieceToBePromotedTo = RookPiece.getRookPiece(playerWhite.colour);
        pawnPromotionRule.promotePawnTo(chessGame, promotionLocation, pieceToBePromotedTo);
        assertIsARook(promotionLocation);
        assert chessGame.getGameState() == GameState.RUNNING;
        assertCurrentPlayerHasChanged();
    }
    
    @Test
    public void testPromotionToKingNotAllowed() {
        assert chessGame.getGameState() == GameState.PAWN_PROMOTION;
        Location promotionLocation = new Location("a8");
        ChessPiece pieceToBePromotedTo = KingPiece.getKingPiece(playerWhite.colour);
        try {
            pawnPromotionRule.promotePawnTo(chessGame, promotionLocation, pieceToBePromotedTo);
            fail("Promotion to king shouldn't be allowed");
        } catch(IllegalMoveException ime) {
            assert chessGame.getGameState() == GameState.PAWN_PROMOTION;
            assertCurrentPlayerHasNotChanged();
        }
    }
    
    @Test
    public void testPromotionToPawnNotAllowed() {
        assert chessGame.getGameState() == GameState.PAWN_PROMOTION;
        Location promotionLocation = new Location("a8");
        ChessPiece pieceToBePromotedTo = PawnPiece.getPawnPiece(playerWhite.colour);
        try {
            pawnPromotionRule.promotePawnTo(chessGame, promotionLocation, pieceToBePromotedTo);
            fail("Promotion to king shouldn't be allowed");
        } catch(IllegalMoveException ime) {
            assert chessGame.getGameState() == GameState.PAWN_PROMOTION;
            assertCurrentPlayerHasNotChanged();
        }
    }
    
    @Test
    public void testPromotionPawnNotInEndRank() {
        Location promotionLocation = new Location("b7");
        ChessPiece pieceToBePromotedTo = RookPiece.getRookPiece(playerWhite.colour);
        try {
            pawnPromotionRule.promotePawnTo(chessGame, promotionLocation, pieceToBePromotedTo);
            fail();
        } catch(IllegalMoveException ime) {
            assertIsAPawn(promotionLocation);
            assertCurrentPlayerHasNotChanged();
            assert chessGame.getGameState() == GameState.PAWN_PROMOTION;
        }
    }
    
    @Test
    public void testPromotionOfNotPawn() {
        Location promotionLocation = new Location("g8");
        ChessPiece pieceToBePromotedTo = RookPiece.getRookPiece(playerWhite.colour);
        try {
            pawnPromotionRule.promotePawnTo(chessGame, promotionLocation, pieceToBePromotedTo);
            fail();
        } catch(IllegalMoveException ime) {
            assertIsNotAPawn(promotionLocation);
            assertCurrentPlayerHasNotChanged();
            assert chessGame.getGameState() == GameState.PAWN_PROMOTION;
        }
    }
    
    @Test
    public void testPromotionOfPawnWithADifferentColour() {
        Location promotionLocation = new Location("a8");
        ChessPiece pieceToBePromotedTo = RookPiece.getRookPiece(playerBlack.colour);
        try {
            pawnPromotionRule.promotePawnTo(chessGame, promotionLocation, pieceToBePromotedTo);
            fail();
        } catch(IllegalMoveException ime) {
            assertIsAPawn(promotionLocation);
            assertCurrentPlayerHasNotChanged();
            assert chessGame.getGameState() == GameState.PAWN_PROMOTION;
        }
    }
    
    @Test
    public void testPromotionOfAnEmptySquare() {
        Location promotionLocation = new Location("a3");
        ChessPiece pieceToBePromotedTo = RookPiece.getRookPiece(playerBlack.colour);
        assert chessGame.chessBoard.get(promotionLocation) == NO_CHESSPIECE;
        
        try {
            pawnPromotionRule.promotePawnTo(chessGame, promotionLocation, pieceToBePromotedTo);
            fail();
        } catch(IllegalMoveException ime) {
            assert chessGame.chessBoard.get(promotionLocation) == NO_CHESSPIECE;
            assertCurrentPlayerHasNotChanged();
            assert chessGame.getGameState() == GameState.PAWN_PROMOTION;
        }
    }

    private void assertIsAPawn(Location location) {
        assert chessGame.chessBoard.get(location)?.getClass() == PawnPiece.class;
    }
    
    private void assertIsARook(Location location) {
        assert chessGame.chessBoard.get(location)?.getClass() == RookPiece.class;
    }
    
    private void assertIsNotAPawn(Location location) {
        assert chessGame.chessBoard.get(location)?.getClass() != PawnPiece.class;
    }
    
    private void assertCurrentPlayerHasChanged() {
        assert ComparePlayers.isSamePlayer(chessGame.currentPlayer, playerBlack) == true;
    }
    
    private void assertCurrentPlayerHasNotChanged() {
        assert ComparePlayers.isSamePlayer(chessGame.currentPlayer, playerWhite) == true;
    }
}
