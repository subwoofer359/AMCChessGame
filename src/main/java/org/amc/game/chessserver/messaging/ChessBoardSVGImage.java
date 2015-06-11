package org.amc.game.chessserver.messaging;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Location;
import org.amc.game.chess.PawnPiece;
import org.amc.game.chess.RookPiece;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.messaging.svg.SVGBlankChessBoard;
import org.amc.game.chessserver.messaging.svg.SVGChessPiece;
import org.amc.game.chessserver.messaging.svg.SVGPawnPiece;
import org.amc.game.chessserver.messaging.svg.SVGRookPiece;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

public class ChessBoardSVGImage {

    private static final Logger logger = Logger.getLogger(ChessBoardSVGImage.class);

    private ServerChessGame serverChessGame;

    private static final String SVG_NODE_G = "g";

    private static final String OUTPUT_FILE = "temp/out.jpg";

    private SVGBlankChessBoard blankChessBoardFactory;

    private Map<Class<? extends ChessPiece>, SVGChessPiece> sVGElementfactory;

    private Document document;
    private Element svgRoot;
    private Element layer;
    private String svgNS;

    public ChessBoardSVGImage() throws ParserConfigurationException {
        svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
    }

    public ChessBoardSVGImage(ServerChessGame serverChessGame) throws ParserConfigurationException {
        this();
        this.serverChessGame = serverChessGame;

    }

    private void createSVGDocument() {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        document = impl.createDocument(svgNS, "svg", null);
        svgRoot = document.getDocumentElement();
        svgRoot.setAttributeNS(null, "width", "500");
        svgRoot.setAttributeNS(null, "height", "500");
        layer = document.createElementNS(svgNS, SVG_NODE_G);
        svgRoot.appendChild(layer);

        blankChessBoardFactory = new SVGBlankChessBoard(document, svgNS);
        setUpSVGChessPieceFactory();
        blankChessBoardFactory.createBlankChessBoard(layer);
        addChessPieces();
    }

    private void setUpSVGChessPieceFactory() {
        if(this.sVGElementfactory == null) {
            this.sVGElementfactory = new HashMap<>();
        } else {
            this.sVGElementfactory.clear();
        }
        sVGElementfactory.put(PawnPiece.class, new SVGPawnPiece(document, svgNS));
        sVGElementfactory.put(RookPiece.class, new SVGRookPiece(document, svgNS));
    }

    

    public void setServerChessGame(ServerChessGame serverChessGame) {
        this.serverChessGame = serverChessGame;
        document = null;
    }

    public String getChessBoardImage() throws IOException {
        if (document == null) {
            createSVGDocument();
        }
        JPEGTranscoder t = new JPEGTranscoder();
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));

        TranscoderInput input = new TranscoderInput(document);
        
        try(OutputStream ostream = new FileOutputStream(OUTPUT_FILE)){
        	TranscoderOutput output = new TranscoderOutput(ostream);
        	t.transcode(input, output);
        	ostream.flush();
        	ostream.close();
        } catch(TranscoderException te) {
        	throw new IOException(te);
        }
        return OUTPUT_FILE;

    }

    private void addChessPieces() {
        ChessGame chessGame = this.serverChessGame.getChessGame();

        if (chessGame == null) {
            logger.debug("ChessBoard doesn't exist in this ServerChessGame");
        } else {
            ChessBoard board = chessGame.getChessBoard();
            for (int i = 1; i <= ChessBoard.BOARD_WIDTH; i++) {
                for (Coordinate letter : ChessBoard.Coordinate.values()) {
                    ChessPiece piece = board.getPieceFromBoardAt(new Location(letter, i));
                    if (piece == null) {

                    } else {
                        SVGChessPiece svgFactory = sVGElementfactory.get(piece.getClass());

                        if (svgFactory != null) {
                            Element element = svgFactory.getChessPieceElement("", new Location(
                                            letter, i), piece.getColour());
                            layer.appendChild(element);
                        }
                    }
                    System.out.println(new Location(letter, i));
                }
            }
        }
    }
}
