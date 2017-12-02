package RadioInfo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChannelParserTest {
    ChannelParser channelParser;
    @Before
    public void setUp() throws Exception {
        channelParser = new ChannelParser();
    }

    @After
    public void tearDown() throws Exception {
        channelParser = null;
    }

    @Test
    public void parseChannels() throws Exception {
        channelParser.parseChannels(
                new FileInputStream(new File("src/test/resources/xml/channels.xml")));
        ArrayList<Channel> channels = channelParser.getChannels();
        assertTrue(channels.size() == 10);
    }

    @Test
    public void parseChannel() throws Exception {
        Channel channel = channelParser.parseChannel(
                new FileInputStream(new File("src/test/resources/xml/132.xml")));
        assertTrue(channel.getId() == 132);
    }

    @Test
    public void buildChannelUrl() throws Exception {
        URL url = channelParser.buildChannelUrl();
        assertEquals("http://api.sr.se/api/v2/channels?pagination=false", url.toString());
    }

    @Test
    public void buildChannelUrlWithId() throws Exception {
        URL url = channelParser.buildChannelUrl(132);
        assertEquals("http://api.sr.se/api/v2/channels/132", url.toString());
    }

}