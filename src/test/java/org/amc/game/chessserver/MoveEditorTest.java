package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MoveEditorTest {

    private Move expectedMove; 
    @Before
    public void setUp() throws Exception {
        expectedMove=new Move(new Location(Coordinate.A,2),new Location(Coordinate.A,3));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSetAsTextString() {
        MoveEditor editor=new MoveEditor();
        String moveString="A2-A3";
        editor.setAsText(moveString);
        assertEquals(expectedMove.getStart(),((Move)editor.getValue()).getStart());
        assertEquals(expectedMove.getEnd(),((Move)editor.getValue()).getEnd());
    }

}
