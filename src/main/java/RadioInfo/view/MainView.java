package RadioInfo.view;

import RadioInfo.ProgramTableModel.ProgramTableRenderer;
import RadioInfo.model.Channel;
import RadioInfo.model.Episode;
import RadioInfo.ProgramTableModel.ProgramTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
/**
 * The main view object of RadioInfo
 * @version 1.0
 * @author Isidor Nygren
 */
public class MainView {
    private final JFrame frame;
    private JEditorPane episodeEditorPane;
    private JEditorPane channelEditorPane;

    private JPanel informationPanel;
    private JPanel episodePanel;
    private JPanel channelPanel;
    private JLabel episodeIconLabel;
    private JLabel channelIconLabel;

    private MainMenuBar menuBar;
    private JTable table;
    private ProgramTableModel tableModel;
    private Channel channel;

    /**
     * Builds the main window
     * @param title the title of the main window
     */
    public MainView(String title){
        frame = new JFrame(title);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(300,300));

        frame.add(buildChannel(), BorderLayout.PAGE_START);
        frame.add(buildTable(), BorderLayout.CENTER);
        frame.add(buildInformation(), BorderLayout.PAGE_END);

        frame.pack();
    }

    /**
     * Sets the top menu bar of the main frame of the view
     * @param menuBar the MeinMenuBar item to put as the menu
     */
    public void setMenu(MainMenuBar menuBar){
        this.menuBar = menuBar;
        frame.setJMenuBar(menuBar.getMenuBar());
        frame.repaint();
    }

    /**
     * Sets if the main window is visible
     * @param visible bool if the window should be visible or not
     */
    public void setVisible(boolean visible){
        frame.setVisible(visible);
    }

    /**
     * Builds the schedule table that the episodes should be rendered to
     * @return the table
     */
    private JPanel buildTable(){
        JPanel panel = new JPanel(new BorderLayout());

        ArrayList<Episode> tempEpisode = new ArrayList<>();
        tableModel = new ProgramTableModel(tempEpisode, "000000");

        table = new JTable(tableModel);

        ProgramTableRenderer renderer = new ProgramTableRenderer();
        table.setDefaultRenderer(String.class, renderer);

        table.setGridColor(Color.LIGHT_GRAY);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setRowHeight(64);
        table.getColumn("Time").setMaxWidth(128);
        table.getColumn("Image").setMaxWidth(128);

        table.setShowVerticalLines(false);
        table.setTableHeader(null);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Listener for clicking on a cell in the table
        table.getSelectionModel().addListSelectionListener(event -> {
            if(tableModel.getRowCount() >= table.getSelectedRow() && table.getSelectedRow() >= 0 ){
                setInformation(tableModel.getRowValue(table.getSelectedRow()));
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);

        return panel;
    }

    /**
     * Builds the channel header bar that is rendered at the top of the
     * main window.
     * @return the panel to be rendered at the top
     */
    private JPanel buildChannel() {
        channelPanel = new JPanel(new BorderLayout());
        channelEditorPane = new JEditorPane();
        channelIconLabel = new JLabel();

        channelEditorPane.setEditable(false);
        channelEditorPane.setOpaque(false);
        channelEditorPane.setFocusable(false);

        channelPanel.add(channelIconLabel, BorderLayout.LINE_START);
        channelPanel.add(channelEditorPane, BorderLayout.CENTER);

        channelPanel.setVisible(false);

        return channelPanel;
    }

    /**
     * Builds the information window that holds a specific channels information
     * @return A Jpanel with information regarding a channel
     */
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
                            new ErrorDialog("Error", "episode URL syntax error", e);
                        }catch(java.io.IOException e){
                            new ErrorDialog("Error", "Could not open episode URL", e);
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

        episodePanel.add(episodeIconLabel, BorderLayout.LINE_START);
        episodePanel.add(scrollPane, BorderLayout.CENTER);

        informationPanel.add(episodePanel, BorderLayout.CENTER);
        informationPanel.setVisible(false);

        return informationPanel;
    }

    /**
     * fetches the menubar of the view so that the listener objects can be accessed.
     * @return the menubar
     */
    public MainMenuBar getMenuBar(){
        return this.menuBar;
    }

    /**
     * Sets the episodes in the main table
     * @param episodes the episodes to set the table to
     * @param color the color to render the time text in
     */
    public void setEpisodes(ArrayList<Episode> episodes, String color){
        tableModel.updateList(episodes, color);
        tableModel.setColor(color);
    }

    /**
     * Sets the channel of the main window, renders an image in the top left corner
     * and sets the color of the header bar
     * @param channel the channel to set to
     */
    public void setChannel(Channel channel){
        if(channel == null){
            if(channelPanel.isVisible()){
                channelPanel.setVisible(false);
            }
        }else {
            this.channel = channel;
            try{
                channel.loadImage();
                ImageIcon icon = new ImageIcon(channel.getImage().getScaledInstance(-1,64,Image.SCALE_SMOOTH));
                channelIconLabel.setIcon(icon);
                channelPanel.setOpaque(true);
                channelPanel.setBackground(Color.decode("#" + channel.getColor()));
                if(!channelPanel.isVisible()){
                    channelPanel.setVisible(true);
                }
            }catch(IOException e){
                new ErrorDialog("Error", "Could not load channel image", e);
            }
        }
    }

    /**
     * returns the current channel
     * @return the current channel
     */
    public Channel getChannel(){
        return this.channel;
    }

    /**
     * returns the episode table
     * @return the table
     */
    public ProgramTableModel getTable() {
        return this.tableModel;
    }

    /**
     * Sets the information in the main window to a specific episode
     * @param episode the episode to render information about
     */
    public void setInformation(Episode episode){
        if(episode == null){
            if(informationPanel.isVisible()){
                informationPanel.setVisible(false);
            }
        }else{
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<h1>").append(episode.getTitle()).append("</h1>");
            // If the episode is currently playing
            if(!episode.hasEnded()){
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
