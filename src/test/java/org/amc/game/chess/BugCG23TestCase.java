package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.view.ChessBoardView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

/**
 * ChessBoard.isEndSquareEmpty method raises an ArrayIndexOutOfBoundsException
 * @author Adrian Mclaughlin
 *
 */
public class BugCG23TestCase {
    private ChessGameFixture chessGameFixture;

    @Before
    public void setUp() throws Exception {
        chessGameFixture = new ChessGameFixture();
        ChessBoardFactory factory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        ChessBoard board = factory.getChessBoard("Ra8:Nb8:Qd8:Ke8:Bf8:Ng8:Rh8:"
                        + "Pa7:Pb7:Pe7:Pf7:Pg7:" + "pc6:Ph6:" + "pa5:" + "qa4:Pd4:" + "pc3:"
                        + "pd2:pe2:pf2:ph2:" + "ra1:nb1:bc1:ke1:bf1:ng1:Bh1");
        chessGameFixture.setBoard(board);
        new ChessBoardView(board);
    }

    @Test
    public void test() throws ParseException, IllegalMoveException {
        Move move = new Move("C6-B7");
        chessGameFixture.move(chessGameFixture.getWhitePlayer(), move);
        chessGameFixture.changePlayer();
        PlayersKingCheckmateCondition pkcc = new PlayersKingCheckmateCondition(chessGameFixture.getBlackPlayer(), 
                        chessGameFixture.getWhitePlayer(), chessGameFixture.getBoard());
        assertFalse(pkcc.isCheckMate());
    }

}
