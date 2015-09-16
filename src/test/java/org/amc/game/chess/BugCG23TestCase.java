package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;

import org.amc.game.chess.view.ChessBoardView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

public class BugCG23TestCase {
    private ChessGameFixture chessGameFixture;
    private ChessBoardFactory factory;

    @Before
    public void setUp() throws Exception {
        chessGameFixture = new ChessGameFixture();
        factory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        ChessBoard board = factory.getChessBoard("Ra8:Nb8:Qd8:Ke8:Bf8:Ng8:Rh8:"
                        + "Pa7:Pb7:Pe7:Pf7:Pg7:" + "pc6:Ph6:" + "pa5:" + "qa4:Pd4:" + "pc3:"
                        + "pd2:pe2:pf2:ph2:" + "ra1:nb1:bc1:ke1:bf1:ng1:Bh1");
        chessGameFixture.setBoard(board);
        new ChessBoardView(board);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws ParseException, IllegalMoveException {
        Move move = new Move(new Location(C, 6), new Location(B, 7));
        chessGameFixture.move(chessGameFixture.getWhitePlayer(), move);
        chessGameFixture.changePlayer();
        chessGameFixture.isCheckMate(chessGameFixture.getBlackPlayer(),chessGameFixture.getBoard());
    }

}
