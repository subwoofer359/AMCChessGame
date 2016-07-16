package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactory;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.AbstractChessGame.GameState;
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
    AbstractServerChessGame scGame;
    Long GAME_UID = 123L;
    ChessGame chessGame;
    static ChessGamePlayer playerWhite;
    static ChessGamePlayer playerBlack;
    
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        playerWhite = new RealChessGamePlayer(new HumanPlayer("playerOne"), Colour.WHITE);
        playerBlack = new RealChessGamePlayer(new HumanPlayer("playerTwo"), Colour.BLACK);
    }
    
    @Before
    public void setUp() throws Exception {
        ChessBoardFactory cFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        ChessBoard board = cFactory.getChessBoard("Ke8:ke1:pa7");
        StandardChessGameFactory sFactory = new StandardChessGameFactory();
        chessGame = sFactory.getChessGame(board, playerWhite, playerBlack);
        scGame = new TwoViewServerChessGame(GAME_UID, chessGame);
    }
	
    @Test
    public void test() {
        Move move = new Move("a7:a8");
        AbstractServerChessGame spyScGame = spy(scGame);
        spyScGame.move(playerWhite, move);
        assert scGame.getChessGame().getGameState() == GameState.PAWN_PROMOTION;
        verify(spyScGame, times(1)).notifyObservers(GameState.PAWN_PROMOTION);
        assert ComparePlayers.comparePlayers(playerWhite, scGame.getChessGame().getCurrentPlayer());
    }
    
    @Test(expected = IllegalMoveException.class)
    public void testNoBlackMoveBeforePromotion() {
        Move move = new Move("a7:a8");
        AbstractServerChessGame spyScGame = spy(scGame);
        spyScGame.move(playerWhite, move);
        assert scGame.getChessGame().getGameState() == GameState.PAWN_PROMOTION;
        verify(spyScGame, times(1)).notifyObservers(GameState.PAWN_PROMOTION);
        
        spyScGame.move(playerBlack, new Move("e8:e7"));
    }
    
    @Test(expected = IllegalMoveException.class)
    public void testNoWhiteMoveBeforePromotion() {
        Move move = new Move("a7:a8");
        AbstractServerChessGame spyScGame = spy(scGame);
        spyScGame.move(playerWhite, move);
        assert scGame.getChessGame().getGameState() == GameState.PAWN_PROMOTION;
        verify(spyScGame, times(1)).notifyObservers(GameState.PAWN_PROMOTION);
        
        spyScGame.move(playerWhite, new Move("e1:e2"));
    }

}
