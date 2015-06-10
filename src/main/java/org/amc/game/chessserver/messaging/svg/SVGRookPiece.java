package org.amc.game.chessserver.messaging.svg;


import org.amc.game.chess.Location;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SVGRookPiece extends SVGChessPiece {

    public SVGRookPiece(Document document, String svgNS) {
        super(document, svgNS);
    }

    @Override
    public Element getChessPieceElement(String id, Location location, Colour pieceColour) {
        float coordX = X + (offsetXY * (location.getLetter().ordinal()));
        float coordY = -offsetXY * (location.getNumber() - 1);
        
        Element g = document.createElementNS(svgNS, "g");
        g.setAttributeNS(null, "transform", "translate(" + coordX + "," + coordY + ")");
        g.setAttributeNS(null, "id", id);
        
        Element path1 = document.createElementNS(svgNS, "path");
        Element path2 = document.createElementNS(svgNS, "path");
        Element path3 = document.createElementNS(svgNS, "path");
        Element rectangle1 = document.createElementNS(svgNS, "rect");
        Element rectangle2 = document.createElementNS(svgNS, "rect");
        Element rectangle3 = document.createElementNS(svgNS, "rect");
        Element rectangle4 = document.createElementNS(svgNS, "rect");
             
        path1.setAttributeNS(null,"style", "fill:" + pieceColour.fill + ";fill-opacity:1;stroke:" + pieceColour.stroke + ";stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50"); 
        path1.setAttributeNS(null,"d","m 19.164173,457.97417 24.363471,-0.18711 3.207655,24.82314 -30.137248,-0.0935 z"); 
        
        path2.setAttributeNS(null, "style", "fill:" + pieceColour.fill + ";fill-opacity:1;stroke:" + pieceColour.stroke + ";stroke-width:0.4675346;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dashoffset:50"); 
        path2.setAttributeNS(null, "d", "m 13.083803,446.84895 36.538917,0 -1.283062,11.11695 -33.652029,-0.0935 z"); 

        rectangle1.setAttributeNS(null, "style", "fill:" + pieceColour.fill + ";fill-opacity:1;stroke:" + pieceColour.stroke + ";stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50"); 
        rectangle1.setAttributeNS(null ,"width", "7.2172222"); 
        rectangle1.setAttributeNS(null, "height", "4.3972631"); 
        rectangle1.setAttributeNS(null, "x", "13.069632"); 
        rectangle1.setAttributeNS(null, "y", "442.34985");
        
        rectangle2.setAttributeNS(null, "style", "fill:" + pieceColour.fill + ";fill-opacity:1;stroke:" + pieceColour.stroke + ";stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50");
        rectangle2.setAttributeNS(null ,"width", "7.2172222"); 
        rectangle2.setAttributeNS(null, "height", "4.3972631"); 
        rectangle2.setAttributeNS(null, "x", "23.253931"); 
        rectangle2.setAttributeNS(null, "y", "442.30307");
        
        rectangle3.setAttributeNS(null, "style", "fill:" + pieceColour.fill + ";fill-opacity:1;stroke:" + pieceColour.stroke + ";stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50"); 
        rectangle3.setAttributeNS(null ,"width", "7.2172222"); 
        rectangle3.setAttributeNS(null, "height", "4.3972631");
        rectangle3.setAttributeNS(null, "x", "42.018715"); 
        rectangle3.setAttributeNS(null, "y", "442.30307"); 
        
        rectangle4.setAttributeNS(null, "style", "fill:" + pieceColour.fill + ";fill-opacity:1;stroke:" + pieceColour.stroke + ";stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50"); 
        rectangle4.setAttributeNS(null ,"width", "7.2172222"); 
        rectangle4.setAttributeNS(null, "height", "4.3972631");
        rectangle4.setAttributeNS(null, "x", "33.197662"); 
        rectangle4.setAttributeNS(null, "y", "442.30307");
        
        path3.setAttributeNS(null, "style", "fill:" + pieceColour.fill + ";fill-opacity:1;stroke:" + pieceColour.stroke + ";stroke-width:0.3504566;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50"); 
        path3.setAttributeNS(null, "d", "m 9.5236162,495.76948 c 12.5961078,0 32.0383488,0.37522 44.6344558,0.37522 0,-4.4898 1.120276,-10.01147 -3.609778,-13.56319 -12.596108,0 -24.943265,0 -37.539373,0 -4.3566298,2.42606 -3.4853048,8.69817 -3.4853048,13.18797 z");
        
        g.appendChild(path1);
        g.appendChild(path2);
        g.appendChild(rectangle1);
        g.appendChild(rectangle2);
        g.appendChild(rectangle3);
        g.appendChild(rectangle4);
        g.appendChild(path3);
        
        return g;
    }
    
    

}
