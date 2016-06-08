package org.amc.game.chess


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import groovy.transform.TypeChecked;

@TypeChecked
class BugCG106InvalidEnCaptureMoveTest {
    ChessBoard board;
    ChessBoardFactory cbFactory;
 
    ChessGamePlayer whitePlayer;
    ChessGamePlayer blackPlayer;
    
    
    @Before
    void setUp() {
        cbFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        
        whitePlayer = new RealChessGamePlayer(new HumanPlayer('Sarah'), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer('Laura'), Colour.BLACK);
        
        
    }
    
    private ChessGame createChessGame(String boardSetup) {
        board = cbFactory.getChessBoard(boardSetup);
        return new StandardChessGame(board, whitePlayer, blackPlayer);
    }
    
    @Test
    void invalidWhiteEnPassantCaptureMove() {
        ChessGame game  = createChessGame('ke1:Ke8:pe2:Pf7');
        game.move(whitePlayer, new Move('e2-e4'));
        game.changePlayer();
        try {
            game.move(blackPlayer, new Move('f7-e6'));
            fail('Should throw an illegalMoveException');
        } catch (IllegalMoveException ime) {
            assertTrue('Not an legal move', true);
        }
    }
    
    @Test
    void invalidBlackEnPassantCaptureMove() {
        ChessGame game  = createChessGame('ke1:Ke8:pe2:Pf7');
        game.changePlayer()
        game.move(blackPlayer, new Move('f7-f5'));
        game.changePlayer();
        try {
            game.move(whitePlayer, new Move('e2-f3'));
            fail('Should throw an illegalMoveException');
        } catch (IllegalMoveException ime) {
            assertTrue('Not an legal move', true);
        }
    }
}
