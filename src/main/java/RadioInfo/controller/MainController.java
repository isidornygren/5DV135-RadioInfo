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
        //TODO remove System.out.println(Episode.setTemplate(this.getClass().getResource("/images/template.png")));
        view = new MainView("RadioInfo");
        ChannelView channelView = new ChannelView();
        view.setVisible(true);
        ChannelMenuBar menuBar = view.getMenuBar();
        menuBar.setChannelsButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                channelView.setVisible(true);
            }
        });
        menuBar.setUpdateButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SRParser xml = new SRParser("http://api.sr.se/api/v2/");
                    Channel channel = view.getChannel();
                    Date today = new Date();

                    ArrayList<Episode> episodes = xml.parseSchedule(xml.buildScheduleUrl(channel.getId(), today).openStream());
                    for(int i = 0; i < episodes.size(); i++){
                        Episode episode = episodes.get(i);
                        System.out.println("Loading image for episode: " + episode.getTitle());
                        episode.loadImage();
                        episode.loadImageTemplate();
                        view.setEpisodes(episodes, channel.getColor());
                    }
                }catch(IOException exception){
                    exception.printStackTrace(); //TODO handle error
                }
            }
        });
        try {
            SRParser xml = new SRParser("http://api.sr.se/api/v2/");
            Date today = new Date();

            System.out.println("Parsing XML:");
            Channel channel = xml.parseChannel(xml.buildChannelUrl(132).openStream());
            System.out.println("Done parsing XML for channel.");
            ArrayList<Episode> episodes = xml.parseSchedule(xml.buildScheduleUrl(channel.getId(), today).openStream());
            System.out.println("Done parsing XML for episodes.");
            ArrayList<Channel> channels = xml.parseChannels(xml.buildChannelUrl().openStream());
            System.out.println("Done parsing XML for channels.");

            for(int i = 0; i < channels.size(); i++){
                Channel temp = channels.get(i);
                System.out.println("Channel: " + temp.getName());
                temp.loadImage();
            }

            channelView.setChannels(channels);
            channelView.setVisible(true);
            channelView.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Channel channel = ((ChannelSelectEvent)e).getChannel();
                    Episode.setTemplate(channel.getImageUrl());
                    view.setChannel(channel);
                    // Update channel list as well
                    try {
                        ArrayList<Episode> episodes = xml.parseSchedule(xml.buildScheduleUrl(channel.getId(), today).openStream());
                        for(int i = 0; i < episodes.size(); i++){
                            Episode episode = episodes.get(i);
                            System.out.println("Loading image for episode: " + episode.getTitle());
                            episode.loadImage();
                            episode.loadImageTemplate();
                            view.setEpisodes(episodes, channel.getColor());
                        }
                    }catch(IOException exception){
                        exception.printStackTrace(); //TODO handle error
                    }
                }
            });
            //view.buildChannelSelect(channels);

            //System.out.println("\nLoading images");
            //channel.loadImage();
            /*for(int i = 0; i < episodes.size(); i++){
                Episode episode = episodes.get(i);
                System.out.println("Loading image for episode: " + episode.getTitle());
                episode.loadImage();
                episode.loadImageTemplate();
            }*/
            /*Episode.setTemplate(channel.getImageUrl());
            System.out.println("\nImages loaded");
            view.setChannel(channel);*/
            //view.setEpisodes(episodes, channel.getColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
