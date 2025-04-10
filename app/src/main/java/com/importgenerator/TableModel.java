package com.importgenerator;

import javax.swing.table.AbstractTableModel;

class TableModel extends AbstractTableModel {
  private String[] columnNames;
  private String[][] data = { { "something", "124345" } };

  TableModel(Field[] fields) {
    columnNames = new String[fields.length];
    for (int i = 0; i < fields.length; i++) {
      columnNames[i] = fields[i].getName();
    }
  }

  public int getColumnCount() {
    return columnNames.length;
  }

  public int getRowCount() {
    return data.length;
  }

  public Object getValueAt(int row, int col) {
    return data[row][col];
  }

  public String getColumnName(int columnIndex) {
    return columnNames[columnIndex];
  }

  public boolean isCellEditable(int row, int col) {
    return true;
  }

  public void setValueAt(Object value, int row, int col) {
    data[row][col] = value.toString();
    fireTableCellUpdated(row, col);
  }

  public String[] getColumnsNames() {
    return columnNames;
  }

  public String[][] getData() {
    return data;
  }
}
