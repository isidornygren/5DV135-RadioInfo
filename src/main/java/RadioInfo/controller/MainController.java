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
    private MainMenuBar menuBar;
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
        menuBar = new MainMenuBar();
        view.setMenu(menuBar);
        view.setVisible(true);

        menuBar.setUpdateButton(e -> {
                Channel channel = view.getChannel();
                Date today = new Date();
                // Parse the xml episodes
                fetchEpisodes(view.getTable(), channel.getId(), today);
        });
        try {
            Date today = new Date();

            // Load all images for the channels
            fetchChannels(menuBar);

            // When a specific channel is pressed
            menuBar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Channel channel = ((ChannelSelectEvent)e).getChannel();
                    Episode.setTemplate(channel.getImageUrl());
                    view.setChannel(channel);
                    view.getTable().setColor(channel.getColor());
                    // Update channel list as well
                    fetchEpisodes(view.getTable(), channel.getId(), today);
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
     * @param channelId the channel to check the episodes for
     * @param date the date to check the episodes for
     */
    private void fetchEpisodes(ProgramTableModel table, Integer channelId, Date date){
        if(episodeWorker != null){
            episodeWorker.cancel(true);
        }
        table.clear();
        episodeWorker = new EpisodeWorker(table, channelId, date);//url);
        episodeWorker.execute();
    }

    /**
     * Clears the channel worker if an instance of it is already running and reruns it with new
     * parameters
     * @param menuBar the view to render the channels to
     */
    private void fetchChannels(MainMenuBar menuBar){
        if(channelWorker != null){
            channelWorker.cancel(true);
        }
        channelWorker = new ChannelWorker(menuBar);
        channelWorker.execute();
    }
}
