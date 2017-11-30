package RadioInfo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class SRParserTest {
    private SRParser parser;
    @Before
    public void setUp() throws Exception {
        //2017-11-28T23:00:00Z
        Calendar cal = Calendar.getInstance();
        cal.set(2017, Calendar.NOVEMBER, 29, 12, 00, 00);
        Date theDate = cal.getTime();
        parser = new SRParser("http://api.sr.se/api/v2", theDate);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parseChannels() throws Exception {
        ArrayList<Channel> channels = parser.parseChannels(
                new FileInputStream(new File("src/test/resources/xml/channels.xml")));
        assertTrue(channels.size() == 10);
    }

    @Test
    public void parseChannel() throws Exception {
        Channel channel = parser.parseChannel(
                new FileInputStream(new File("src/test/resources/xml/132.xml")));
        assertTrue(channel.getId() == 132);
    }

    @Test
    public void parseSchedule() throws Exception {
        ArrayList<Episode> episodes = parser.parseSchedule(
                new FileInputStream(new File("src/test/resources/xml/scheduledepisodes.xml")));
        assertEquals(87, episodes.size());
    }
    @Test
    public void buildChannelUrl() throws Exception {
        URL url = parser.buildChannelUrl();
        assertEquals("http://api.sr.se/api/v2/channels?pagination=false", url.toString());
    }

    @Test
    public void buildChannelUrlWithId() throws Exception {
        URL url = parser.buildChannelUrl(132);
        assertEquals("http://api.sr.se/api/v2/channels/132", url.toString());
    }

    @Test
    public void buildScheduleUrl() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2012, Calendar.DECEMBER, 23, 06, 06, 06);
        Date theDate = cal.getTime();
        URL url = parser.buildScheduleUrl(132, theDate);
        assertEquals("http://api.sr.se/api/v2/scheduledepisodes?channelid=132&date=2012-12-23&pagination=false", url.toString());
    }

    @Test
    public void getErrors() throws Exception {
        parser.parseChannels(
                new FileInputStream(new File("src/test/resources/xml/error.xml")));
        assertEquals(1, parser.getErrors().size());
    }

}