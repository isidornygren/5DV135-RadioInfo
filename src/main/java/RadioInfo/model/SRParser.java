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
import java.util.TimeZone;

public class SRParser {

    private static String apiUrl;
    private static final String apiChannelurl = "channels";
    private static final String apiScheduleurl = "scheduledepisodes";
    private DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public SRParser(String apiUrl){
        this.apiUrl = apiUrl;
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

    }

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
        } catch (Exception e) { //TODO error handling
            e.printStackTrace();
        }
        return null;
    }

    public Channel parseChannel(InputStream inputStream){
        try {
            Document doc = parseInputStream(inputStream);

            Node channel = doc.getElementsByTagName("channel").item(0);

            if (channel.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) channel;
                return buildChannel(element);
            }
        } catch (Exception e) { //TODO error handling
            e.printStackTrace();
        }
        return null;
    }

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
                        results.add(buildEpisode(element));
                    }
                }
            }
            return results;
        } catch (Exception e) { //TODO error handling
            e.printStackTrace();
        }
        return null;
    }

    public Episode buildEpisode(Element element){
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
        }catch(MalformedURLException e){    //TODO error handling
            e.printStackTrace();
        }catch(ParseException e){
            e.printStackTrace();
        }
        return episodeBuilder.createEpisodeObject();
    }

    public Channel buildChannel(Element element){
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
            //TODO handle error
            e.printStackTrace();
        }

        return channelBuilder.createChannelObject();
    }

    public URL buildChannelUrl(){
        try{
            return new URL(apiUrl + "/" + apiChannelurl + "?pagination=false");
        }catch(MalformedURLException e){
            return null;
        }
    }

    public URL buildChannelUrl(Integer channelId){
        try{
            return new URL(apiUrl + "/" + apiChannelurl + "/" + channelId);
        }catch(MalformedURLException e){
            return null;
        }
    }

    public URL buildScheduleUrl(Integer channelId, Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new URL(apiUrl + "/" + apiScheduleurl + "?channelid=" +
                    channelId + "&date=" + sdf.format(date) + "&pagination=false");
        }catch(MalformedURLException e){
            return null;
        }
    }

    private Document parseInputStream(InputStream inputStream) throws
            ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputStream);
        doc.getDocumentElement().normalize();
        return doc;
    }

    private String getElementValue(Element element, String tagName){
        String ret = null;
        if(element.getElementsByTagName(tagName).getLength() > 0){
            ret = element.getElementsByTagName(tagName).item(0).getTextContent();
        }
        return ret;
    }
}
