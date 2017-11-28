package RadioInfo.controller;

import RadioInfo.ChannelObject;
import RadioInfo.ChannelObjectBuilder;
import RadioInfo.EpisodeObject;
import RadioInfo.EpisodeObjectBuilder;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class XMLController {

    private static final String apiChannelurl = "http://api.sr.se/api/v2/channels";
    private static final String apiScheduleurl = "http://api.sr.se/api/v2/scheduledepisodes";

    public XMLController(){

    }

    public ArrayList<ChannelObject> parseChannels(InputStream is){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            ArrayList<ChannelObject> results = new ArrayList<>();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));

            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            doc.getElementsByTagName("channels").item(0).normalize();

            NodeList episodes = doc.getElementsByTagName("channel");//schedule.item(0).getChildNodes();

            for (int temp = 0; temp < episodes.getLength(); temp++) {
                Node n = episodes.item(temp);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) n;
                    results.add(buildChannel(element));
                }
            }
            return results;
        } catch (Exception e) { //TODO error handling
            e.printStackTrace();
        }
        return null;
    }

    public ChannelObject parseChannel(InputStream is){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
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

    public ArrayList<EpisodeObject> parseSchedule(InputStream is){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            ArrayList<EpisodeObject> results = new ArrayList<>();


            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            doc.getElementsByTagName("schedule").item(0).normalize();

            NodeList episodes = doc.getElementsByTagName("scheduledepisode");//schedule.item(0).getChildNodes();

            for (int temp = 0; temp < episodes.getLength(); temp++) {
                Node n = episodes.item(temp);
                if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName() == "scheduledepisode") {
                    Element element = (Element) n;
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

    public EpisodeObject buildEpisode(Element element){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        EpisodeObjectBuilder episodeObjectBuilder = new EpisodeObjectBuilder();
        try {
            episodeObjectBuilder.setId(Integer.parseInt(getElementValue(element, "episodeid")));
            episodeObjectBuilder.setTitle(getElementValue(element, "title"));
            episodeObjectBuilder.setSubtitle(getElementValue(element, "subtitle"));
            episodeObjectBuilder.setDescription(getElementValue(element, "description"));
            episodeObjectBuilder.setStartTimeUtc(format.parse(getElementValue(element, "starttimeutc")));
            episodeObjectBuilder.setEndTimeUtc(format.parse(getElementValue(element, "endtimeutc")));
            if (getElementValue(element, "imageurl") != null) {
                episodeObjectBuilder.setImageUrl(new URL(getElementValue(element, "imageurl")));
            }
            if (getElementValue(element, "imageurltemplate") != null) {
                episodeObjectBuilder.setImageUrlTemplate(new URL(getElementValue(element, "imageurltemplate")));
            }
        }catch(MalformedURLException e){    //TODO error handling
            e.printStackTrace();
        }catch(ParseException e){
            e.printStackTrace();
        }
        return episodeObjectBuilder.createEpisodeObject();
    }

    public ChannelObject buildChannel(Element element){
        ChannelObjectBuilder channelObjectBuilder = new ChannelObjectBuilder();
        try {
            channelObjectBuilder.setId(Integer.parseInt(element.getAttribute("id")));
            channelObjectBuilder.setName(element.getAttribute("name"));
            channelObjectBuilder.setChannelType(element.getElementsByTagName("channeltype").item(0).getTextContent());
            channelObjectBuilder.setColor(element.getElementsByTagName("color").item(0).getTextContent());
            channelObjectBuilder.setImageUrl(new URL(element.getElementsByTagName("image").item(0).getTextContent()));
            channelObjectBuilder.setImageUrl(new URL(element.getElementsByTagName("imagetemplate").item(0).getTextContent()));
            if(element.getElementsByTagName("scheduleurl").getLength() > 0){
                channelObjectBuilder.setScheduleUrl(new URL(element.getElementsByTagName("scheduleurl").item(0).getTextContent()));
            }
            channelObjectBuilder.setSiteUrl(new URL(element.getElementsByTagName("siteurl").item(0).getTextContent()));
        }catch(MalformedURLException e){
            //TODO handle error
            e.printStackTrace();
        }

        return channelObjectBuilder.createChannelObject();
    }

    public URL buildChannelUrl(){
        try{
            return new URL(apiChannelurl + "?pagination=false");
        }catch(MalformedURLException e){
            return null;
        }
    }

    public URL buildChannelUrl(Integer channelId){
        try{
            return new URL(apiChannelurl + "/" + channelId);
        }catch(MalformedURLException e){
            return null;
        }
    }n

    public URL buildScheduleUrl(Integer channelId, Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new URL(apiScheduleurl + "?channelid=" + channelId + "&date=" + sdf.format(date) + "&pagination=false");
        }catch(MalformedURLException e){
            return null;
        }
    }

    private String getElementValue(Element element, String tagName){
        String ret = null;
        if(element.getElementsByTagName(tagName).getLength() > 0){
            ret = element.getElementsByTagName(tagName).item(0).getTextContent();
        }
        return ret;
    }
}
