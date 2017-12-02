package RadioInfo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class ScheduleParserTest {
    private ScheduleParser scheduleParser;
    @Before
    public void setUp() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2017, Calendar.NOVEMBER, 29, 12, 00, 00);
        Date date = cal.getTime();
        scheduleParser = new ScheduleParser(132, date);
    }

    @After
    public void tearDown() throws Exception {
        scheduleParser = null;
    }

    @Test
    public void parseSchedule() throws Exception {
        scheduleParser.parseSchedule(new FileInputStream(new File("src/test/resources/xml/scheduledepisodes.xml")));
        ArrayList<Episode> episodes = scheduleParser.getEpisodes();
        assertEquals(85, episodes.size());
    }

}