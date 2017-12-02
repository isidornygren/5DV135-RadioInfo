package RadioInfo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() throws Exception {
        parser = new Parser();
    }

    @After
    public void tearDown() throws Exception {
        parser = null;
    }
    @Test
    public void parseInputStream() throws Exception {
        Document doc = parser.parseInputStream(new FileInputStream(new File("src/test/resources/xml/132.xml")));
        assertEquals(1,doc.getElementsByTagName("copyright").getLength());
    }
    @Test
    public void hasNoErrors() throws Exception {
        parser.parseInputStream(new FileInputStream(new File("src/test/resources/xml/132.xml")));
        assertFalse(parser.hasErrors());
    }
    @Test
    public void hasErrors() throws Exception {
        parser.parseInputStream(new FileInputStream(new File("src/test/resources/xml/error.xml")));
        assertTrue(parser.hasErrors());
    }
    @Test
    public void getErrors() throws Exception {
        parser.parseInputStream(new FileInputStream(new File("src/test/resources/xml/error.xml")));
        assertEquals(parser.getErrors().size(), 1);
    }

    @Test
    public void getElementValue() throws Exception {
        Document doc = parser.parseInputStream(new FileInputStream(new File("src/test/resources/xml/132.xml")));
        String value = parser.getElementValue((Element)doc.getFirstChild(), "copyright");
        assertEquals("Copyright Sveriges Radio 2017. All rights reserved.", value);
    }

}