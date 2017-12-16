package RadioInfo.controller;

import RadioInfo.ProgramTableModel.ProgramTableModel;
import RadioInfo.model.Channel;
import RadioInfo.model.Episode;
import RadioInfo.model.ScheduleParser;
import RadioInfo.view.ErrorDialog;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

/**
 * Swing worker which parses episode data and renders it to the view
 * @version 1.0
 * @author Isidor Nygren
 */
public class EpisodeWorker extends SwingWorker<Boolean, Episode>{
    private final ProgramTableModel table;
    private final Channel channel;

    /**
     * Creates a new swing worker
     * @param table table to render all the episodes to
     * @param channel the channel to fetch the episodes from
     */
    EpisodeWorker(ProgramTableModel table, Channel channel){//URL url){
        this.table = table;
        this.channel = channel;
    }

    /**
     * Fetches and loads all the images for the episodes
     * @return true if finished and without error, otherwise false
     */
    @Override
    protected Boolean doInBackground() {
        // Fetch the current list of episodes
        // Load all the episode images after the episodes has been published
        ArrayList<Episode> episodes = channel.getEpisodes();
        for (Episode episode : episodes) {
            try {
                episode.loadImage();
                episode.loadImageTemplate();
                publish(episode);
            }catch(IOException e){
                new ErrorDialog("Error","Error loading images from API",e);
                cancel(true);
            }
        }
        return true;
    }

    /**
     * renders all the episodes one by one to the table in the EDT thread
     * @param chunks the resulting episodes from the parsing
     */
    @Override
    protected void process(List<Episode> chunks) {
        for (Episode episode : chunks) {
            if(!isCancelled()) {
                //this.table.addEpisode(episode);
                this.table.fireTableDataChanged();
            }
        }
    }
}
