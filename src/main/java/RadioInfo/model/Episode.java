package RadioInfo.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * The model handling episodes from the SR api
 * @version 1.0
 * @author Isidor Nygren
 */
public class Episode {
    private Integer id;
    private String title;
    private String subtitle;
    private String description;
    private Date startTimeUtc;
    private Date endTimeUtc;
    private Integer programId;
    private Integer channelId;

    private URL url;
    private URL imageUrl;
    private URL imageUrlTemplate;

    private Image image;
    private Image imageTemplate;

    private static Image template;
    private boolean isInTime = false;

    /**
     * An episode object, mainly built using the EpisodeBuilder class to avoid unnecessary implementation
     * of methods in this class. Used to parse the xml objects from the sr api.
     * @param id
     * @param title
     * @param subtitle short description
     * @param description long description
     * @param startTimeUtc date object containing the start time in UTC
     * @param endTimeUtc date object containing the end time in UTC
     * @param url The url of the episode on the sr website
     * @param programId sr's program id
     * @param channelId which channel the episode is on
     * @param imageUrl scaled down version of the image of the episode
     * @param imageUrlTemplate original image of the episode
     */
    public Episode(Integer id, String title, String subtitle, String description, Date startTimeUtc,
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

    /**
     * Sets a default image for all episode objects
     * @param path the local path to the image as an URL
     */
    public static void setTemplate(URL path) throws IOException {
        if(path != null){
            template = ImageIO.read(path);
        }else{
            template = null;
        }
    }

    /**
     * Tries to load the scaled down image and puts it in an
     * attribute of this object.
     * @throws IOException if the image can't be opened
     */
    public void loadImage() throws IOException {
        if(imageUrl != null){
            image = ImageIO.read(imageUrl);
        }
    }

    /**
     * Tries to load the original image and put it in an
     * attribute of this object.
     * @throws IOException if the image can't be opened
     */
    public void loadImageTemplate() throws IOException {
        if(imageUrlTemplate != null){
            imageTemplate = ImageIO.read(imageUrlTemplate);
        }
    }

    /**
     * Checks the if the ending time is 12 hours before the current time and if
     * the starting time is 12 hours after the current time
     * @param time the time to check with
     */
    public boolean checkTime(Date time){
        if(this.endTimeUtc.after(new Date(time.getTime() - 3600 * 1000 * 12)) &&
                this.startTimeUtc.before(new Date(time.getTime() + 3600 * 1000 * 12))){
            this.isInTime = true;
        }else{
            this.isInTime = false;
        }
        return this.isInTime;
    }

    /**
     * Returns true if the programme has already ended and false if it is going
     * to end in the future
     * @return
     */
    public boolean hasEnded(){
        return  (this.getEndTimeUtc().getTime() > new Date().getTime());
    }

    /**
     * Sets the title of the episode, used by the table model
     * to be able to update all the table data.
     * @param title the new title to update to
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets a new start time of the episode
     * @param startTimeUtc the new Date object with UTC as the timezone
     */
    public void setStartTimeUtc(Date startTimeUtc) {
        this.startTimeUtc = startTimeUtc;
    }

    /**
     * Sets a new image for the episode
     * @param image A loaded Image object
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Returns the id of the episode
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the title of the episode
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the subtitle of the episode
     * @return the subtitle
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Returns the description of the episode
     * @return the description
     */
    public String getDescription() { return description; }

    /**
     * Returns the start time of the episode in UTC
     * @return the starting time
     */
    public Date getStartTimeUtc() {
        return startTimeUtc;
    }

    /**
     * Returns the ending time of the episode in UTC
     * @return the ending time
     */
    public Date getEndTimeUtc() {
        return endTimeUtc;
    }

    /**
     * Returns the url of the show
     * @return the url of the show
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Returns the id of the program the episode is part of
     * @return the id of the program
     */
    public Integer getProgramId() {
        return programId;
    }

    /**
     * Returns the id of the channel that the episode is/was played
     * on
     * @return the id of the channel
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * Returns the scaled down image's URL
     * @return the url
     */
    public URL getImageUrl() {
        return imageUrl;
    }

    /**
     * Returns the original image's URL
     * @return the url
     */
    public URL getImageUrlTemplate() {
        return imageUrlTemplate;
    }

    /**
     * Returns the scaled down image
     * @return the image
     */
    public Image getImage() {
        if(image != null){
            return image;
        }else{
            return template;
        }
    }

    /**
     * Returns the original image
     * @return the image
     */
    public Image getImageTemplate() {
        if(imageTemplate != null){
            return imageTemplate;
        }else{
            return template;
        }
    }
}
