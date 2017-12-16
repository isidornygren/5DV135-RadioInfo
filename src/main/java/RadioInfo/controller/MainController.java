package RadioInfo.controller;

import RadioInfo.ProgramTableModel.ProgramTableModel;
import RadioInfo.model.*;
import RadioInfo.view.*;

import java.io.IOException;
import java.util.*;

/**
 * RadioInfo controller that initiates the view and starts the swing workers
 * @version 1.0
 * @author Isidor Nygren
 */
public class MainController {
    private MainView view;
    private EpisodeWorker episodeWorker;
    private ChannelWorker channelWorker;
    private Timer timer = new Timer();

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
        MainMenuBar menuBar = new MainMenuBar();
        view.setMenu(menuBar);
        view.setVisible(true);


        // Add timer to update schedule every hour
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                // Load all the channels
                Date now = new Date();
                fetchChannels(menuBar, now);
            }
        }, 0, 3600 * 1000);

        menuBar.setUpdateButton(e -> {
                // Update the channel list
                Date today = new Date();
                fetchChannels(menuBar, today);
        });

        // When a specific channel is pressed
        menuBar.addActionListener(e -> {
            Channel channel = ((ChannelSelectEvent)e).getChannel();
            // Set the header image of the channel
            try{
                Episode.setTemplate(channel.getImageUrl());
            }catch(IOException exception){
                new ErrorDialog("Error", "Could not set episode template icons", exception);
            }
            view.setChannel(channel);
            view.getTable().setColor(channel.getColor());
            // Update the table with the episodes, loading the images (if not already loaded)
            // In the process
            fetchEpisodes(view.getTable(), channel);
        });
    }

    /**
     * Clears the episodeworker if an instance of it is already running and reruns it with the new
     * parameters
     * @param table the table to render the episodes to
     * @param channel the channel to check the episodes for
     */
    private void fetchEpisodes(ProgramTableModel table, Channel channel){
        table.clear();

        // Add the episodes to the table first
        ArrayList<Episode> episodes = channel.getEpisodes();
        for (Episode episode : episodes) {
            table.addEpisode(episode);
        }
        table.fireTableDataChanged();

        if(episodeWorker != null){
            episodeWorker.cancel(true);
        }
        episodeWorker = new EpisodeWorker(table, channel);
        episodeWorker.execute();
    }

    /**
     * Clears the channel worker if an instance of it is already running and reruns it with new
     * parameters
     * @param menuBar the view to render the channels to
     * @param date the date to fetch the episodes from
     */
    private void fetchChannels(MainMenuBar menuBar, Date date){
        menuBar.clearChannels();
        if(channelWorker != null){
            channelWorker.cancel(true);
        }
        channelWorker = new ChannelWorker(menuBar, date);
        channelWorker.execute();
        channelWorker.addListener(() -> {
            // If there is a currently selected channel then update that
            Channel channel = view.getChannel();
            if(channel != null){
                // Parse the xml episodes
                fetchEpisodes(view.getTable(), channel);
            }
        });
    }
}
