package com.importgenerator;

import java.util.Arrays;

import javax.swing.table.AbstractTableModel;

class TableModel extends AbstractTableModel {
  private String[] columnNames;
  private String[] columnCodes;
  private Field[] fields;
  private String[][] data = {};

  TableModel(Field[] fields) {
    this.fields = fields;
    columnNames = new String[fields.length];
    columnCodes = new String[fields.length];
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      String columnName;
      if (field.getIsRequired()) {
        columnName = String.format("<html><strong>%s*</strong><br>%s</html>", field.getName(), field.getType());
      } else {
        columnName = String.format("<html>%s<br>%s</html>", field.getName(), field.getType());
      }
      columnNames[i] = columnName;
    }
    for (int j = 0; j < fields.length; j++) {
      columnCodes[j] = fields[j].getCode();
    }
    addDefaultRow();
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

  private void setData(String[][] newData) {
    this.data = newData;
  }

  public String[] getColumnCodes() {
    return columnCodes;
  }

  public void addDefaultRow() {
    String[][] copyOfData = Arrays.copyOfRange(data, 0, data.length + 1);
    String defaultValue;
    copyOfData[data.length] = new String[fields.length];
    for (int i = 0; i < fields.length; i++) {
      if (fields[i].getDefaultValue().length() > 0) {
        defaultValue = fields[i].getDefaultValue();
      } else {
        defaultValue = "";
      }
      if (defaultValue.equals("true")) {
        defaultValue = "1";
      } else if (defaultValue.equals("false")) {
        defaultValue = "0";
      }
      copyOfData[data.length][i] = defaultValue;
    }
    setData(copyOfData);
    this.fireTableRowsInserted(data.length, data.length);

  }

  public void deleteRow() {
    if (data.length == 1)
      return;
    String[][] copyOfData = Arrays.copyOfRange(data, 0, data.length - 1);
    setData(copyOfData);
    this.fireTableRowsDeleted(copyOfData.length - 1, copyOfData.length - 1);
  }

  public void duplicateRow() {
    String[][] copyOfData = Arrays.copyOfRange(data, 0, data.length + 1);
    String[] dupedRow = Arrays.copyOfRange(data[data.length - 1], 0, fields.length);
    copyOfData[data.length] = dupedRow;
    setData(copyOfData);
    this.fireTableRowsInserted(data.length, data.length);
  }
}
