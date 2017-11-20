package RadioInfo.view;

import RadioInfo.EpisodeObject;
import RadioInfo.EpisodeObjectBuilder;
import RadioInfo.ProgramTableModel.ProgramTableModel;
import RadioInfo.controller.MainController;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Date;

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

        //setInformation("Information about the current program, Testing multiple lines and scaleability\n What happens if this line is even longer? Nobody really knows.");
        URL imageUrl;
        EpisodeObjectBuilder episodeBuilder = new EpisodeObjectBuilder();
        try {
            EpisodeObjectBuilder episodeObjectBuilder = new EpisodeObjectBuilder();
            EpisodeObject tempEpisode = episodeObjectBuilder.setImageUrl(new URL("http://image.flaticon.com/icons/png/128/291/291201.png"))
                    .setTitle("Episode title").setChannelId(1).setStartTimeUtc(new Date()).setEndTimeUtc(new Date()).setId(1).setImageUrlTemplate(new URL("http://image.flaticon.com/icons/png/128/291/291201.png"))
                    .setProgramId(1).setSubtitle("Subtitle").setUrl(new URL("http://www.google.com/")).createEpisodeObject();
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

        // Style the editorPane
        HTMLEditorKit kit = new HTMLEditorKit();
        editorPane.setEditorKit(kit);

        JPanel informationControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("close");
        closeButton.addActionListener(e -> setInformation(null));

        informationControls.add(closeButton);

        episodePanel.add(iconLabel, BorderLayout.LINE_START);
        episodePanel.add(scrollPane, BorderLayout.CENTER);

        informationPanel.add(episodePanel, BorderLayout.CENTER);
        informationPanel.add(informationControls, BorderLayout.PAGE_END);
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
            stringBuilder.append("<h2>").append(episode.getSubtitle()).append("</h2>");
            editorPane.setText(stringBuilder.toString());
            ImageIcon icon = new ImageIcon(episode.getImage());
            iconLabel.setIcon(icon);

            if(!informationPanel.isVisible()){
                informationPanel.setVisible(true);
            }
        }
        frame.pack();
    }
}
