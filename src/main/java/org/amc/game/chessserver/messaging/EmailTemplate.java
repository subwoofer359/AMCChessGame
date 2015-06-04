package org.amc.game.chessserver.messaging;

import com.google.gson.Gson;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.JsonChessGameView.JsonChessGame;
import org.amc.game.chessserver.ServerChessGame;
import org.apache.log4j.Logger;
import org.apache.xerces.dom.ElementNSImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Locale;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class EmailTemplate {

    private static final Logger logger = Logger.getLogger(EmailTemplate.class);
    
    private SpringTemplateEngine templateEngine;
    
    private final ServerChessGame serverChessGame;
    
    private final Player otherPlayer;
    
    private ScriptEngine jsEngine;
    
    public EmailTemplate(Player player, ServerChessGame serverChessGame) {
        this.serverChessGame = serverChessGame;
        this.otherPlayer = player;
    }
    
    public String getEmailHtml() {
        final Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("name", otherPlayer.getName());
        ctx.setVariable("status", serverChessGame.getCurrentStatus().toString());  
        try{
            ctx.setVariable("svg", getChessBoardImage());
        }
        catch(Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        
        return this.templateEngine.process("gameStatus.html", ctx);
    }
    
    private String getChessBoardImage() throws Exception {
        jsEngine.eval(new FileReader("src/main/webapp/js/chesspieces.js"));
        jsEngine.eval(new FileReader("src/main/webapp/js/chessboard.js"));
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setValidating(false);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        Document document = dBuilder.newDocument();
        
        Bindings engineScope = jsEngine.getBindings(ScriptContext.ENGINE_SCOPE);
        engineScope.put("window", engineScope);
        engineScope.put("document", document);
        
        Invocable in = (Invocable) jsEngine;
        
        ElementNSImpl result = (ElementNSImpl)in.invokeFunction("createBlankChessBoardSVG", "");
        
        JsonChessGame jsChessGame = new JsonChessGame(this.serverChessGame.getChessGame());
        Gson gson = new Gson();
        
        String chessPieces = (String)in.invokeFunction("createChessPiecesElements", this.serverChessGame.getOpponent(), gson.toJson(jsChessGame));
        chessPieces = "<g id=\"layer2\">" + chessPieces + "</g>";
        
        InputSource strInput = new InputSource(new StringReader(chessPieces));
        
        Document chessPiecesDoc = dBuilder.parse(strInput);
        document.appendChild(result);
        
        NodeList list = document.getElementsByTagName("g");
        Element element = (Element) list.item(0);
        
        Node pieces = document.importNode(chessPiecesDoc.getFirstChild(), true);
        element.appendChild(pieces);
        
       
       
        
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        StringWriter writer =new StringWriter();
        Source input = new DOMSource(document);
        Result output = new StreamResult(writer);
        transformer.transform(input, output);
        return writer.toString();
    }
    
    @Autowired
    public void setTemplateEngine(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    
    @Autowired
    public void setScriptEngine(ScriptEngine engine) {
        this.jsEngine = engine;
    }
}
