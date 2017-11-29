package RadioInfo.ProgramTableModel;

import RadioInfo.model.Episode;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Table model used to print episode objects in an orderly fashion
 * @version 1.0
 * @author Isidor Nygren
 */
public class ProgramTableModel extends AbstractTableModel {
    private String[] columnNames;
    private List<Episode> episodes;
    private String color;

    /**
     * Builds a new table model
     * @param episodes list of episode objects to be visualised
     * @param color the channel color the episodes are connected to, will be used to
     *              render the text of the starting time of the episode
     */
    public ProgramTableModel(List<Episode> episodes, String color){
        this.episodes = episodes;
        this.color = color;
        this.columnNames = new String[]{
                "Time",
                "Image",
                "Description"
        };
    }

    /**
     * Updates the list of episodes with a new list of episode and a new color
     * @param episodes the list of episodes
     * @param color hex value of the channel e.g. "000000"
     */
    public void updateList(List<Episode> episodes, String color){
       this.episodes = episodes;
       this.color = color;
       fireTableDataChanged();
    }

    /**
     * Gets the total amount of rows in the table
     * @return the row count
     */
    public int getRowCount() {
        return this.episodes.size();
    }

    /**
     * Gets the total amount of columns in the table
     * @return the column count
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Returns the header of the column
     * @param col column position
     * @return the header string
     */
    public String getColumnName(int col){
        return columnNames[col];
    }

    /**
     * Returns the class type of each column, used by the table renderer
     * to epply specific patterns to specific classes in each column.
     * @param col the columns position
     * @return the class in the column
     */
    @Override
    public Class<?> getColumnClass(int col) {
        switch (col){
            case 0:
                return Integer.class;
            case 1:
                return ImageIcon.class;
            case 2:
                return String.class;
            default: return null;
        }
    }

    /**
     * Returns the episode at the row
     * @param row the position of the row
     * @return an Episode object.
     */
    public Episode getRowValue(int row){
        return episodes.get(row);
    }

    /**
     * Returns the value at a specific point in the table, handles the rendering of
     * the text in html.
     * @param row position of the row
     * @param col position of the column
     * @return an HTML string or an image
     */
    public Object getValueAt(int row, int col) {
        Episode episode = episodes.get(row);
        switch(col){
            case 0:
                String time = "<html><h2 style='color:#" + this.color + ";'>" + new SimpleDateFormat("HH:mm", new Locale("sv"))
                        .format(episode.getStartTimeUtc()) + "</h2></html>";
                return time;
            case 1:
                return new ImageIcon(episode.getImage().getScaledInstance(-1,50,Image.SCALE_SMOOTH));
            case 2:
                String returnString = "<html><h3 style='margin:0;padding:0;'>" + episode.getTitle() + "</h3>";
                if(episode.getSubtitle() != null){
                    returnString += "<p>" + episode.getSubtitle() + "</p></html>";
                }
                return returnString;
            default: return null;
        }
    }

    /**
     * Sets the channel color of the table
     * @param color hex value of the color e.g "000000"
     */
    public void setColor(String color){
        this.color = color;
        fireTableDataChanged();
    }

    /**
     * Sets a new value at the specific column and row
     * @param value the new value to be set
     * @param row row position
     * @param col column position
     */
    public void setValueAt(Object value, int row, int col){
        if(value != null){
            switch(col){
                case 0:
                    episodes.get(row).setStartTimeUtc((Date)value);
                    break;
                case 1:
                    episodes.get(row).setImage((Image)value);
                    break;
                case 2:
                    episodes.get(row).setTitle((String)value);
                    break;
            }
        }
        fireTableCellUpdated(row, col);
    }
}
