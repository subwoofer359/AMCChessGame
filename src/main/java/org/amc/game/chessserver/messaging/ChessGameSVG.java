package org.amc.game.chessserver.messaging;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessBoard.Coordinate;

@Deprecated
public class ChessGameSVG {

    private ChessGame chessGame;
    private String width;
    private String height;
    private static float Xdelta = 62.5f;
    private static float Ydelta = 62.5f;
    private static String whiteSquareColour = "#e6e6e6";
    private static String blackSquareColour = "#000000";
    
    public ChessGameSVG(String width, String height) {
        this.height = height;
        this.width = width;
    }
    
    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }
    
    public String generateSVG() {
        StringBuilder svg = new StringBuilder();
        svg.append(String.format("<svg width=\"%s\" height=\"%s\">%n", width , height));
        svg.append("<g id=\"layer1\">\n");
        svg.append(generateChessBoard());
        svg.append(attachChessPieces());
        svg.append("</g>\n");
        svg.append("</svg>");
        
        return svg.toString();
    }
    
    private String generateChessBoard() {
        boolean whiteToggle = true;
        StringBuilder svg = new StringBuilder();
        float x = 0;
        float y = 0;
        for(int i = 0 ; i < ChessBoard.BOARD_WIDTH; i++){
            x = 0;
            for(Coordinate coord : ChessBoard.Coordinate.values()) {
                svg.append(String.format("<rect id=\"%s%d\" ", coord, i));
                svg.append(String.format("x=\"%f\" ", x));
                svg.append(String.format("y=\"%f\" ", y));
                svg.append(String.format("width=\"%f\" ", Xdelta));
                svg.append(String.format("height=\"%f\" ", Ydelta));
                if(whiteToggle) {
                    svg.append(String.format("fill=\"%s\" ", whiteSquareColour));
                } else {
                    svg.append(String.format("fill=\"%s\" ", blackSquareColour));
                }
                
                svg.append("stroke=\"none\" ");
                svg.append("class=\"dropzone\">");
                x = x + Xdelta;
                svg.append("</rect>\n");
                whiteToggle = !whiteToggle;
            }
            y = y + Ydelta;
            whiteToggle = !whiteToggle;
        }
        
        return svg.toString();
    }
    
    private String attachChessPieces() {
        return "";
    }
}
