package org.amc.game.chessserver.messaging.svg;

import org.amc.game.chess.Colour;
import org.amc.game.chess.Location;
import org.w3c.dom.Attr;
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
		
	    Attr styleAttr = (Attr)document.createAttributeNS(null, "style");
	    styleAttr.setValue("fill:" + svgColour.fill + ";fill-opacity:1;stroke:"  + svgColour.stroke + 
	    		";stroke-width:0.7;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;"
	    		+ "stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50");
	    
	    Element g = document.createElementNS(svgNS, "g");
        g.setAttributeNS(null, "transform", "translate(" + coordX + "," + coordY + ")");
        g.setAttributeNS(null, "id", id);
        						
        Element rectangle1 = document.createElementNS(svgNS, "rect");
        rectangle1.setAttributeNodeNS(styleAttr);
        rectangle1.setAttributeNS(null, "width", "35.355339");
        rectangle1.setAttributeNS(null, "height", "4.0406103");
        rectangle1.setAttributeNS(null, "x", "14.520943");
        rectangle1.setAttributeNS(null, "y", "55.176395");
        rectangle1.setAttributeNS(null, "transform", "translate(0,989.86218)");
        
        Element rectangle2 = (Element)rectangle1.cloneNode(true); 
        rectangle2.setAttributeNS(null, "width", "30.430845");
        rectangle2.setAttributeNS(null, "height", "4.2931485");
        rectangle2.setAttributeNS(null, "x", "16.793787");
        rectangle2.setAttributeNS(null, "y", "50.630707");  
       
        Element rectangle3 = (Element)rectangle1.cloneNode(true); 
        rectangle3.setAttributeNS(null, "width", "17.578154");
        rectangle3.setAttributeNS(null, "height", "2.9223585");
        rectangle3.setAttributeNS(null, "x", "23.220133");
        rectangle3.setAttributeNS(null, "y", "25.23");
        
        Element path1 = (Element)document.createElementNS(svgNS, "path");
                                 
        path1.setAttributeNS(null, "d" ,"M 39.342805,6.9601953 A 6.4397225,9.975256 0 1 1 38.535974,5.600699 l -4.317056,7.401826 z"); 
        path1.setAttributeNS(null, "transform", "translate(-1.894036,992.13502)");  
        path1.setAttributeNodeNS(styleAttr);
        
        Element path2 = (Element)document.createElementNS(svgNS, "path");
        path2.setAttributeNS(null, "d", "m 34.084445,2.8670609 a 1.8940361,1.5152289 0 1 1 -0.0018,-0.00187"); 
        path2.setAttributeNS(null, "transform", "translate(0,989.86218)");  
        path2.setAttributeNodeNS(styleAttr);
        
        Element path3 = (Element)document.createElementNS(svgNS, "path");
        path3.setAttributeNS(null, "d", "m 28.663078,1018.5221 6.565991,0 1.495423,12.3247 8.281137,9.8247 -25.792606,0 7.760741,-9.8247 z");  
        path3.setAttributeNodeNS(styleAttr);
        
        g.appendChild(rectangle1);
        g.appendChild(rectangle2);
        g.appendChild(rectangle3);
        g.appendChild(path1);
        g.appendChild(path2);
        g.appendChild(path3);
        
		return g;
	}

}
