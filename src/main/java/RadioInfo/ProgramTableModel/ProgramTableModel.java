package RadioInfo.ProgramTableModel;

import javax.swing.table.AbstractTableModel;

public class ProgramTableModel extends AbstractTableModel {
    private String[] columnNames;
    private Object[][] data;

    public ProgramTableModel(String[] columnNames, Object[][] data){
        this.columnNames = columnNames;
        this.data = data;
    }

    public int getRowCount() {
        return data.length;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int col){
        return columnNames[col];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public void setValueAt(Object value, int row, int col){
        data[row][col] = value;
    }
}
