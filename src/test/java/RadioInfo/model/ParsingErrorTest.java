package RadioInfo.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParsingErrorTest {
    private ParsingError error;
    @Before
    public void setUp() throws Exception {
        error = new ParsingError("error");
    }

    @Test
    public void buildString() throws Exception {
        error = null;
    }

    @Test
    public void buildWithoutException() throws Exception {
        assertFalse(error.isException());
    }

    @Test
    public void buildWithException() throws Exception {
        error = new ParsingError("error", new Exception());
        assertTrue(error.isException());
    }

    @Test
    public void returnsString() throws Exception {
        assertEquals(error.toString(), "error");
    }

}