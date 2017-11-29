package RadioInfo.controller;

import RadioInfo.ProgramTableModel.ProgramTableModel;
import RadioInfo.model.*;
import RadioInfo.view.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Date;
/**
 * Main controller that initiates the view and starts the swing workers
 * @version 1.0
 * @author Isidor Nygren
 */
public class MainController {
    private MainView view;
    private EpisodeWorker episodeWorker;
    private ChannelWorker channelWorker;

    /**
     * Starts the main EDT
     */
    public MainController(){
        javax.swing.SwingUtilities.invokeLater(this::run);
    }

    /**
     * Initiates all the views and starts the workers
     */
    private void run() {
        view = new MainView("RadioInfo");
        ChannelView channelView = new ChannelView();
        MainMenuBar menuBar = view.getMenuBar();

        view.setVisible(true);
        SRParser xml = new SRParser("http://api.sr.se/api/v2/");

        menuBar.setChannelsButton(e -> channelView.setVisible(true));
        menuBar.setUpdateButton(e -> {
                Channel channel = view.getChannel();
                Date today = new Date();
                // Parse the xml episodes
                fetchEpisodes(view.getTable(), xml, xml.buildScheduleUrl(channel.getId(), today));
        });
        try {
            Date today = new Date();

            // Load all images for the channels
            channelView.setVisible(true);
            fetchChannels(channelView, xml, xml.buildChannelUrl());

            // When a specific channel is pressed
            channelView.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Channel channel = ((ChannelSelectEvent)e).getChannel();
                    Episode.setTemplate(channel.getImageUrl());
                    view.setChannel(channel);
                    view.getTable().setColor(channel.getColor());
                    // Update channel list as well
                    fetchEpisodes(view.getTable(), xml, xml.buildScheduleUrl(channel.getId(), today));
                }
            });
        } catch (Exception e) {
            e.printStackTrace(); //TODO handle error
        }
    }

    /**
     * Clears the episodeworker if an instance of it is already running and reruns it with the new
     * parameters
     * @param table the table to render the episodes to
     * @param parser a SRParser to be used for parsing the episodes
     * @param url the url to parse the data from
     */
    private void fetchEpisodes(ProgramTableModel table, SRParser parser, URL url){
        if(episodeWorker != null){
            episodeWorker.cancel(true);
        }
        table.clear();
        episodeWorker = new EpisodeWorker(table, parser, url);
        episodeWorker.execute();
    }

    /**
     * Clears the channel worker if an instance of it is already running and reruns it with new
     * parameters
     * @param channelView the view to render the channels to
     * @param parser the parser object that fetches the data
     * @param url the stream to fetch the data from
     */
    private void fetchChannels(ChannelView channelView, SRParser parser, URL url){
        if(channelWorker != null){
            channelWorker.cancel(true);
        }
        channelWorker = new ChannelWorker(channelView, parser, url);
        channelWorker.execute();
    }
}
