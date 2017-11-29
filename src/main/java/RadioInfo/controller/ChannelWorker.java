package RadioInfo.controller;

import RadioInfo.model.Channel;
import RadioInfo.model.SRParser;
import RadioInfo.view.ChannelView;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * Swing worker which parses channel data and renders it to the view
 * @version 1.0
 * @author Isidor Nygren
 */
public class ChannelWorker extends SwingWorker<Boolean, Channel>{
    private ChannelView channelView;
    private SRParser parser;
    private InputStream inputStream;

    /**
     * Creates a new swing worker
     * @param channelView the view to render the channels to
     * @param parser the parser object that fetches the data
     * @param inputStream the stream to fetch the data from
     */
    ChannelWorker(ChannelView channelView, SRParser parser, InputStream inputStream){
        this.channelView = channelView;
        this.parser = parser;
        this.inputStream = inputStream;
    }

    /**
     * Parses the data into channelobjects and loads the images for the objects
     * @return true if it parsed all the channels without error, false if not
     */
    @Override
    protected Boolean doInBackground(){
        ArrayList<Channel> channels = parser.parseChannels(inputStream);
        for(Channel channel : channels){
            try {
                System.out.println("Channel: " + channel.getName());
                channel.loadImage();
                if(isCancelled()){
                    return false;
                }else {
                    publish(channel);
                }
            }catch(IOException e){
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * Renders the channels to the view one by one
     * @param chunks a list of all the channels to render to the view
     */
    @Override
    protected void process(List<Channel> chunks) {
        for(Channel channel : chunks){
            this.channelView.addChannel(channel);
        }
    }
}
