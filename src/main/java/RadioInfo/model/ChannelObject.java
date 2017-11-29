package RadioInfo.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class ChannelObject {
    Integer id;
    String name;
    String color;
    String tagline;
    String channelType;
    URL imageUrl;
    URL siteUrl;
    URL scheduleUrl;
    private Image image;

    public ChannelObject(){}

    public ChannelObject(String name, Integer id, String color, String tagline, URL imageUrl, URL siteUrl, URL scheduleUrl, String channelType) {
        this.name = name;
        this.id = id;
        this.color = color;
        this.tagline = tagline;
        this.siteUrl = siteUrl;
        this.imageUrl = imageUrl;
        this.scheduleUrl = scheduleUrl;
        this.channelType = channelType;
    }

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
