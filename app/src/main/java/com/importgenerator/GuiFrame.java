package com.importgenerator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class GuiFrame extends JFrame {
  JPanel formPanel;
  JScrollPane formScrollPane;
  JButton exportImportButton;
  JButton addRowButton;
  JLabel frameLabel;
  JLabel comboBoxLabel;
  TableModel tableModel;
  JTable table;
  JComboBox<ImportType> comboBox;
  GridBagConstraints constraints;
  ImportType[] importTypes;

  GuiFrame(ImportType[] importTypes) {
    this.importTypes = importTypes;
    build();
    setVisible(true);
  }

  public void build() {
    constraints = new GridBagConstraints();
    setupFrame();
    buildframeLabel();
    buildComboBox();
    buildScrollPane();
    buildExportImportButton();
    buildAddRowButton();
  }

  private void setupFrame() {
    setTitle("Vandelay Industries");
    setSize(1250, 800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new GridBagLayout());
    setLocationRelativeTo(null);

  }

  private void buildframeLabel() {
    frameLabel = new JLabel("For your importing/exporting needs");
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = .5;
    this.add(frameLabel, constraints);
  }

  private void buildComboBox() {
    comboBoxLabel = new JLabel("Select import type");
    constraints.gridx = 2;
    constraints.gridy = 0;
    this.add(comboBoxLabel, constraints);
    comboBox = new JComboBox<ImportType>(importTypes);
    constraints.gridx = 2;
    constraints.gridy = 1;
    constraints.weightx = .5;
    comboBox.addActionListener(handleComboBoxUpdated());
    this.add(comboBox, constraints);
  }

  private void buildScrollPane() {
    formScrollPane = new JScrollPane(table);
    constraints.gridx = 0;
    constraints.gridy = 2;
    constraints.gridwidth = 3;
    constraints.weightx = 1;
    constraints.ipady = 400;
    constraints.ipadx = 1100;
    this.add(formScrollPane, constraints);
  }

  private void createTable(ImportType importType) {
    this.remove(formScrollPane);
    tableModel = new TableModel(importType.getFields());
    table = new JTable(tableModel);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    for (int column = 0; column < table.getColumnCount(); column++) {
      TableColumn tableColumn = table.getColumnModel().getColumn(column);
      tableColumn.setPreferredWidth(200);
    }
    table.setRowHeight(30);
    buildScrollPane();
    this.revalidate();
    this.repaint();
  }

  private void buildExportImportButton() {
    exportImportButton = new JButton("Export Import");
    constraints.gridx = 2;
    constraints.gridy = 3;
    constraints.ipadx = 0;
    constraints.ipady = 0;
    exportImportButton.addActionListener(handleExportImportButtonClicked());
    this.add(exportImportButton, constraints);
  }

  private void buildAddRowButton() {
    addRowButton = new JButton("Add Row");
    constraints.gridx = 0;
    constraints.gridy = 3;
    addRowButton.addActionListener(handleAddRowButtonClicked());
    this.add(addRowButton, constraints);
  }

  private ActionListener handleComboBoxUpdated() {
    ActionListener action = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ImportType selectedImportType = (ImportType) comboBox.getSelectedItem();
        createTable(selectedImportType);
      }
    };
    return action;
  }

  private ActionListener handleExportImportButtonClicked() {
    ActionListener action = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        CsvBuilder.createCsv(tableModel.getColumnCodes(), tableModel.getData());
      }
    };
    return action;
  }

  private ActionListener handleAddRowButtonClicked() {
    ActionListener action = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        tableModel.addDefaultRow();
      }
    };
    return action;
  }
}
