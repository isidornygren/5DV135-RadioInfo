package RadioInfo.model;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;

/**
 * Parsing model that connects to the SR API
 * @version 1.0
 * @author Isidor Nygren
 */
public class SRParser {

    private String apiUrl;
    private static final String apiChannelurl = "channels";
    private static final String apiScheduleurl = "scheduledepisodes";
    private DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private ArrayList<ParsingError> errors = new ArrayList<>();
    private Date date;

    /**
     * Sets up the api and corrects the timezone
     * @param apiUrl the base url for the api
     */
    public SRParser(String apiUrl, Date date){
        this.apiUrl = apiUrl;
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.date = date;
    }

    /**
     * Parses all channel objects from an inputstream (buildChannelUrl)
     * @param inputStream an inputstream for the sr channels url
     * @return an arraylist with channelobjects parsed from the inputstream
     */
    public ArrayList<Channel> parseChannels(InputStream inputStream){
        try {
            Document doc = parseInputStream(inputStream);
            ArrayList<Channel> results = new ArrayList<>();
            doc.getElementsByTagName("channels").item(0).normalize();
            NodeList episodes = doc.getElementsByTagName("channel");

            for (int temp = 0; temp < episodes.getLength(); temp++) {
                Node node = episodes.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    results.add(buildChannel(element));
                }
            }
            return results;
        } catch (ParserConfigurationException e) {
            errors.add(new ParsingError("Error configuring parser", e));
        } catch (IOException e){
            errors.add(new ParsingError("Error opening stream", e));
        } catch (SAXException e){
            errors.add(new ParsingError("Error parsing XML", e));
        }catch (NullPointerException e){
            errors.add(new ParsingError("Error parsing XML, could not find element", e));
        }
        return null;
    }

    /**
     * Parses a specific channel from the sr API
     * @param inputStream an inputstream for the sr channel (buildChannelUrl(integer))
     * @return the parsed channel from the api
     */
    public Channel parseChannel(InputStream inputStream){
        try {
            Document doc = parseInputStream(inputStream);

            Node channel = doc.getElementsByTagName("channel").item(0);

            if (channel.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) channel;
                return buildChannel(element);
            }
        } catch (ParserConfigurationException e) {
            errors.add(new ParsingError("Error configuring parser", e));
        } catch (IOException e){
            errors.add(new ParsingError("Error opening stream", e));
        } catch (SAXException e){
            errors.add(new ParsingError("Error parsing XML", e));
        }
        return null;
    }

    /**
     * Parses a schedule from the sr API
     * @param inputStream a inputstream, use (buildScheduleUrl)
     * @return an arraylist of the parsed episodes as Episode objects
     */
    public ArrayList<Episode> parseSchedule(InputStream inputStream){
        try {
            ArrayList<Episode> results = new ArrayList<>();

            Document doc = parseInputStream(inputStream);
            doc.getElementsByTagName("schedule").item(0).normalize();

            NodeList episodes = doc.getElementsByTagName("scheduledepisode");

            for (int temp = 0; temp < episodes.getLength(); temp++) {
                Node node = episodes.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName() == "scheduledepisode") {
                    Element element = (Element) node;
                    if(element.getElementsByTagName("episodeid").getLength() > 0){
                        Episode episode = buildEpisode(element);
                        if(episode != null){
                            results.add(episode);
                        }
                    }
                }
            }
            return results;
        } catch (ParserConfigurationException e) {
            errors.add(new ParsingError("Error configuring parser", e));
        } catch (IOException e){
            errors.add(new ParsingError("Error opening stream", e));
        } catch (SAXException e){
            errors.add(new ParsingError("Error parsing XML", e));
        }
        return null;
    }

    /**
     * Parses a schedule from the sr API and adds it to a arraylist
     * which it returns in the end
     * @param inputStream a inputstream, use (buildScheduleUrl)
     * @param episodes the arraylist to add the episodes to
     * @return an arraylist of the parsed episodes as Episode objects
     */
    public ArrayList<Episode> parseSchedule(InputStream inputStream, ArrayList<Episode> episodes){
        try {

            Document doc = parseInputStream(inputStream);
            doc.getElementsByTagName("schedule").item(0).normalize();

            NodeList nodeList = doc.getElementsByTagName("scheduledepisode");

            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node node = nodeList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName() == "scheduledepisode") {
                    Element element = (Element) node;
                    if(element.getElementsByTagName("episodeid").getLength() > 0){
                        Episode episode = buildEpisode(element);
                        episodes.add(episode);
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            errors.add(new ParsingError("Error configuring parser", e));
        } catch (IOException e){
            errors.add(new ParsingError("Error opening stream", e));
        } catch (SAXException e){
            errors.add(new ParsingError("Error parsing XML", e));
        }
        return episodes;
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

    /**
     * Builds a channel object from an xml element
     * @param element the element from which to build the object
     * @return a channel object
     */
    private Channel buildChannel(Element element){
        ChannelBuilder channelBuilder = new ChannelBuilder();
        try {
            channelBuilder.setId(Integer.parseInt(element.getAttribute("id")));
            channelBuilder.setName(element.getAttribute("name"));
            channelBuilder.setChannelType(element.getElementsByTagName("channeltype").item(0).getTextContent());
            channelBuilder.setColor(element.getElementsByTagName("color").item(0).getTextContent());
            channelBuilder.setImageUrl(new URL(element.getElementsByTagName("image").item(0).getTextContent()));
            channelBuilder.setImageUrl(new URL(element.getElementsByTagName("imagetemplate").item(0).getTextContent()));
            if(element.getElementsByTagName("scheduleurl").getLength() > 0){
                channelBuilder.setScheduleUrl(new URL(element.getElementsByTagName("scheduleurl").item(0).getTextContent()));
            }
            channelBuilder.setSiteUrl(new URL(element.getElementsByTagName("siteurl").item(0).getTextContent()));
        }catch(MalformedURLException e){
            errors.add(new ParsingError("Error setting schedule URL for channel", e));
        }

        return channelBuilder.createChannelObject();
    }

    /**
     * Builds a channel url based on the api url used for constructing the object,
     * makes sure the api returns all the data in one call
     * @return a constructed URL which should return channels when called within the API
     */
    public URL buildChannelUrl(){
        try{
            return new URL(apiUrl + "/" + apiChannelurl + "?pagination=false");
        }catch(MalformedURLException e){
            return null;
        }
    }

    /**
     * Builds a singular channel url where data regarding one specific channel will
     * be returned when called.
     * @param channelId the id of the channel to call
     * @return an URL that should return the channel when called within the API
     */
    public URL buildChannelUrl(Integer channelId){
        try{
            return new URL(apiUrl + "/" + apiChannelurl + "/" + channelId);
        }catch(MalformedURLException e){
            return null;
        }
    }

    /**
     * Builds an URL for calling the api and accessing the schedule for a specific channel
     * @param channelId the channel to access the schedule for
     * @param date the date the schedule should be checked for
     * @return a URL that should return a schedule for that channel and date
     */
    public URL buildScheduleUrl(Integer channelId, Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new URL(apiUrl + "/" + apiScheduleurl + "?channelid=" +
                    channelId + "&date=" + sdf.format(date) + "&pagination=false");
        }catch(MalformedURLException e){
            return null;
        }
    }

    /**
     * returns an arraylist of all errors that was found during parsing of the API
     * @return an arraylist of all the ParsingError objects
     */
    public ArrayList<ParsingError> getErrors(){
        return errors;
    }

    /**
     * Parses an XML inputstream and returns it into a normalized document
     * @param inputStream the inputstream to parse
     * @return A normalized Document
     * @throws ParserConfigurationException it there is an inherit error in the parser
     * @throws IOException if there was an error parsing the stream
     * @throws SAXException if there was an error parsing the xml
     */
    private Document parseInputStream(InputStream inputStream) throws
            ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputStream);
        doc.getDocumentElement().normalize();
        return doc;
    }

    /**
     * Returns the value (text context) of en XML element
     * @param element the element to check for the text context
     * @param tagName the tag inside the element
     * @return the string that was inside the tag
     */
    private String getElementValue(Element element, String tagName){
        String ret = null;
        if(element.getElementsByTagName(tagName).getLength() > 0){
            ret = element.getElementsByTagName(tagName).item(0).getTextContent();
        }
        return ret;
    }
}
