package RadioInfo.model;

import java.net.URL;
import java.util.Date;

public class EpisodeObjectBuilder {
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

    public EpisodeObjectBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public EpisodeObjectBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public EpisodeObjectBuilder setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public EpisodeObjectBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public EpisodeObjectBuilder setStartTimeUtc(Date startTimeUtc) {
        this.startTimeUtc = startTimeUtc;
        return this;
    }

    public EpisodeObjectBuilder setEndTimeUtc(Date endTimeUtc) {
        this.endTimeUtc = endTimeUtc;
        return this;
    }

    public EpisodeObjectBuilder setUrl(URL url) {
        this.url = url;
        return this;
    }

    public EpisodeObjectBuilder setProgramId(Integer programId) {
        this.programId = programId;
        return this;
    }

    public EpisodeObjectBuilder setChannelId(Integer channelId) {
        this.channelId = channelId;
        return this;
    }

    public EpisodeObjectBuilder setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public EpisodeObjectBuilder setImageUrlTemplate(URL imageUrlTemplate) {
        this.imageUrlTemplate = imageUrlTemplate;
        return this;
    }

    public EpisodeObject createEpisodeObject() {
        return new EpisodeObject(id, title, subtitle, description, startTimeUtc, endTimeUtc, url, programId, channelId, imageUrl, imageUrlTemplate);
    }
}