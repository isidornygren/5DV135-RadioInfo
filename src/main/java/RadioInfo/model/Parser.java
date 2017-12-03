package RadioInfo.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
    protected final String apiUrl = "http://api.sr.se/api/v2";
    /**
     * returns an arraylist of all errors that was found during parsing of the API
     * @return an arraylist of all the ParsingError objects
     */
    public ArrayList<ParsingError> getErrors(){
        return errors;
    }

    /**
     * Returns if the parser has encountered at least one error
     * @return true if at least one error has been encountered
     */
    public boolean hasErrors(){
        return (errors.size() > 0);
    }

    /**
     * Parses an XML inputstream and returns it into a normalized document
     * @param inputStream the inputstream to parse
     * @return A normalized Document
     */
    protected Document parseInputStream(InputStream inputStream) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            // Override documentbuilders error handlers to not allow fatal errors
            dBuilder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {}

                @Override
                public void error(SAXParseException exception) throws SAXException {}

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {}
            });
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (ParserConfigurationException e) {
            errors.add(new ParsingError("Error configuring Parser", e));
        } catch (IOException e){
            errors.add(new ParsingError("Error opening stream", e));
        } catch (SAXException e){
            errors.add(new ParsingError("Error parsing XML", e));
        }catch (NullPointerException e){
            errors.add(new ParsingError("Error parsing XML, could not find element", e));
        }
        return null;
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
