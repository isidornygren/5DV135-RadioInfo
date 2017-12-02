package RadioInfo.controller;

import RadioInfo.model.Channel;
import RadioInfo.model.ChannelParser;
import RadioInfo.view.ErrorDialog;
import RadioInfo.view.MainMenuBar;

import javax.swing.*;
import java.io.IOException;
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
        try {
            parser.parseChannels(parser.buildChannelUrl().openStream());
            if (parser.hasErrors()) {
                new ErrorDialog(parser.getErrors().get(0));
                cancel(true);
            } else {
                ArrayList<Channel> channels = parser.getChannels();
                if (parser.hasErrors()) {
                    new ErrorDialog(parser.getErrors().get(0));
                    cancel(true);
                } else {
                    for (Channel channel : channels) {
                        if (isCancelled()) {
                            return false;
                        } else {
                            publish(channel);
                        }
                    }
                }
            }
        }catch(IOException exception){
            new ErrorDialog("Error", "Could not build channel API URL", exception);
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
