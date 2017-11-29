package RadioInfo.model;

import RadioInfo.model.Channel;
import RadioInfo.model.ChannelBuilder;
import RadioInfo.model.Episode;
import RadioInfo.model.EpisodeBuilder;
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

    private static final String apiChannelurl = "http://api.sr.se/api/v2/channels";
    private static final String apiScheduleurl = "http://api.sr.se/api/v2/scheduledepisodes";

    public SRParser(){

    }

    public ArrayList<Channel> parseChannels(InputStream inputStream){
        try {
            Document doc = parseInputStream(inputStream);

            ArrayList<Channel> results = new ArrayList<>();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));

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

    public Episode buildEpisode(Element element){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        EpisodeBuilder episodeBuilder = new EpisodeBuilder();
        try {
            episodeBuilder.setId(Integer.parseInt(getElementValue(element, "episodeid")));
            episodeBuilder.setTitle(getElementValue(element, "title"));
            episodeBuilder.setSubtitle(getElementValue(element, "subtitle"));
            episodeBuilder.setDescription(getElementValue(element, "description"));
            episodeBuilder.setStartTimeUtc(format.parse(getElementValue(element, "starttimeutc")));
            episodeBuilder.setEndTimeUtc(format.parse(getElementValue(element, "endtimeutc")));
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
    }

    public URL buildScheduleUrl(Integer channelId, Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new URL(apiScheduleurl + "?channelid=" + channelId + "&date=" + sdf.format(date) + "&pagination=false");
        }catch(MalformedURLException e){
            return null;
        }
    }

    private Document parseInputStream(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
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
