package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ChessPieceCanCopy {
    private ChessPiece piece;

    public ChessPieceCanCopy(ChessPiece piece) {
        this.piece = piece;
    }

    @Parameters
    public static Collection<?> addedChessPieces() {

        return Arrays.asList(new Object[][] { { new RookPiece(Colour.WHITE) },
                { new BishopPiece(Colour.BLACK) }, { new QueenPiece(Colour.WHITE) },
                { new KingPiece(Colour.BLACK) }, { new KnightPiece(Colour.WHITE) },
                { new PawnPiece(Colour.BLACK) } });

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
