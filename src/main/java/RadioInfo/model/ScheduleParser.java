package RadioInfo.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * Handles the parsing of the schedules from the sr API
 * @version 1.0
 * @author Isidor Nygren
 */
public class ScheduleParser extends Parser {
    private DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    ArrayList<Episode> episodes = new ArrayList<>();
    private Date date;
    private String apiScheduleUrl = "scheduledepisodes";
    private Integer channelId;

    /**
     * Parses a schedule for a specific channel for a specific date
     * @param channelId the channel to parse the schedule for
     * @param date the date -12/+12 hours to get the schedule for
     */
    public ScheduleParser(Integer channelId, Date date){
        this.date = date;
        this.channelId = channelId;
    }
    /**
     * Builds an URL for calling the api and accessing the schedule for a specific channel
     * @param channelId the channel to access the schedule for
     * @param date the date the schedule should be checked for
     * @return a URL that should return a schedule for that channel and date
     */
    private URL buildScheduleUrl(Integer channelId, Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new URL(apiUrl + "/" + apiScheduleUrl + "?channelid=" +
                    channelId + "&date=" + sdf.format(date) + "&pagination=false");
        }catch(MalformedURLException e){
            return null;
        }
    }

    /**
     * Returns the parsed episodes as an arraylist of Episode objects
     * @return Arraylist of Episode objects
     */
    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    /**
     * Parses a schedule from the sr API
     */
    public void parseSchedule(Date date){
        try {
            InputStream inputStream = buildScheduleUrl(channelId, date).openStream();
            Document doc = parseInputStream(inputStream);
            doc.getElementsByTagName("schedule").item(0).normalize();

            NodeList episodeNodes = doc.getElementsByTagName("scheduledepisode");

            for (int temp = 0; temp < episodeNodes.getLength(); temp++) {
                Node node = episodeNodes.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName() == "scheduledepisode") {
                    Element element = (Element) node;
                    if(element.getElementsByTagName("episodeid").getLength() > 0){
                        Episode episode = buildEpisode(element);
                        if(episode != null){
                            episodes.add(episode);
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            errors.add(new ParsingError("Error configuring Parser", e));
        } catch (IOException e){
            errors.add(new ParsingError("Error opening stream", e));
        } catch (SAXException e){
            errors.add(new ParsingError("Error parsing XML", e));
        }
    }
    /**
     * Constructs an episode object from an xml element
     * @param element a xml element to construct an episode object from
     * @return the constructed episode object
     */
    private Episode buildEpisode(Element element){
        EpisodeBuilder episodeBuilder = new EpisodeBuilder();
        try {
            episodeBuilder.setId(Integer.parseInt(getElementValue(element, "episodeid")));
            episodeBuilder.setTitle(getElementValue(element, "title"));
            episodeBuilder.setSubtitle(getElementValue(element, "subtitle"));
            episodeBuilder.setDescription(getElementValue(element, "description"));
            episodeBuilder.setStartTimeUtc(timeFormat.parse(getElementValue(element, "starttimeutc")));
            episodeBuilder.setEndTimeUtc(timeFormat.parse(getElementValue(element, "endtimeutc")));
            if (getElementValue(element, "imageurl") != null) {
                episodeBuilder.setImageUrl(new URL(getElementValue(element, "imageurl")));
            }
            if (getElementValue(element, "imageurltemplate") != null) {
                episodeBuilder.setImageUrlTemplate(new URL(getElementValue(element, "imageurltemplate")));
            }
        }catch(MalformedURLException e){
            errors.add(new ParsingError("Error setting image URL for episode", e));
        }catch(ParseException e){
            errors.add(new ParsingError("Error parsing image URL from XML", e));
        }
        Episode episode = episodeBuilder.createEpisodeObject();
        // If the episode is not inside the +/- 12 hour span around the date
        if (!episode.checkTime(this.date)){
            episode = null;
        }
        return episode;
    }
}
