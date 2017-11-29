package RadioInfo.controller;

import RadioInfo.model.Channel;
import RadioInfo.model.Episode;
import RadioInfo.model.SRParser;
import RadioInfo.view.ChannelMenuBar;
import RadioInfo.view.ChannelSelectEvent;
import RadioInfo.view.ChannelView;
import RadioInfo.view.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MainController {
    private MainView view;

    public MainController(){
        javax.swing.SwingUtilities.invokeLater(this::run);
    }

    private void run() {
        view = new MainView("RadioInfo");
        ChannelView channelView = new ChannelView();
        ChannelMenuBar menuBar = view.getMenuBar();

        view.setVisible(true);
        SRParser xml = new SRParser("http://api.sr.se/api/v2/");

        menuBar.setChannelsButton(e -> channelView.setVisible(true));
        menuBar.setUpdateButton(e -> {
            try {
                Channel channel = view.getChannel();
                Date today = new Date();
                // Parse the xml once again
                ArrayList<Episode> episodes = xml.parseSchedule(xml.buildScheduleUrl(channel.getId(), today).openStream());
                for(Episode episode: episodes){
                    episode.loadImage();
                    episode.loadImageTemplate();
                }
                view.setEpisodes(episodes, channel.getColor());
            }catch(IOException exception){
                exception.printStackTrace(); //TODO handle error
            }
        });
        try {
            Date today = new Date();
            ArrayList<Channel> channels = xml.parseChannels(xml.buildChannelUrl().openStream());

            // Load all images for the channels
            for(Channel channel : channels){
                channel.loadImage();
            }

            channelView.setChannels(channels);
            channelView.setVisible(true);
            // When a specific channel is pressed
            channelView.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Channel channel = ((ChannelSelectEvent)e).getChannel();
                    Episode.setTemplate(channel.getImageUrl());
                    view.setChannel(channel);
                    // Update channel list as well
                    try {
                        ArrayList<Episode> episodes = xml.parseSchedule(xml.buildScheduleUrl(channel.getId(), today).openStream());
                        for(Episode episode: episodes){
                            episode.loadImage();
                            episode.loadImageTemplate();
                        }
                        view.setEpisodes(episodes, channel.getColor());

                    }catch(IOException exception){
                        exception.printStackTrace(); //TODO handle error
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace(); //TODO handle error
        }
    }
}
