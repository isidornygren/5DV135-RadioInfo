package RadioInfo.controller;

import RadioInfo.ProgramTableModel.ProgramTableModel;
import RadioInfo.model.Episode;
import RadioInfo.model.SRParser;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/**
 * Swing worker which parses episode data and renders it to the view
 * @version 1.0
 * @author Isidor Nygren
 */
public class EpisodeWorker extends SwingWorker<Boolean, Episode>{
    private final ProgramTableModel table;
    private final SRParser parser;
    private InputStream inputStream;

    /**
     * Creates a new swing worker
     * @param table table to render all the episodes to
     * @param parser the parser that is used to fetch the data
     * @param url the inputstream in which to fetch the data from
     */
    EpisodeWorker(ProgramTableModel table, SRParser parser, URL url){
        this.table = table;
        this.parser = parser;
        try {
            this.inputStream = url.openStream();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Fetches and loads all the images for the episodes
     * @return true if finished and without error, otherwise false
     */
    @Override
    protected Boolean doInBackground() {
        ArrayList<Episode> episodes = parser.parseSchedule(inputStream);
        for (Episode episode : episodes) {
            try {
                episode.loadImage();
                episode.loadImageTemplate();
                if (isCancelled()) {
                    return false;
                } else {
                    publish(episode);
                }
            } catch (IOException e) {
                return false;
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
                this.table.addEpisode(episode);
            }
        }
    }
}
