package RadioInfo.model;

import java.net.URL;

public class ChannelBuilder {
    private String name;
    private Integer id;
    private String color;
    private String tagline;
    private URL imageUrl;
    private URL siteUrl;
    private URL scheduleUrl;
    private String channelType;

    public ChannelBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ChannelBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public ChannelBuilder setColor(String color) {
        this.color = color;
        return this;
    }

    public ChannelBuilder setTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public ChannelBuilder setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ChannelBuilder setSiteUrl(URL siteUrl) {
        this.siteUrl = siteUrl;
        return this;
    }

    public ChannelBuilder setScheduleUrl(URL scheduleUrl) {
        this.scheduleUrl = scheduleUrl;
        return this;
    }

    public ChannelBuilder setChannelType(String channelType) {
        this.channelType = channelType;
        return this;
    }

    public Channel createChannelObject() {
        return new Channel(name, id, color, tagline, imageUrl, siteUrl, scheduleUrl, channelType);
    }
}