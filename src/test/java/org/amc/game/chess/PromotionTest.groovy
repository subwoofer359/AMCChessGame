package org.amc.game.chess;

import org.amc.game.chess.view.ChessGameTextView;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PromotionTest {

    
    ChessBoardFactory chessBoardFactory;
    
    ChessGame chessGame;
    ChessBoard board;
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
        board  = chessBoardFactory.getChessBoard("Ke8:ke1:pa7");
        StandardChessGameFactory sGameFactory = new StandardChessGameFactory();
        chessGame = sGameFactory.getChessGame(board, playerWhite, playerBlack);     
    }
    
    @Test
    public void testPromotionMove() throws IllegalMoveException {
        Move move = new Move("a7:a8");
        
        chessGame.move(playerWhite, move);
        
        PawnPromotionRule ppr = PawnPromotionRule.getInstance();
        ppr.promotePawnTo(chessGame, move.getEnd(), RookPiece.getRookPiece(Colour.WHITE));
        
        ChessPiece promotedPiece = chessGame.getChessBoard().get(new Location("a8"));
        
        assert promotedPiece?.getClass() == RookPiece.class; 
    }
}
