package org.amc.game.chessserver

import java.text.ParseException
import java.util.Arrays;
import java.util.Collection;

import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.StartingSquare;
import org.apache.taglibs.standard.lang.jstl.test.ParserTest;
import org.junit.Before;
import org.junit.Test
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;;

@RunWith(Parameterized.class)
class PromotionStompControllerParsingTest {
	PromotionStompController controller;
	def promotionString;
	
	@Parameters
	public static Collection<?> addedChessPieces() {

		return [
			'promoteqa3', 
			'e',
			'qa3',
			'promote  qa3',
			'promote qa9',
			'promote promote'
			];

	}
	
	public PromotionStompControllerParsingTest(promotionString) {
		this.promotionString = promotionString;
	}
	
	@Before
	void setUp() {
		controller = new PromotionStompController()
	}

	@Test(expected = ParseException)
	public void test() {
		controller.parsePromotionString(promotionString);
	}
}
