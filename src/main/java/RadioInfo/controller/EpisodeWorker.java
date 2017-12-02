package RadioInfo.controller;

import RadioInfo.ProgramTableModel.ProgramTableModel;
import RadioInfo.model.Episode;
import RadioInfo.model.ScheduleParser;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Swing worker which parses episode data and renders it to the view
 * @version 1.0
 * @author Isidor Nygren
 */
public class EpisodeWorker extends SwingWorker<Boolean, Episode>{
    private final ProgramTableModel table;
    private final ScheduleParser parser;
    private Date date;
    private Integer channelId;

    /**
     * Creates a new swing worker
     * @param table table to render all the episodes to
     * @param channelId the id of the channel to fetch episodes for
     * @param date the date to fetch the episodes from
     */
    EpisodeWorker(ProgramTableModel table, Integer channelId, Date date){//URL url){
        this.table = table;
        this.parser = new ScheduleParser(channelId, date);
        this.channelId = channelId;
        this.date = date;
    }

    /**
     * Fetches and loads all the images for the episodes
     * @return true if finished and without error, otherwise false
     */
    @Override
    protected Boolean doInBackground() {
        // Fetch the current list of episodes
        try{
            // Fetch the episodes for today, yesterday and tomorrow
            for(int day = -1; day <= 1; day++){
                Date date = new Date(this.date.getTime() + day*3600*1000*24);
                parser.parseSchedule(date);
                ArrayList<Episode> episodes = parser.getEpisodes();
                for (Episode episode : episodes) {
                    episode.loadImage();
                    episode.loadImageTemplate();
                    if (isCancelled()) {
                        return false;
                    } else {
                        publish(episode);
                    }
                }
            }
        }catch(IOException e){
            return false;
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
                this.table.addEpisode(episode);
            }
        }
    }
}
