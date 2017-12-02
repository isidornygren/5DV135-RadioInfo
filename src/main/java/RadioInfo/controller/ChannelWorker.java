package RadioInfo.controller;

import RadioInfo.model.Channel;
import RadioInfo.model.ChannelParser;
import RadioInfo.view.MainMenuBar;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Swing worker which parses channel data and renders it to the view
 * @version 1.0
 * @author Isidor Nygren
 */
public class ChannelWorker extends SwingWorker<Boolean, Channel>{
    private final MainMenuBar menuBar;
    private final ChannelParser parser;

    /**
     * Creates a new swing worker
     * @param menuBar the view to render the channels to
     */
    ChannelWorker(MainMenuBar menuBar){
        this.parser = new ChannelParser();
        this.menuBar = menuBar;
    }

    /**
     * Parses the data into channelobjects and loads the images for the objects
     * @return true if it parsed all the channels without error, false if not
     */
    @Override
    protected Boolean doInBackground(){
        parser.parseChannels();
        ArrayList<Channel> channels = parser.getChannels();
        for(Channel channel : channels){
                if(isCancelled()){
                    return false;
                }else {
                    publish(channel);
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
            this.menuBar.addChannel(channel);
        }
    }
}
