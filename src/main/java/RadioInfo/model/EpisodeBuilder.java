package RadioInfo.model;

import java.net.URL;
import java.util.Date;

public class EpisodeBuilder {
    private Integer id;
    private String title;
    private String subtitle;
    private String description;
    private Date startTimeUtc;
    private Date endTimeUtc;
    private URL url;
    private Integer programId;
    private Integer channelId;
    private URL imageUrl;
    private URL imageUrlTemplate;

    public EpisodeBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public EpisodeBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public EpisodeBuilder setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public EpisodeBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public EpisodeBuilder setStartTimeUtc(Date startTimeUtc) {
        this.startTimeUtc = startTimeUtc;
        return this;
    }

    public EpisodeBuilder setEndTimeUtc(Date endTimeUtc) {
        this.endTimeUtc = endTimeUtc;
        return this;
    }

    public EpisodeBuilder setUrl(URL url) {
        this.url = url;
        return this;
    }

    public EpisodeBuilder setProgramId(Integer programId) {
        this.programId = programId;
        return this;
    }

    public EpisodeBuilder setChannelId(Integer channelId) {
        this.channelId = channelId;
        return this;
    }

    public EpisodeBuilder setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public EpisodeBuilder setImageUrlTemplate(URL imageUrlTemplate) {
        this.imageUrlTemplate = imageUrlTemplate;
        return this;
    }

    public Episode createEpisodeObject() {
        return new Episode(id, title, subtitle, description, startTimeUtc, endTimeUtc, url, programId, channelId, imageUrl, imageUrlTemplate);
    }
}