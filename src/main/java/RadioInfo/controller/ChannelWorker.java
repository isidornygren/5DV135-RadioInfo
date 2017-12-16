package RadioInfo.controller;

import RadioInfo.model.*;
import RadioInfo.view.ErrorDialog;
import RadioInfo.view.MainMenuBar;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Swing worker which parses channel data and renders it to the view
 * @version 1.0
 * @author Isidor Nygren
 */
public class ChannelWorker extends SwingWorker<Boolean, Channel>{
    private ArrayList<ChannelListener> channelListeners = new ArrayList<ChannelListener>();

    private final MainMenuBar menuBar;
    private final ChannelParser parser;
    private ScheduleParser scheduleParser;
    private Date date;

    /**
     * Creates a new swing worker
     * @param menuBar the view to render the channels to
     */
    ChannelWorker(MainMenuBar menuBar, Date date){
        this.parser = new ChannelParser();
        this.menuBar = menuBar;
        this.date = date;
    }

    public void addListener(ChannelListener listener){
        channelListeners.add(listener);
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
                            channel.addEpisodeList(parseSchedule(channel.getId()));
                            publish(channel);
                        }
                    }
                    if (isCancelled()) {
                        return false;
                    }else{
                        // Send a done call to all the listeners
                        for(ChannelListener listener : channelListeners){
                            listener.channelDone();
                        }
                    }
                }
            }
        }catch(IOException exception){
            new ErrorDialog("Error", "Could not build channel API URL", exception);
        }
        return true;
    }

    private ArrayList<Episode> parseSchedule(int channelId){
        ArrayList<Episode> episodeList = new ArrayList<>();

        scheduleParser = new ScheduleParser(channelId, new Date());
        // Fetch the current list of episodes
        try{
            // Fetch the episodes for today, yesterday and tomorrow
            for(int day = -1; day <= 1; day++){
                Date date = new Date(this.date.getTime() + day*86400000);
                scheduleParser.parseSchedule(scheduleParser.buildScheduleUrl(date).openStream());
            }
            if(scheduleParser.hasErrors()){
                // Show the first error of the parser
                new ErrorDialog(scheduleParser.getErrors().get(0));
                cancel(true);
            }else{
                ArrayList<Episode> episodes = scheduleParser.getEpisodes();
                if(scheduleParser.hasErrors()){
                    // Show the first error of the parser
                    new ErrorDialog(scheduleParser.getErrors().get(0));
                    cancel(true);
                }else {
                    for (Episode episode : episodes) {
                        episodeList.add(episode);
                    }
                }
            }
        }catch(IOException e){
            new ErrorDialog("Error","Error building schedule API URL",e);
            cancel(true);
        }
        return episodeList;
    }

    public void channelDone(){

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
