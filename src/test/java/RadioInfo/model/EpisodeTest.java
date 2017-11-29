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

public class EpisodeTest {

    Episode episode;
    Date startTime;
    Date endTime;

    @Before
    public void setUp() throws Exception {
        EpisodeBuilder episodeBuilder = new EpisodeBuilder();
        startTime = new Date();
        endTime = new Date();
        endTime.setTime(startTime.getTime() + 1000);
        episode = episodeBuilder
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
        Episode.setTemplate(null);
        episode = null;
    }
    @Test
    public void setTemplate() throws Exception {
        Episode.setTemplate(new File("src/test/resources/images/template.png").toURI().toURL());
        assertNotNull(episode.getImage());
    }

    @Test
    public void setTitle() throws Exception {
        episode.setTitle("New title");
        assertEquals(episode.getTitle(), "New title");
    }

    @Test
    public void setStartTimeUtc() throws Exception {
        Date date = new Date();
        episode.setStartTimeUtc(date);
        assertEquals(episode.getStartTimeUtc(), date);
    }

    @Test
    public void setImage() throws Exception {
        Image oldImage = episode.getImage();
        episode.setImage(ImageIO.read(new File("src/test/resources/images/template.png").toURI().toURL()));
        assertNotEquals(episode.getImage(),oldImage);
    }

    @Test
    public void getDescription() throws Exception {
        assertEquals(episode.getDescription(), "Description");
    }

    @Test
    public void getImage() throws Exception {
        assertNull(episode.getImage());
    }

    @Test
    public void getImageTemplate() throws Exception {
        assertNull(episode.getImageTemplate());
    }

    @Test
    public void loadImage() throws Exception {
        episode.loadImage();
        assertNotNull(episode.getImage());
    }

    @Test
    public void loadImageTemplate() throws Exception {
        episode.loadImageTemplate();
        assertNotNull(episode.getImageTemplate());
    }

    @Test
    public void getId() throws Exception {
        assertTrue(episode.getId() == 1);
    }

    @Test
    public void getTitle() throws Exception {
        assertEquals(episode.getTitle(), "Episode title");
    }

    @Test
    public void getSubtitle() throws Exception {
        assertEquals(episode.getSubtitle(), "Subtitle");
    }

    @Test
    public void getStartTimeUtc() throws Exception {
        assertEquals(episode.getStartTimeUtc(), startTime);
    }

    @Test
    public void getEndTimeUtc() throws Exception {
        assertEquals(episode.getEndTimeUtc(), endTime);
    }

    @Test
    public void getUrl() throws Exception {
        assertEquals(episode.getUrl(), new URL("http://www.google.com/"));
    }

    @Test
    public void getProgramId() throws Exception {
        assertTrue(episode.getProgramId() == 2);
    }

    @Test
    public void getChannelId() throws Exception {
        assertTrue(episode.getChannelId() == 3);
    }

    @Test
    public void getImageUrl() throws Exception {
        assertEquals(episode.getImageUrl(), new File("src/test/resources/images/template.png").toURI().toURL());
    }

    @Test
    public void getImageUrlTemplate() throws Exception {
        assertEquals(episode.getImageUrlTemplate(), new File("src/test/resources/images/template.png").toURI().toURL());
    }

}