package RadioInfo.model;

import java.net.URL;

public class ChannelObjectBuilder {
    private String name;
    private Integer id;
    private String color;
    private String tagline;
    private URL imageUrl;
    private URL siteUrl;
    private URL scheduleUrl;
    private String channelType;

    public ChannelObjectBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ChannelObjectBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public ChannelObjectBuilder setColor(String color) {
        this.color = color;
        return this;
    }

    public ChannelObjectBuilder setTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public ChannelObjectBuilder setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ChannelObjectBuilder setSiteUrl(URL siteUrl) {
        this.siteUrl = siteUrl;
        return this;
    }

    public ChannelObjectBuilder setScheduleUrl(URL scheduleUrl) {
        this.scheduleUrl = scheduleUrl;
        return this;
    }

    public ChannelObjectBuilder setChannelType(String channelType) {
        this.channelType = channelType;
        return this;
    }

    public ChannelObject createChannelObject() {
        return new ChannelObject(name, id, color, tagline, imageUrl, siteUrl, scheduleUrl, channelType);
    }
}