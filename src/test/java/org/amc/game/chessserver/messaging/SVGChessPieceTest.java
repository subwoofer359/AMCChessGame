package org.amc.game.chessserver.messaging;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.Colour;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chessserver.messaging.svg.SVGBishopPiece;
import org.amc.game.chessserver.messaging.svg.SVGBlankChessBoard;
import org.amc.game.chessserver.messaging.svg.SVGChessPiece;
import org.amc.game.chessserver.messaging.svg.SVGPawnPiece;
import org.amc.game.chessserver.messaging.svg.SVGRookPiece;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.tools.ant.filters.StringInputStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



public class SVGChessPieceTest {
	
	private static final String SVG_DOCTYPE = "\n<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" "
			+ "\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n";
	private DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
	private Document document;
    private Element svgRoot;
    private Element layer;
    private String svgNS;
    
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); 
    private DocumentBuilder dBuilder;
    private Transformer transformer;
    
    
    public SVGChessPieceTest() {
    	dbFactory.setValidating(true);
    	try {
			dBuilder = dbFactory.newDocumentBuilder();
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @Rule
    public Timeout timeout =new Timeout(10, TimeUnit.SECONDS);
    
    @Before
    public void setUp() throws Exception {
    	svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
    	
    	document = impl.createDocument(svgNS, "svg", null);
        svgRoot = document.getDocumentElement();
        svgRoot.setAttributeNS(null, "width", "500");
        svgRoot.setAttributeNS(null, "height", "500");
        svgRoot.setAttribute("version", "1.1");
        layer = document.createElementNS(svgNS, "g");
        svgRoot.appendChild(layer);
    }
    
    @Test
    public void testSVGPawnPiece() throws Exception{
    	SVGChessPiece piece = new SVGPawnPiece(document, svgNS);
    	Element element = piece.getChessPieceElement("test", new Location(Coordinate.A ,1), Colour.BLACK);
    	layer.appendChild(element);
    	assertTrue(isvalidElement());
    }
    
    @Test
    public void testSVGRookPiece() throws Exception{
    	SVGChessPiece piece = new SVGRookPiece(document, svgNS);
    	Element element = piece.getChessPieceElement("test", new Location(Coordinate.A ,1), Colour.WHITE);
    	layer.appendChild(element);
    	assertTrue(isvalidElement());
    }
    
    @Test
    public void testSVGBishopPiece() throws Exception{
    	SVGChessPiece piece = new SVGBishopPiece(document, svgNS);
    	Element element = piece.getChessPieceElement("test", new Location(Coordinate.A ,1), Colour.WHITE);
    	layer.appendChild(element);
    	assertTrue(isvalidElement());
    }
    
    @Test
    public void testBlankChessBoard() throws Exception {
    	SVGBlankChessBoard board = new SVGBlankChessBoard(document, svgNS);
    	board.createBlankChessBoard(layer);
    	assertTrue(isvalidElement());
    }
    
    @Test
    public void testMarkBlankChessBoard() throws Exception {
    	Move move = new Move(new Location(Coordinate.A,1), new Location(Coordinate.E, 8));
    	List<String> locationStr = Arrays.asList("A1","E8");
    	
    	SVGBlankChessBoard board = new SVGBlankChessBoard(document, svgNS);
    	board.createBlankChessBoard(layer);
    	board.markMove(layer, move);
    	assertTrue(isvalidElement());
    	
    	NodeList list = layer.getChildNodes();
    	for(int i = 0 ;i < list.getLength();i++) {
    		if(list.item(i) instanceof Element) {
    			Element element = (Element)list.item(i);
    			if(element.getAttribute("fill").equals("red")) {
    				assertTrue(locationStr.contains(element.getAttribute("id")));
    			}
    		}
    	}
    }
    
    @Test
    public void testMarkEmptyMoveBlankChessBoard() throws Exception {
    	Move move = Move.EMPTY_MOVE;
    	
    	SVGBlankChessBoard board = new SVGBlankChessBoard(document, svgNS);
    	board.createBlankChessBoard(layer);
    	board.markMove(layer, move);
    	assertTrue(isvalidElement());
    	
    	int noOfMarkSquares = 0;
    	
    	NodeList list = layer.getChildNodes();
    	for(int i = 0 ;i < list.getLength();i++) {
    		if(list.item(i) instanceof Element) {
    			Element element = (Element)list.item(i);
    			if(element.getAttribute("fill").equals("red")) {
    				noOfMarkSquares++;
    			}
    		}
    	}
    	assertEquals(0, noOfMarkSquares);
    }
    
    
    private boolean isvalidElement() throws Exception {
        StringWriter writer = new StringWriter();
        Source input = new DOMSource(document);
        Result output = new StreamResult(writer);
        transformer.transform(input, output);
        
        String svg = getSVGString(writer.toString());
        
        svg = SVG_DOCTYPE + svg;
        
        StringInputStream reader = new StringInputStream(svg);
        dBuilder.parse(reader);
        
        return true;
    }
    
    private String getSVGString(String html) {
        String startTag = "<svg";
        String endTag = "</svg>";
        
        int start = html.indexOf(startTag);
        int end = html.indexOf(endTag);
        return html.substring(start, end + endTag.length());
    }
    
}
