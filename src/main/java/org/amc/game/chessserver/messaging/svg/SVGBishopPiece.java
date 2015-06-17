package org.amc.game.chessserver.messaging.svg;

import org.amc.game.chess.Colour;
import org.amc.game.chess.Location;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class SVGBishopPiece extends SVGChessPiece {

	public SVGBishopPiece(Document document, String svgNS) {
		super(document, svgNS);
	}
	
	@Override
	public Element getChessPieceElement(String id, Location location,
			Colour pieceColour) {
		SVGColour svgColour = SVGColour.getColour(pieceColour);
		float coordX = offsetXY * (location.getLetter().ordinal());
	    float coordY = -554 - offsetXY * (location.getNumber() - 1);
		
	    String styleString_1 = "fill:" + svgColour.fill + ";fill-opacity:1;stroke:"  + svgColour.stroke + 
	    		";stroke-width:0.7;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;"
	    		+ "stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50";
	    
	    String transform_1 = "translate(0,989.86218)";
	    
	    Element g = createG(id, "translate(" + coordX + "," + coordY + ")");
        						
        setTransform(appendNewRectangle(g, "14.520943", "55.176395", "35.355339", "4.0406103", styleString_1),
                        transform_1);
        
        setTransform(appendNewRectangle(g, "16.793787", "50.630707", "30.430845", "4.2931485", styleString_1),
                        transform_1);
        setTransform(appendNewRectangle(g, "23.220133", "25.23", "17.578154", "2.9223585", styleString_1), 
                        transform_1);
        setTransform(appendNewPath(g, "M 39.342805,6.9601953 A 6.4397225,9.975256 0 1 1 38.535974,5.600699 l -4.317056,7.401826 z",
                        styleString_1), "translate(-1.894036,992.13502)");  
        
        setTransform(appendNewPath(g, "m 34.084445,2.8670609 a 1.8940361,1.5152289 0 1 1 -0.0018,-0.00187",
                        styleString_1), "translate(0,989.86218)");  
        
 
        appendNewPath(g, "m 28.663078,1018.5221 6.565991,0 1.495423,12.3247 8.281137,9.8247 -25.792606,0 7.760741,-9.8247 z",  
        styleString_1);
        
		return g;
	}

}
