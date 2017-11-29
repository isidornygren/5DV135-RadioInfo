package RadioInfo.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class EpisodeObject {
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

    private Image image;
    private Image imageTemplate;

    private static Image template;

    public EpisodeObject(Integer id, String title, String subtitle, String description, Date startTimeUtc,
                         Date endTimeUtc, URL url, Integer programId, Integer channelId,
                         URL imageUrl, URL imageUrlTemplate){
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.startTimeUtc = startTimeUtc;
        this.endTimeUtc = endTimeUtc;
        this.url = url;
        this.programId = programId;
        this.channelId = channelId;
        this.imageUrl = imageUrl;
        this.imageUrlTemplate = imageUrlTemplate;
    }

    public static void setTemplate(URL path){
        try{
            if(path != null){
                template = ImageIO.read(path);
            }else{
                template = null;
            }
        }catch(IOException e){
            e.printStackTrace(); //TODO add error handling
        }
    }

    public void loadImage() throws IOException {
        if(imageUrl != null){
            image = ImageIO.read(imageUrl);
        }
    }

    public void loadImageTemplate() throws IOException {
        if(imageUrlTemplate != null){
            imageTemplate = ImageIO.read(imageUrlTemplate);
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartTimeUtc(Date startTimeUtc) {
        this.startTimeUtc = startTimeUtc;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() { return description; }

    public Date getStartTimeUtc() {
        return startTimeUtc;
    }

    public Date getEndTimeUtc() {
        return endTimeUtc;
    }

    public URL getUrl() {
        return url;
    }

    public Integer getProgramId() {
        return programId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public URL getImageUrlTemplate() {
        return imageUrlTemplate;
    }

    public Image getImage() {
        if(image != null){
            return image;
        }else{
            return template;
        }
    }

    public Image getImageTemplate() {
        if(imageTemplate != null){
            return imageTemplate;
        }else{
            return template;
        }
    }
}
