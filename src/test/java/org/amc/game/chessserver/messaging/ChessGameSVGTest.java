package org.amc.game.chessserver.messaging;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactory;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChessGameSVGTest {

    private ChessBoard chessboard;
    private ChessGameSVG svg;
    
    @Before
    public void setUp() throws Exception {
        ChessBoardFactory factory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        chessboard=factory.getChessBoard("Ke8:bc6:qe1:nf6");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        svg = new ChessGameSVG("500px", "400px");
        System.out.println(svg.generateSVG());
    }

}
