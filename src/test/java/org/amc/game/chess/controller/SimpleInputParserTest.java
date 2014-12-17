package org.amc.game.chess.controller;

import static org.junit.Assert.*;

import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.SimpleInputParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

public class SimpleInputParserTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParseMoveString() throws ParseException{
        SimpleInputParser parser=new SimpleInputParser();
        String[] inputs={"A1B1","H8H1","D3G5"};
        for(String input:inputs){
            Move move=parser.parseMoveString(input);
            Location start=move.getStart();
            Location end=move.getEnd();
            assertEquals(start.getLetter().toString(),String.valueOf(input.charAt(0)));
            assertEquals(start.getNumber(),Integer.parseInt(String.valueOf(input.charAt(1))));
            assertEquals(end.getLetter().toString(),String.valueOf(input.charAt(2)));
            assertEquals(end.getNumber(),Integer.parseInt(String.valueOf(input.charAt(3))));
        }
    }
    
    @Test(expected=ParseException.class)
    public void testParseMoveStringthrowsException()throws ParseException{
        SimpleInputParser parser=new SimpleInputParser();
        parser.parseMoveString("12345");
    }
    
    @Test(expected=ParseException.class)
    public void testParseMoveStringthrowssException()throws ParseException{
        SimpleInputParser parser=new SimpleInputParser();
        parser.parseMoveString("1234");
    }

}
