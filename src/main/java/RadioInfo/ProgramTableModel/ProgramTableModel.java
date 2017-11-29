package RadioInfo.ProgramTableModel;

import RadioInfo.model.Episode;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgramTableModel extends AbstractTableModel {
    private String[] columnNames;
    private List<Episode> episodes;
    private String color;

    public ProgramTableModel(List<Episode> episodes, String color){
        this.episodes = episodes;
        this.color = color;
        this.columnNames = new String[]{
                "Time",
                "Image",
                "Description"
        };
    }

    public void updateList(List<Episode> episodes, String color){
       this.episodes = episodes;
       this.color = color;
       fireTableDataChanged();
    }

    public int getRowCount() {
        return this.episodes.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int col){
        return columnNames[col];
    }

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

    public Episode getRowValue(int row){
        return episodes.get(row);
    }

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

    public void setColor(String color){
        this.color = color;
        fireTableDataChanged();
    }


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
