package RadioInfo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.*;

public class ChannelObjectTest {
    private ChannelObject channelObject;

        /*      private String name;
            private Integer id;
            private String color;
            private String tagline;
            private URL imageUrl;
            private URL siteUrl;
            private URL scheduleUrl;
            private String channelType;
    */

    @Before
    public void setUp() throws Exception {
        ChannelObjectBuilder channelObjectBuilder = new ChannelObjectBuilder();
        channelObject = channelObjectBuilder
                .setName("Channel1")
                .setId(1)
                .setColor("000000")
                .setTagline("This is the tagline")
                .setImageUrl(new File("src/test/resources/images/template.png").toURI().toURL())
                .setSiteUrl(new URL("http://www.google.com/"))
                .setScheduleUrl(new URL("http://www.google.com/"))
                .setChannelType("Rikskanal")
                .createChannelObject();
    }

    @After
    public void tearDown() throws Exception {
        channelObject = null;
    }

    @Test
    public void loadImage() throws Exception {
        channelObject.loadImage();
        assertNotNull(channelObject.getImage());
    }

    @Test
    public void getName() throws Exception {
        assertEquals(channelObject.getName(), "Channel1");
    }

    @Test
    public void getId() throws Exception {
        assertTrue(channelObject.getId() == 1);
    }

    @Test
    public void getColor() throws Exception {
        assertEquals(channelObject.getColor(), "000000");
    }

    @Test
    public void getTagline() throws Exception {
        assertEquals(channelObject.getTagline(), "This is the tagline");
    }

    @Test
    public void getChannelType() throws Exception {
        assertEquals(channelObject.getChannelType(), "Rikskanal");
    }

    @Test
    public void getSiteUrl() throws Exception {
        assertEquals(channelObject.getSiteUrl().toString(), "http://www.google.com/");
    }

    @Test
    public void getScheduleUrl() throws Exception {
        assertEquals(channelObject.getScheduleUrl().toString(), "http://www.google.com/");
    }

    @Test
    public void getImage() throws Exception {
        assertNull(channelObject.getImage());
    }

    @Test
    public void getImageUrl() throws Exception {
        assertEquals(channelObject.getImageUrl(),new File("src/test/resources/images/template.png").toURI().toURL());
    }

}