package org.amc.game.chess;

import org.amc.game.chess.AbstractChessGame.GameState
import org.amc.game.chess.view.ChessGameTextView;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import groovy.transform.TypeChecked

@TypeChecked
public class PromotionTest {

    AbstractChessGame chessGame;

    static ChessGamePlayer playerWhite;
    
	static ChessGamePlayer playerBlack;
	
	private static final String BOARD_CONFIG = 'Ke8:ke1:pa7:Bb8';
	
	private final ChessBoardFactory chessBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
    
    @BeforeClass
    static void setUpBeforeClass() {
        playerWhite = new RealChessGamePlayer(new HumanPlayer('playerOne'), Colour.WHITE);
        playerBlack = new RealChessGamePlayer(new HumanPlayer('playerTwo'), Colour.BLACK);
    }
    
    @Before
    void setup() {
        chessGame = new StandardChessGameFactory().getChessGame(
			chessBoardFactory.getChessBoard(BOARD_CONFIG),
			playerWhite, 
			playerBlack
		);     
    }
    
    @Test
    void testPromotionMove() throws IllegalMoveException {
        Move move = new Move('a7-a8');
        
        chessGame.move(playerWhite, move);
        
        PawnPromotionRule ppr = PawnPromotionRule.getInstance();
        
		ppr.promotePawnTo(chessGame, move.getEnd(), RookPiece.getPiece(Colour.WHITE));
        
        final ChessPiece promotedPiece = chessGame.getChessBoard().get(move.end);
        
        assert promotedPiece?.class == RookPiece; 
    }
}
