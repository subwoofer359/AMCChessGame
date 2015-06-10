package org.amc.game.chessserver.messaging;

import org.amc.game.chessserver.JsonChessGameView;
import org.amc.game.chessserver.ServerChessGame;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ChessBoardSVGImage {

    private static final Logger logger = Logger.getLogger(ChessBoardSVGImage.class);

    private ServletContext servletContext;

    private ServerChessGame serverChessGame;

    private ScriptEngine jsEngine;
    private DocumentBuilder dBuilder;

    private static final String JS_FUNCTION_EMPTY_CHESSBOARD = "createBlankChessBoardSVG";
    private static final String JS_FUNCTION_CHESSPIECES = "createChessPiecesElements";
    private static final String SVG_NODE_G = "g";
    private static final String BINDING_DOCUMENT = "document";
    private static final String BINDING_WINDOW = "window";
    private static final String OUTPUT_FILE="out.jpg";

    private static final List<String> SCRIPTS_TO_EVAL;
    static {
        SCRIPTS_TO_EVAL = Collections.unmodifiableList(Arrays.asList("js/chesspieces.js",
                        "js/chessboard.js"));
    }

    public ChessBoardSVGImage() throws ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setValidating(false);
        this.dBuilder = dbFactory.newDocumentBuilder();
    }

    public ChessBoardSVGImage(ServerChessGame serverChessGame)
                    throws ParserConfigurationException {
        this();
        this.serverChessGame = serverChessGame;
        
    }
    
    public void setServerChessGame(ServerChessGame serverChessGame) {
        this.serverChessGame = serverChessGame;
    }

    public String getChessBoardImage() throws Exception {
        JPEGTranscoder t = new JPEGTranscoder();
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
                        new Float(.8));
        
        StringReader reader = new StringReader(getChessBoardSVG());
        TranscoderInput input = new TranscoderInput(reader);
        OutputStream ostream = new FileOutputStream(OUTPUT_FILE);
        TranscoderOutput output = new TranscoderOutput(ostream);
        t.transcode(input, output);
        ostream.flush();
        ostream.close();
        return OUTPUT_FILE;

    }

    private String getChessBoardSVG() throws Exception {
        loadJSScripts();

        Document document = dBuilder.newDocument();

        document.setXmlStandalone(true);

        setScriptEngineBindings(document);

        Invocable in = (Invocable) jsEngine;

        Element svgRoot = (Element) in.invokeFunction(JS_FUNCTION_EMPTY_CHESSBOARD, "");

        Element chessPieces = getChessPiecesElement(in);

        mergeDocuments(document, svgRoot, chessPieces);

        return svgDomToString(document);
    }

    private void loadJSScripts() throws FileNotFoundException, ScriptException {
        for (String script : SCRIPTS_TO_EVAL) {
            jsEngine.eval(new InputStreamReader(servletContext.getResourceAsStream(script)));
        }
    }

    private Element getChessPiecesElement(Invocable in) throws Exception {
        String chessPieces = (String) in.invokeFunction(JS_FUNCTION_CHESSPIECES,
                        this.serverChessGame.getOpponent(),
                        JsonChessGameView.convertChessGameToJson(serverChessGame.getChessGame()));
        chessPieces = String.format("<g id=\"layer2\">%s</g>", chessPieces);
        return (Element) getDOMDocument(chessPieces).getFirstChild();
    }

    private void mergeDocuments(Document document, Element svgRoot, Element chessPieces)
                    throws SAXException, ParserConfigurationException, IOException {
        document.appendChild(svgRoot);

        Element rootGNode = getRootGNode(document);

        Node pieces = document.importNode(chessPieces, true);
        rootGNode.appendChild(pieces);
    }

    private Element getRootGNode(Document document) {
        NodeList list = document.getElementsByTagName(SVG_NODE_G);
        return (Element) list.item(0);
    }

    private Document getDOMDocument(String xml) throws ParserConfigurationException, IOException,
                    SAXException {
        InputSource strInput = new InputSource(new StringReader(xml));
        return dBuilder.parse(strInput);
    }
    
    private String svgDomToString(Document document) {
        StringWriter writer = new StringWriter();
        Source input = new DOMSource(document);
        Result output = new StreamResult(writer);
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(input, output);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
        return writer.toString();
    }

    private void setScriptEngineBindings(Document document) {
        Bindings engineScope = jsEngine.getBindings(ScriptContext.ENGINE_SCOPE);
        engineScope.put(BINDING_WINDOW, engineScope);
        engineScope.put(BINDING_DOCUMENT, document);
    }

    @Autowired
    public void setScriptEngine(ScriptEngine engine) {
        this.jsEngine = engine;
    }

    @Autowired
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
