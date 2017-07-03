package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ChessPieceCanCopyTest {
    private ChessPiece piece;

    public ChessPieceCanCopyTest(ChessPiece piece) {
        this.piece = piece;
    }

    @Parameters
    public static Collection<?> addedChessPieces() {

        return Arrays.asList(new Object[][] { { RookPiece.getPiece(Colour.WHITE) },
                { BishopPiece.getPiece(Colour.BLACK) }, { QueenPiece.getPiece(Colour.WHITE) },
                { KingPiece.getPiece(Colour.BLACK) }, { KnightPiece.getPiece(Colour.WHITE) },
                { PawnPiece.getPiece(Colour.BLACK) } });

    }

    @Test
    public void test() {
        ChessPiece copyOfPiece = piece.copy();
        assertTrue(copyOfPiece.equals(piece));

        copyOfPiece.moved();
        ChessPiece secondCopy = copyOfPiece.copy();
        assertTrue(copyOfPiece.equals(secondCopy));
    }

}
