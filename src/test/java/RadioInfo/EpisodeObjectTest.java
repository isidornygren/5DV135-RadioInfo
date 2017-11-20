package RadioInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.*;

public class EpisodeObjectTest {
    EpisodeObject episodeObject;
    Date startTime;
    Date endTime;

    @Before
    public void setUp() throws Exception {
        EpisodeObjectBuilder episodeObjectBuilder = new EpisodeObjectBuilder();
        startTime = new Date();
        endTime = new Date();
        endTime.setTime(startTime.getTime() + 1000);
        episodeObject = episodeObjectBuilder.setImageUrl(new URL("http://image.flaticon.com/icons/png/128/291/291201.png"))
                .setTitle("Episode title").setChannelId(3).setStartTimeUtc(startTime).setEndTimeUtc(endTime).setId(1).setImageUrlTemplate(new URL("http://image.flaticon.com/icons/png/128/291/291201.png"))
                .setProgramId(2).setSubtitle("Subtitle").setUrl(new URL("http://www.google.com/")).createEpisodeObject();
    }

    @After
    public void tearDown() throws Exception {
        episodeObject = null;
    }

    @Test
    public void loadImage() throws Exception {
        try{
            episodeObject.loadImage();
            assertNotNull(episodeObject.getImage());
        }catch(IOException e){

        }
    }

    @Test
    public void loadImageTemplate() throws Exception {
        try{
            episodeObject.loadImageTemplate();
            assertNotNull(episodeObject.getImageTemplate());
        }catch(IOException e){

        }
    }

    @Test
    public void getId() throws Exception {
        assertTrue(episodeObject.getId() == 1);
    }

    @Test
    public void getTitle() throws Exception {
        assertEquals(episodeObject.getTitle(), "Episode title");
    }

    @Test
    public void getSubtitle() throws Exception {
        assertEquals(episodeObject.getSubtitle(), "Subtitle");
    }

    @Test
    public void getStartTimeUtc() throws Exception {
        assertEquals(episodeObject.getStartTimeUtc(), startTime);
    }

    @Test
    public void getEndTimeUtc() throws Exception {
        assertEquals(episodeObject.getEndTimeUtc(), endTime);
    }

    @Test
    public void getUrl() throws Exception {
        assertEquals(episodeObject.getUrl(), new URL("http://www.google.com/"));
    }

    @Test
    public void getProgramId() throws Exception {
        assertTrue(episodeObject.getProgramId() == 2);
    }

    @Test
    public void getChannelId() throws Exception {
        assertTrue(episodeObject.getChannelId() == 3);
    }

    @Test
    public void getImageUrl() throws Exception {
        assertEquals(episodeObject.getImageUrl(), new URL("http://image.flaticon.com/icons/png/128/291/291201.png"));
    }

    @Test
    public void getImageUrlTemplate() throws Exception {
        assertEquals(episodeObject.getImageUrlTemplate(), new URL("http://image.flaticon.com/icons/png/128/291/291201.png"));
    }

}