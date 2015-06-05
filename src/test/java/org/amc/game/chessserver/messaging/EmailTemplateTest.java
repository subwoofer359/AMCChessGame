package org.amc.game.chessserver.messaging;

import static org.junit.Assert.*;

import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class EmailTemplateTest {

    private EmailTemplate template;
    private SpringTemplateEngine templateEngine;
    private Player player;
    private ServerChessGame scg;
    private ScriptEngine engine;
    private final long GAME_UID = 20202l;
    private FileTemplateResolver emailTemplateResolver;
    private static final Logger logger = Logger.getLogger(EmailTemplateTest.class);
    
    
    @Before
    public void setUp() throws Exception {
        player = new HumanPlayer("Adrian McLaughlin");
        scg = new ServerChessGame(GAME_UID, player);
        scg.addOpponent(new HumanPlayer("Player 2"));
        template = new EmailTemplate(player, scg);
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        
        
        emailTemplateResolver = new FileTemplateResolver();
        emailTemplateResolver.setPrefix("src/main/webapp/mail/");
        emailTemplateResolver.setTemplateMode("HTML5");
        emailTemplateResolver.setCharacterEncoding("UTF-8");
        
        templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(emailTemplateResolver);
        
        emailTemplateResolver.initialize();
        templateEngine.initialize();
     
        template.setScriptEngine(engine);
        template.setTemplateEngine(templateEngine);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        String html = template.getEmailHtml();
        
        String svg = getSVGString(html);
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        InputSource input = new InputSource(new StringReader(svg));
        Document document = dBuilder.parse(input);
        assertNotNull(document);
        NodeList list = document.getElementsByTagName("g");
        
    }
    
    private String getSVGString(String html) {
        String startTag = "<svg";
        String endTag = "</svg>";
        
        int start = html.indexOf(startTag);
        int end = html.indexOf(endTag);
        return html.substring(start, end + endTag.length());
    }
    
    @Test
    public void testSVG() throws Exception {
        String html = template.getEmailHtml();
        
        String svg = getSVGString(html);
        svg = "\n<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n" + svg;
        
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        f.setValidating(true);
        StringReader reader =new StringReader(svg);
        Document doc = f.createDocument("file:", reader);
        assertNotNull(doc);
    }

}
