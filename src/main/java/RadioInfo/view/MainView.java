package RadioInfo.view;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainView {
    private final JFrame frame;
    private final MainController controller;
    private JEditorPane editorPane;

    private JPanel informationPanel;
    private JPanel episodePanel;
    private JLabel iconLabel;

    public MainView(MainController controller, String title){
        this.controller = controller;

        frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(300,300));

        frame.setJMenuBar(buildMenuBar());
        frame.add(buildTable(), BorderLayout.CENTER);
        frame.add(buildInformation(), BorderLayout.PAGE_END);

        try {
            EpisodeObjectBuilder episodeObjectBuilder = new EpisodeObjectBuilder();
            EpisodeObject tempEpisode = episodeObjectBuilder.setImageUrl(new URL("http://static-cdn.sr.se/sida/images/2519/9e5f591c-c3a5-48a6-9458-ade46a3cbf12.jpg"))
                    .setTitle("Nazistattacken i Kärrtorp").setChannelId(1).setStartTimeUtc(new Date()).setEndTimeUtc(new Date()).setId(1).setImageUrlTemplate(new URL("http://static-cdn.sr.se/sida/images/2519/9e5f591c-c3a5-48a6-9458-ade46a3cbf12.jpg"))
                    .setProgramId(1).setSubtitle("Plötsligt hörs ett flyglarm. Svartklädda, maskerade män närmar sig. De kastar glasflaskor in mot torget. Snart pratar hela Sverige om nazistattacken i Kärrtorp.").setUrl(new URL("http://www.google.com/")).createEpisodeObject();
            tempEpisode.loadImage();
            setInformation(tempEpisode);
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
        JMenuItem menuItem, subMenu;

        subMenu = new JMenu("A submenu");
        subMenu.setMnemonic(KeyEvent.VK_S);

        menuItem = new JMenuItem("An item in the submenu");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_2, ActionEvent.ALT_MASK));
        subMenu.add(menuItem);

        menuItem = new JMenuItem("Another item");
        subMenu.add(menuItem);
        menuBar.add(subMenu);
        return menuBar;
    }

    private JPanel buildTable(){
        JPanel panel = new JPanel(new BorderLayout());
        Object rowData[][] = { { "Row1-Column1", "Row1-Column2", "Row1-Column3"},
                { "Row2-Column1", "Row2-Column2", "Row2-Column3"} };
        String columnNames[] = { "Column One", "Column Two", "Column Three"};
        ProgramTableModel tableModel = new ProgramTableModel(columnNames, rowData);

        JTable table = new JTable(tableModel);

        table.setGridColor(Color.LIGHT_GRAY);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);

        return panel;
    }

    private JPanel buildInformation(){
        informationPanel = new JPanel(new BorderLayout());
        episodePanel = new JPanel(new BorderLayout());
        editorPane = new JEditorPane();
        iconLabel = new JLabel();
        JScrollPane scrollPane = new JScrollPane(editorPane);

        scrollPane.setBorder(null);
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        editorPane.setFocusable(false);
        editorPane.setPreferredSize(new Dimension(400,150));

        // Make hyperlinks open in the browser
        editorPane.addHyperlinkListener(new HyperlinkListener() {
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
        editorPane.setEditorKit(kit);
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

        episodePanel.add(iconLabel, BorderLayout.LINE_START);
        episodePanel.add(scrollPane, BorderLayout.CENTER);

        informationPanel.add(episodePanel, BorderLayout.CENTER);
        informationPanel.add(informationControls, BorderLayout.PAGE_START);
        informationPanel.setVisible(false);

        return informationPanel;
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
            editorPane.setText(stringBuilder.toString());
            ImageIcon icon = new ImageIcon(episode.getImage().getScaledInstance(200,-1,Image.SCALE_SMOOTH));
            iconLabel.setIcon(icon);

            if(!informationPanel.isVisible()){
                informationPanel.setVisible(true);
            }
        }
        // Updates frame size + more
        //frame.pack();
    }
}
