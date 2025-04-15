package com.importgenerator;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class GuiFrame extends JFrame {
  JPanel formPanel;
  JScrollPane formScrollPane;
  TableModel tableModel;
  JTable table;
  JComboBox<ImportType> comboBox;
  GridBagConstraints constraints;
  ImportType[] importTypes;
  Config config;

  GuiFrame(Config config, ImportType[] importTypes) {
    this.importTypes = importTypes;
    this.config = config;
    build();
    setVisible(true);
  }

  public void build() {
    constraints = new GridBagConstraints();
    setupFrame();
    buildComboBoxSection();
    buildDirSelectorSection();
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

  private void buildComboBoxSection() {
    JPanel comboBoxPanel = new JPanel();
    comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.PAGE_AXIS));
    JLabel comboBoxLabel = new JLabel("Select import type");
    comboBox = new JComboBox<ImportType>(importTypes);
    comboBox.addActionListener(handleComboBoxUpdated());
    constraints.gridx = 0;
    constraints.gridy = 0;
    comboBoxPanel.add(comboBoxLabel);
    comboBoxPanel.add(comboBox);
    this.add(comboBoxPanel, constraints);

  }

  private void buildDirSelectorSection() {
    JPanel dirSelectorPanel = new JPanel();
    dirSelectorPanel.setLayout(new BoxLayout(dirSelectorPanel, BoxLayout.PAGE_AXIS));
    JLabel setFibDirLabel = new JLabel(String.format("Getting imports from: %s", config.getFibNxPath()));
    JButton setFibDirButton = new JButton("Update FibNx repo location");
    setFibDirButton.addActionListener(handleSetFibDirButtonClicked());
    constraints.gridx = 1;
    constraints.gridy = 0;
    dirSelectorPanel.add(setFibDirLabel);
    dirSelectorPanel.add(setFibDirButton);
    this.add(dirSelectorPanel, constraints);
  }

  private void buildScrollPane() {
    table = new JTable();
    formScrollPane = new JScrollPane(table);
    constraints.gridx = 0;
    constraints.gridy = 2;
    constraints.gridwidth = 3;
    constraints.ipady = 400;
    constraints.ipadx = 1100;
    this.add(formScrollPane, constraints);
  }

  private void createTable(ImportType importType) {
    tableModel = new TableModel(importType.getFields());
    table.setModel(tableModel);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    this.setColumnWidths(200);
    this.revalidate();
    this.repaint();
  }

  private void buildExportImportButton() {
    JButton exportImportButton = new JButton("Export Import");
    constraints.gridx = 1;
    constraints.gridy = 3;
    constraints.ipadx = 0;
    constraints.ipady = 0;
    exportImportButton.addActionListener(handleExportImportButtonClicked());
    this.add(exportImportButton, constraints);
  }

  private void buildAddRowButton() {
    JButton addRowButton = new JButton("Add Row");
    constraints.gridx = 0;
    constraints.gridy = 3;
    addRowButton.addActionListener(handleAddRowButtonClicked());
    this.add(addRowButton, constraints);
  }

  //
  // Action Listeners
  //

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

  private ActionListener handleSetFibDirButtonClicked() {
    ActionListener action = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        config.getFibNxPathFromUser();
      }
    };
    return action;
  }

  //
  // Helper methods
  //

  private void setColumnWidths(int newWidth) {
    for (int column = 0; column < table.getColumnCount(); column++) {
      TableColumn tableColumn = table.getColumnModel().getColumn(column);
      tableColumn.setPreferredWidth(newWidth);
    }
    table.setRowHeight(30);
  }
}
