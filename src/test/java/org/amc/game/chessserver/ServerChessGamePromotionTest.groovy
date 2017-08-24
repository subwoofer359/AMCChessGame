package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.amc.game.chess.AbstractChessGame.GameState.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactory;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Move;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chess.StandardChessGameFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

class ServerChessGamePromotionTest {
    
	private static ChessGamePlayer playerWhite;
	private static ChessGamePlayer playerBlack;
	private static final ChessBoardFactory cFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
	private static final StandardChessGameFactory sFactory = new StandardChessGameFactory();
	private static final String CHESSBOARD = 'Ke8:ke1:pa7';
	private static final Move move = new Move('a7:a8');
	private static Long GAME_UID = 123L;
	
	AbstractServerChessGame scGame;
    ChessGame chessGame;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        playerWhite = new RealChessGamePlayer(new HumanPlayer('playerOne'), Colour.WHITE);
        playerBlack = new RealChessGamePlayer(new HumanPlayer('playerTwo'), Colour.BLACK);
    }
    
    @Before
    void setUp() {
        chessGame = sFactory.getChessGame(cFactory.getChessBoard(CHESSBOARD), playerWhite, playerBlack);
        scGame = new TwoViewServerChessGame(GAME_UID, chessGame);
    }
	
    @Test
    void test() {
        Move move = new Move();
        AbstractServerChessGame spyScGame = spy(scGame);
        
		spyScGame.move(playerWhite, move);
        
		assert scGame.chessGame.gameState == PAWN_PROMOTION;
        verify(spyScGame, times(1)).notifyObservers(PAWN_PROMOTION);
        assert ComparePlayers.isSamePlayer(playerWhite, scGame.getChessGame().getCurrentPlayer());
    }
    
    @Test(expected = IllegalMoveException)
    void testNoBlackMoveBeforePromotion() {
        AbstractServerChessGame spyScGame = spy(scGame);
        
		spyScGame.move(playerWhite, move);
        
		assert scGame.chessGame.gameState == PAWN_PROMOTION;
        verify(spyScGame, times(1)).notifyObservers(PAWN_PROMOTION);
        
        spyScGame.move(playerBlack, new Move('e8:e7'));
    }
    
    @Test(expected = IllegalMoveException)
    void testNoWhiteMoveBeforePromotion() {
        AbstractServerChessGame spyScGame = spy(scGame);
        
		spyScGame.move(playerWhite, move);
        
		assert scGame.chessGame.gameState == PAWN_PROMOTION;
        verify(spyScGame, times(1)).notifyObservers(PAWN_PROMOTION);
        
        spyScGame.move(playerWhite, new Move('e1:e2'));
    }

}
