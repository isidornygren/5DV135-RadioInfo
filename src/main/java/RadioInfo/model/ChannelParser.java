package RadioInfo.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
/**
 * Handles the parsing of the channels from the sr API
 * @version 1.0
 * @author Isidor Nygren
 */
public class ChannelParser extends Parser {
    private static final String apiChannelUrl = "channels";
    private ArrayList<Channel> channels = new ArrayList<>();
    /**
     * Constructor
     */
    public ChannelParser(){}
    /**
     * Parses all channel objects from an inputstream (buildChannelUrl)
     */
    public void parseChannels(InputStream inputStream){
        try {
            Document doc = parseInputStream(inputStream);
            doc.getElementsByTagName("channels").item(0).normalize();
            NodeList channelNodes = doc.getElementsByTagName("channel");

            for (int temp = 0; temp < channelNodes.getLength(); temp++) {
                Node node = channelNodes.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    channels.add(buildChannel(element));
                }
            }
        }catch (NullPointerException e){
            errors.add(new ParsingError("Error parsing XML, could not find element", e));
        }
    }

    /**
     * Returns all the parsed channels from the api
     * @return arraylist of Channel objects
     */
    public ArrayList<Channel> getChannels() {
        return channels;
    }

    /**
     * Parses a specific channel from the sr API
     * @param inputStream an inputstream for the sr channel (buildChannelUrl(integer))
     * @return the parsed channel from the api
     */
    public Channel parseChannel(InputStream inputStream){
        Document doc = parseInputStream(inputStream);

        Node channel = doc.getElementsByTagName("channel").item(0);

        if (channel.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) channel;
            return buildChannel(element);
        }
        return null;
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
            return new URL(apiUrl + "/" + apiChannelUrl + "?pagination=false");
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
            return new URL(apiUrl + "/" + apiChannelUrl + "/" + channelId);
        }catch(MalformedURLException e){
            return null;
        }
    }
}
