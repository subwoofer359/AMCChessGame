package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

class ChessBoardGetPieceToBePromotedTest {
    ChessBoard board;
    static ChessBoardFactory cFactory;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        cFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
    }

    @Test
    public void testFindWhitePawnForPromotion() {
        board = cFactory.getChessBoard("ke1:Ke8:Pb1:pa1:pb8");
        ChessPieceLocation pieceLocation = board.getPawnToBePromoted(Colour.WHITE);
        assert pieceLocation?.piece instanceof PawnPiece;
        assert pieceLocation?.piece?.colour == Colour.WHITE;
        assert pieceLocation?.location == new Location("b8");
    }
    
    @Test
    public void testFindBlackPawnForPromotion() {
        board = cFactory.getChessBoard("ke1:Ke8:Pb1:pa1:pb8");
        ChessPieceLocation pieceLocation = board.getPawnToBePromoted(Colour.BLACK);
        assert pieceLocation?.piece instanceof PawnPiece;
        assert pieceLocation?.piece?.colour == Colour.BLACK;
        assert pieceLocation?.location == new Location("b1");
    }
    
    @Test
    public void testFindNoPawnForPromotion() {
        board = cFactory.getChessBoard("ke1:Ke8:Pb8:pa1:pb1");
        ChessPieceLocation pieceLocation = board.getPawnToBePromoted(Colour.BLACK);
        assert pieceLocation == null;
    }

}
