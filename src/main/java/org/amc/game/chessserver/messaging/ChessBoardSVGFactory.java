package org.amc.game.chessserver.messaging;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.BishopPiece;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.KnightPiece;
import org.amc.game.chess.KingPiece;
import org.amc.game.chess.Location;
import org.amc.game.chess.PawnPiece;
import org.amc.game.chess.QueenPiece;
import org.amc.game.chess.RookPiece;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.messaging.svg.SVGBishopPiece;
import org.amc.game.chessserver.messaging.svg.SVGBlankChessBoard;
import org.amc.game.chessserver.messaging.svg.SVGChessPiece;
import org.amc.game.chessserver.messaging.svg.SVGKnightPiece;
import org.amc.game.chessserver.messaging.svg.SVGKingPiece;
import org.amc.game.chessserver.messaging.svg.SVGPawnPiece;
import org.amc.game.chessserver.messaging.svg.SVGQueenPiece;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

public class ChessBoardSVGFactory {

    private static final Logger logger = Logger.getLogger(ChessBoardSVGFactory.class);

    private ServerChessGame serverChessGame;

    private static final String SVG_NODE_G = "g";

    private static final String OUTPUT_DIR = "temp";
    
    private static final String TEMP_FILE_PREFIX = "svg_";
    
    private static final String TEMP_FILE_SUFFIX = ".jpg";

    private SVGBlankChessBoard blankChessBoardFactory;

    private Map<Class<? extends ChessPiece>, SVGChessPiece> sVGElementfactory;

    private Document document;
    private Element svgRoot;
    private Element layer;
    private String svgNS;

    public ChessBoardSVGFactory() throws ParserConfigurationException {
        svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
    }

    public ChessBoardSVGFactory(ServerChessGame serverChessGame) throws ParserConfigurationException {
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
        blankChessBoardFactory.markMove(layer, serverChessGame.getChessGame().getChessBoard().getTheLastMove());
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
        sVGElementfactory.put(BishopPiece.class, new SVGBishopPiece(document, svgNS));
        sVGElementfactory.put(KnightPiece.class, new SVGKnightPiece(document, svgNS));
        sVGElementfactory.put(QueenPiece.class, new SVGQueenPiece(document, svgNS));
        sVGElementfactory.put(KingPiece.class, new SVGKingPiece(document, svgNS));
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
                            logger.info("ChessBoardSVGFactory:adding chesspiece:" + piece.toString() + " at location:" + letter + i);
                            Element element = svgFactory.getChessPieceElement("", new Location(
                                            letter, i), piece.getColour());
                            layer.appendChild(element);
                        }
                    }
                }
            }
        }
    }

    

    public void setServerChessGame(ServerChessGame serverChessGame) {
        this.serverChessGame = serverChessGame;
        document = null;
    }

    public File getChessBoardImage() throws IOException {
        createSVGIfItDoesntExists();
        
        File file = createImageFile();
        
        try(OutputStream ostream = new FileOutputStream(file)){
        	generateImgFromSvg(ostream);
        } catch(TranscoderException te) {
        	logger.debug(te);
        	throw new IOException(te);
        }
        return file;

    }
    
    private void generateImgFromSvg(OutputStream ostream) throws TranscoderException, IOException {
    	JPEGTranscoder t = new JPEGTranscoder();
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));
    	TranscoderInput input = new TranscoderInput(document);
    	TranscoderOutput output = new TranscoderOutput(ostream);
    	t.transcode(input, output);
    	ostream.flush();
    	ostream.close();
    }
    
    private void createSVGIfItDoesntExists() {
    	if (document == null) {
            createSVGDocument();
        }
    }
    
    private File createImageFile() throws IOException {        
        File file = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX, new File(OUTPUT_DIR));
        logger.info("Creating SVG file:" + file.getAbsolutePath());
        return file;
    }
}
