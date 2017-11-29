package RadioInfo.view;

import RadioInfo.model.Channel;
import RadioInfo.model.Episode;
import RadioInfo.ProgramTableModel.ProgramTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainView {
    private final JFrame frame;
    private JEditorPane episodeEditorPane;
    private JEditorPane channelEditorPane;

    private JPanel informationPanel;
    private JPanel episodePanel;
    private JPanel channelPanel;
    private JLabel episodeIconLabel;
    private JLabel channelIconLabel;

    private ChannelMenuBar menuBar;
    private JTable table;
    private ProgramTableModel tableModel;
    private Channel channel;


    public MainView(String title){
        frame = new JFrame(title);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(300,300));
        menuBar = new ChannelMenuBar();

        frame.setJMenuBar(menuBar.getMenuBar());
        frame.add(buildChannel(), BorderLayout.PAGE_START);
        frame.add(buildTable(), BorderLayout.CENTER);
        frame.add(buildInformation(), BorderLayout.PAGE_END);

        frame.pack();
    }

    public void setVisible(boolean visible){
        frame.setVisible(visible);
    }

    private JPanel buildTable(){
        JPanel panel = new JPanel(new BorderLayout());

        ArrayList<Episode> tempEpisode = new ArrayList<>();
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
                if(tableModel.getRowCount() >= table.getSelectedRow() && table.getSelectedRow() >= 0 ){
                    setInformation(tableModel.getRowValue(table.getSelectedRow()));
                }
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
        //informationPanel.add(informationControls, BorderLayout.PAGE_START);//TODO remove all of this
        informationPanel.setVisible(false);

        return informationPanel;
    }


    public ChannelMenuBar getMenuBar(){
        return this.menuBar;
    }

    public void setEpisodes(ArrayList<Episode> episodes, String color){
        tableModel.updateList(episodes, color);
        tableModel.setColor(color);
    }

    public void setChannel(Channel channel){
        if(channel == null){
            if(channelPanel.isVisible()){
                channelPanel.setVisible(false);
            }
        }else {
            this.channel = channel;
            ImageIcon icon = new ImageIcon(channel.getImage().getScaledInstance(-1,64,Image.SCALE_SMOOTH));
            channelIconLabel.setIcon(icon);
            channelPanel.setOpaque(true);
            channelPanel.setBackground(Color.decode("#" + channel.getColor()));
            if(!channelPanel.isVisible()){
                channelPanel.setVisible(true);
            }
        }
    }

    public Channel getChannel(){
        return this.channel;
    }

    public void setInformation(Episode episode){
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
            if(episode.getSubtitle() != null){
                stringBuilder.append("<p>").append(episode.getSubtitle()).append("</p>");
            }
            if(episode.getDescription() != null){
                stringBuilder.append("<p>").append(episode.getDescription()).append("</p>");
            }
            if(episode.getUrl() != null){
                stringBuilder.append("<a href=\"").append(episode.getUrl().toString()).append("\">Lyssna här</a>");
            }
            episodeEditorPane.setText(stringBuilder.toString());
            ImageIcon icon = new ImageIcon(episode.getImage().getScaledInstance(128,-1,Image.SCALE_SMOOTH));
            episodeIconLabel.setIcon(icon);

            if(!informationPanel.isVisible()){
                informationPanel.setVisible(true);
            }
        }
        // Updates frame size + more
        frame.pack();
    }
}
