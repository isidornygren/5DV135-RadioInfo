package RadioInfo.ProgramTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ProgramTableRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRenderer(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        ProgramTableModel programTableModel = (ProgramTableModel)table.getModel();
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(programTableModel.getRowValue(row).hasEnded()){
            c.setBackground(Color.GRAY);
        }else{
            c.setBackground(Color.white);
        }
        return c;
    }
}
