package RadioInfo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
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
        episodeObject = episodeObjectBuilder
                .setImageUrl(new File("src/test/resources/images/template.png").toURI().toURL())
                .setTitle("Episode title")
                .setChannelId(3)
                .setStartTimeUtc(startTime)
                .setEndTimeUtc(endTime)
                .setId(1)
                .setImageUrlTemplate(new File("src/test/resources/images/template.png").toURI().toURL())
                .setProgramId(2)
                .setSubtitle("Subtitle")
                .setDescription("Description")
                .setUrl(new URL("http://www.google.com/"))
                .createEpisodeObject();
    }

    @After
    public void tearDown() throws Exception {
        EpisodeObject.setTemplate(null);
        episodeObject = null;
    }
    @Test
    public void setTemplate() throws Exception {
        EpisodeObject.setTemplate(new File("src/test/resources/images/template.png").toURI().toURL());
        assertNotNull(episodeObject.getImage());
    }

    @Test
    public void setTitle() throws Exception {
        episodeObject.setTitle("New title");
        assertEquals(episodeObject.getTitle(), "New title");
    }

    @Test
    public void setStartTimeUtc() throws Exception {
        Date date = new Date();
        episodeObject.setStartTimeUtc(date);
        assertEquals(episodeObject.getStartTimeUtc(), date);
    }

    @Test
    public void setImage() throws Exception {
        Image oldImage = episodeObject.getImage();
        episodeObject.setImage(ImageIO.read(new File("src/test/resources/images/template.png").toURI().toURL()));
        assertNotEquals(episodeObject.getImage(),oldImage);
    }

    @Test
    public void getDescription() throws Exception {
        assertEquals(episodeObject.getDescription(), "Description");
    }

    @Test
    public void getImage() throws Exception {
        assertNull(episodeObject.getImage());
    }

    @Test
    public void getImageTemplate() throws Exception {
        assertNull(episodeObject.getImageTemplate());
    }

    @Test
    public void loadImage() throws Exception {
        episodeObject.loadImage();
        assertNotNull(episodeObject.getImage());
    }

    @Test
    public void loadImageTemplate() throws Exception {
        episodeObject.loadImageTemplate();
        assertNotNull(episodeObject.getImageTemplate());
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
        assertEquals(episodeObject.getImageUrl(), new File("src/test/resources/images/template.png").toURI().toURL());
    }

    @Test
    public void getImageUrlTemplate() throws Exception {
        assertEquals(episodeObject.getImageUrlTemplate(), new File("src/test/resources/images/template.png").toURI().toURL());
    }

}