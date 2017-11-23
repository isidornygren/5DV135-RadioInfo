package RadioInfo.view;

import RadioInfo.ChannelObject;
import RadioInfo.ChannelObjectBuilder;
import RadioInfo.EpisodeObject;
import RadioInfo.EpisodeObjectBuilder;
import RadioInfo.ProgramTableModel.ProgramTableModel;
import RadioInfo.controller.MainController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainView {
    private final JFrame frame;
    private final MainController controller;
    private JEditorPane episodeEditorPane;
    private JEditorPane channelEditorPane;

    private JPanel informationPanel;
    private JPanel episodePanel;
    private JPanel channelPanel;
    private JLabel episodeIconLabel;
    private JLabel channelIconLabel;

    private JTable table;
    private ProgramTableModel tableModel;


    public MainView(MainController controller, String title){
        this.controller = controller;

        frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(300,300));

        frame.setJMenuBar(buildMenuBar());
        frame.add(buildChannel(), BorderLayout.PAGE_START);
        frame.add(buildTable(), BorderLayout.CENTER);
        frame.add(buildInformation(), BorderLayout.PAGE_END);

        try {
            ChannelObjectBuilder channelBuilder = new ChannelObjectBuilder();
            ChannelObject tempChannel = channelBuilder.setName("P1").setChannelType("Rikskanal").setColor("31a1bd")
                    .setId(132).setImageUrl(new URL("http://static-cdn.sr.se/sida/images/132/2186745_512_512.jpg?preset=api-default-square"))
                    .setScheduleUrl(new URL("http://api.sr.se/v2/scheduledepisodes?channelid=132")).createChannelObject();
            tempChannel.loadImage();
            setChannel(tempChannel);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));

            EpisodeObjectBuilder episodeObjectBuilder = new EpisodeObjectBuilder();
            EpisodeObject tempEpisode = episodeObjectBuilder.setImageUrl(new URL("http://static-cdn.sr.se/sida/images/4540/3634468_2048_1152.jpg?preset=api-default-square"))
                    .setTitle("Ekonyheter").setChannelId(1).setStartTimeUtc(format.parse("2017-11-22T23:00:00Z")).setEndTimeUtc(format.parse("2017-11-22T23:02:00Z")).setId(1).setImageUrlTemplate(new URL("http://static-cdn.sr.se/sida/images/4540/3634468_2048_1152.jpg"))
                    .setProgramId(1).setUrl(new URL("http://www.google.com/")).createEpisodeObject();
            tempEpisode.loadImage();
            setInformation(tempEpisode);

            EpisodeObjectBuilder episodeObjectBuilder2 = new EpisodeObjectBuilder();
            EpisodeObject tempEpisode2 = episodeObjectBuilder2.setImageUrl(new URL("http://static-cdn.sr.se/sida/images/2689/bb164a36-2ecc-4d21-a48e-363ad55541d9.jpg?preset=api-default-square"))
                    .setTitle("Vaken").setChannelId(1).setStartTimeUtc(format.parse("2017-11-22T23:02:00Z")).setEndTimeUtc(format.parse("2017-11-23T00:00:00Z")).setId(1).setImageUrlTemplate(new URL("http://static-cdn.sr.se/sida/images/2689/bb164a36-2ecc-4d21-a48e-363ad55541d9.jpg"))
                    .setProgramId(1).setSubtitle("med Wivianne Svedberg och Peter Sundberg").setUrl(new URL("http://www.google.com/")).createEpisodeObject();
            tempEpisode2.loadImage();

            ArrayList<EpisodeObject> tempEpisodes = new ArrayList<>();
            tempEpisodes.add(tempEpisode);
            tempEpisodes.add(tempEpisode2);
            tempEpisodes.add(tempEpisode);
            tempEpisodes.add(tempEpisode2);
            tempEpisodes.add(tempEpisode);
            tempEpisodes.add(tempEpisode2);
            tempEpisodes.add(tempEpisode);
            tempEpisodes.add(tempEpisode2);
            tempEpisodes.add(tempEpisode);
            tempEpisodes.add(tempEpisode2);
            tableModel.updateList(tempEpisodes, tempChannel.getColor());
            tableModel.setColor(tempChannel.getColor());

        }catch(Exception e){
            e.printStackTrace();
        }

        frame.pack();
    }

    public void setVisible(boolean visible){
        frame.setVisible(visible);
    }

    private JMenuBar buildMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenuItem menuItem, subMenu, updateMenu;

        subMenu = new JMenu("Channels");
        subMenu.setMnemonic(KeyEvent.VK_C);

        updateMenu = new JMenuItem("Update");
        updateMenu.setMnemonic(KeyEvent.VK_U);

        menuItem = new JMenuItem("P1");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_2, ActionEvent.ALT_MASK));
        subMenu.add(menuItem);

        menuItem = new JMenuItem("P2");
        subMenu.add(menuItem);
        menuBar.add(subMenu);
        menuBar.add(updateMenu);
        return menuBar;
    }

    private JPanel buildTable(){
        JPanel panel = new JPanel(new BorderLayout());

        ArrayList<EpisodeObject> tempEpisode = new ArrayList<>();
        tableModel = new ProgramTableModel(tempEpisode, "000000");

        table = new JTable(tableModel);

        table.setGridColor(Color.LIGHT_GRAY);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        //table.setShowGrid(false);
        table.setRowHeight(64);
        table.getColumn("Time").setMaxWidth(128);
        table.getColumn("Image").setMaxWidth(128);

        table.setShowVerticalLines(false);
        table.setTableHeader(null);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Listener for clicking on a cell in the table
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                setInformation(tableModel.getRowValue(table.getSelectedRow()));
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);

        return panel;
    }

    private JPanel buildChannel() {
        channelPanel = new JPanel(new BorderLayout());
        channelEditorPane = new JEditorPane();
        channelIconLabel = new JLabel();

        channelEditorPane.setEditable(false);
        channelEditorPane.setOpaque(false);
        channelEditorPane.setFocusable(false);
        //channelEditorPane.setPreferredSize(new Dimension(40));

        channelPanel.add(channelIconLabel, BorderLayout.LINE_START);
        channelPanel.add(channelEditorPane, BorderLayout.CENTER);

        channelPanel.setVisible(false);

        return channelPanel;
    }

    private JPanel buildInformation(){
        informationPanel = new JPanel(new BorderLayout());
        episodePanel = new JPanel(new BorderLayout());
        episodeEditorPane = new JEditorPane();
        episodeIconLabel = new JLabel();
        JScrollPane scrollPane = new JScrollPane(episodeEditorPane);

        scrollPane.setBorder(null);
        episodeEditorPane.setEditable(false);
        episodeEditorPane.setOpaque(false);
        episodeEditorPane.setFocusable(false);
        episodeEditorPane.setPreferredSize(new Dimension(400,150));

        // Make hyperlinks open in the browser
        episodeEditorPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent event) {
                if(event.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
                    if(Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(event.getURL().toURI());
                        }catch(java.net.URISyntaxException e){
                            e.printStackTrace(); //TODO change error printout
                        }catch(java.io.IOException e){
                            e.printStackTrace(); //TODO change error printout
                        }
                    }
                }
            }
        });

        // Add padding to the panel
        informationPanel.setBorder(new EmptyBorder(10,10,10,10));

        // Style the editorPane
        HTMLEditorKit kit = new HTMLEditorKit();
        episodeEditorPane.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("h1 {font-family: Avenir-Heavy,\"Helvetica Neue\",Helvetica,Arial,Sans-serif;" +
                "font-size: 20px;margin:0;padding:0;}");
        styleSheet.addRule("small {font-family: \"Helvetica Neue\",Helvetica,Arial,Sans-serif;" +
                "font-size: 10px;color:#575757;margin:0;padding:0;}");
        styleSheet.addRule("p {font-family: \"Helvetica Neue\",Helvetica,Arial,Sans-serif;" +
                "font-size: 12px;margin:0;padding:0;}");

        JPanel informationControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("X");
        closeButton.addActionListener(e -> setInformation(null));

        informationControls.add(closeButton);

        episodePanel.add(episodeIconLabel, BorderLayout.LINE_START);
        episodePanel.add(scrollPane, BorderLayout.CENTER);

        informationPanel.add(episodePanel, BorderLayout.CENTER);
        informationPanel.add(informationControls, BorderLayout.PAGE_START);
        informationPanel.setVisible(false);

        return informationPanel;
    }

    public void setChannel(ChannelObject channel){
        if(channel == null){
            if(channelPanel.isVisible()){
                channelPanel.setVisible(false);
            }
        }else {
            ImageIcon icon = new ImageIcon(channel.getImage().getScaledInstance(-1,64,Image.SCALE_SMOOTH));
            channelIconLabel.setIcon(icon);
            channelPanel.setOpaque(true);
            channelPanel.setBackground(Color.decode("#" + channel.getColor()));
            if(!channelPanel.isVisible()){
                channelPanel.setVisible(true);
            }
        }
    }

    public void setInformation(EpisodeObject episode){
        if(episode == null){
            if(informationPanel.isVisible()){
                informationPanel.setVisible(false);
            }
        }else{
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<h1>").append(episode.getTitle()).append("</h1>");
            // If the episode is currently playing
            if(episode.getEndTimeUtc().getTime() > new Date().getTime()){
                stringBuilder.append("<small>Sänds kl.");
            }else{
                stringBuilder.append("<small>Sändes kl.");
            }
            stringBuilder.append(new SimpleDateFormat("HH:mm").format(episode.getStartTimeUtc()))
                    .append("-")
                    .append(new SimpleDateFormat("HH:mm").format(episode.getEndTimeUtc()))
                    .append(", ")
                    .append(new SimpleDateFormat("EEEE", new Locale("sv")).format(episode.getEndTimeUtc()))
                    .append(" den ")
                    .append(new SimpleDateFormat("d/M YYYY").format(episode.getEndTimeUtc()))
                    .append("</small>");
            stringBuilder.append("<p>").append(episode.getSubtitle()).append("</p>");
            stringBuilder.append("<a href=\"").append(episode.getUrl().toString()).append("\">Lyssna här</a>");
            episodeEditorPane.setText(stringBuilder.toString());
            ImageIcon icon = new ImageIcon(episode.getImage().getScaledInstance(128,-1,Image.SCALE_SMOOTH));
            episodeIconLabel.setIcon(icon);

            if(!informationPanel.isVisible()){
                informationPanel.setVisible(true);
            }
        }
        // Updates frame size + more
        //frame.pack();
    }
}
