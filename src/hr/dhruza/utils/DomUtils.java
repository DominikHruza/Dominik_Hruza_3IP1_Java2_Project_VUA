package hr.dhruza.utils;

import hr.dhruza.model.Player;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DomUtils {

    private static final String PLAYERS = "players.xml";

    private DomUtils(){}

    public static void savePlayers(List<Player> players) {
        try {
            Document document = createDocument("players");
            players.forEach(p -> document.getDocumentElement()
                    .appendChild(createPlayerElement(p, document)));

            saveDocumnet(document, PLAYERS);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void saveActivePlayer(Player activePlayer) {
    }

    private static Document createDocument(String root) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation domImplementation = builder.getDOMImplementation();
        return domImplementation.createDocument(null, root, null);
    }

    private static Document createDocument(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }

    private static void saveDocumnet(Document document, String fileName) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(document), new StreamResult(new File(fileName)));
    }

    private static Node createPlayerElement(Player player, Document document){
        Element element = document.createElement("player");
        element.setAttributeNode(createAttribute(document, "id", String.valueOf(player.getId())));
        element.appendChild(createElement(document, "name", player.getName()));
        element.appendChild(createElement(document, "position", String.valueOf(player.getPosition())));
        return element;
    }

    private static Attr createAttribute(Document document, String name, String value) {
        Attr attr = document.createAttribute(name);
        attr.setValue(value);
        return attr;
    }

    private static Node createElement(Document document, String tagName, String data) {
        Element element = document.createElement(tagName);
        Text text = document.createTextNode(data);
        element.appendChild(text);
        return element;
    }

    public static List<Player> loadPlayers() {
        List<Player> players = new ArrayList<>();
        try {
            Document document = createDocument(new File(PLAYERS));
            NodeList nodes = document.getElementsByTagName("player");
            for (int i = 0; i < nodes.getLength(); i++) {
                players.add(processPlayerNode((Element) nodes.item(i)));
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return players;
    }

    private static Player processPlayerNode(Element item) {
        Player player = new Player();
        player.setId(Integer.valueOf(
                item.getAttribute("id")));
        player.setPosition(Integer.valueOf(
                item.getElementsByTagName("position").item(0).getTextContent()));
        player.setName(
                item.getElementsByTagName("name").item(0).getTextContent());

        return player;
    }
}
