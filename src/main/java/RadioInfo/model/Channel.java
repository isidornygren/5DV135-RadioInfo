package RadioInfo.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
/**
 * The model handling channels from the SR api
 * @version 1.0
 * @author Isidor Nygren
 */
public class Channel {
    Integer id;
    String name;
    String color;
    String tagline;
    String channelType;
    URL imageUrl;
    URL siteUrl;
    URL scheduleUrl;
    private Image image;

    /**
     * A channel object based on the SR API. contains information about
     * the visual representation of a channel as well as links to the channel on the main page
     * as well as a url for the schedule.
     * @param name the name of the channel
     * @param id the id of the channel
     * @param color the main  background color of the channel
     * @param tagline the tagline of the channel
     * @param imageUrl Logotype
     * @param siteUrl link to the site containing the info of the channel
     * @param scheduleUrl url to the schedule of the channel
     * @param channelType type of the channel, e.g. if it's local or international
     */
    public Channel(String name, Integer id, String color, String tagline, URL imageUrl, URL siteUrl, URL scheduleUrl, String channelType) {
        this.name = name;
        this.id = id;
        this.color = color;
        this.tagline = tagline;
        this.siteUrl = siteUrl;
        this.imageUrl = imageUrl;
        this.scheduleUrl = scheduleUrl;
        this.channelType = channelType;
    }

    /**
     * Loads the imageurl (icon) of the channel
     * @throws IOException if the url of the image can't be successfully read
     */
    public void loadImage() throws IOException {
        image = ImageIO.read(imageUrl);
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getTagline() {
        return tagline;
    }

    public String getChannelType() {
        return channelType;
    }

    public URL getSiteUrl() {
        return siteUrl;
    }

    public URL getScheduleUrl() {
        return scheduleUrl;
    }

    public Image getImage() {
        return image;
    }

    public URL getImageUrl() { return imageUrl;}
}
