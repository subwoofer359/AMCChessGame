package org.amc.dao

import static org.junit.Assert.*;

import org.amc.game.chess.Move;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for MoveListExternalizerTest
 * @author adrian
 *
 */
class MoveListExternalizerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testStringOfAllMoves() {
		def listOfMoves = [];
		def stringOutput = "";
		def moveStr = ["A1-A2", "B1-B2", "C1-C3"];
		
		moveStr.eachWithIndex  { str, index ->
			listOfMoves << new Move(str);
			stringOutput <<= str;
			if(index < moveStr.size() - 1) {
				stringOutput <<= Move.MOVE_SEPARATOR;
			}
		};
		
		def resultList = MoveListExternalizer.stringOfAllMoves(listOfMoves);
		
		assert resultList == stringOutput.toString();
	}

	@Test
	public void testListOfMovesFromString() {
		String moveStr = 'A1-A2:B2-B3:C4-C5';
		
		def listOfMoves = MoveListExternalizer.listOfMovesFromString(moveStr);
		
		def NoOfMoves = moveStr.split(Move.MOVE_SEPARATOR.toString()).length;
		
		assert listOfMoves.size() == NoOfMoves;
	}

}
