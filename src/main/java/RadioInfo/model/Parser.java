package RadioInfo.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Superclass containing general xml and api parsing methods
 * @version 1.0
 * @author Isidor Nygren
 */
public class Parser {
    protected ArrayList<ParsingError> errors = new ArrayList<>();
    protected final String apiUrl = "http://api.sr.se/api/v2/";
    /**
     * returns an arraylist of all errors that was found during parsing of the API
     * @return an arraylist of all the ParsingError objects
     */
    public ArrayList<ParsingError> getErrors(){
        return errors;
    }

    /**
     * Parses an XML inputstream and returns it into a normalized document
     * @param inputStream the inputstream to parse
     * @return A normalized Document
     * @throws ParserConfigurationException it there is an inherit error in the Parser
     * @throws IOException if there was an error parsing the stream
     * @throws SAXException if there was an error parsing the xml
     */
    protected Document parseInputStream(InputStream inputStream) throws
            ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputStream);
        doc.getDocumentElement().normalize();
        return doc;
    }

    /**
     * Returns the value (text context) of en XML element
     * @param element the element to check for the text context
     * @param tagName the tag inside the element
     * @return the string that was inside the tag
     */
    protected String getElementValue(Element element, String tagName){
        String ret = null;
        if(element.getElementsByTagName(tagName).getLength() > 0){
            ret = element.getElementsByTagName(tagName).item(0).getTextContent();
        }
        return ret;
    }
}
