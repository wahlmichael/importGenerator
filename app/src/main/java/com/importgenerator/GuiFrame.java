package com.importgenerator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.desktop.QuitEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.JTableHeader;
import javax.imageio.ImageIO;

public class GuiFrame extends JFrame {
  JPanel formPanel;
  JScrollPane formScrollPane;
  TableModel tableModel;
  JTable table;
  JComboBox<ImportType> comboBox;
  GridBagConstraints constraints;
  ImportType[] importTypes;
  Config config;
  ImportGrabber importGrabber;
  JOptionPane locateFilesErrorOptionPane;

  GuiFrame(Config config) {
    setFonts();
    this.config = config;
    build();
    setVisible(true);
  }

  private void setImportTypes(ImportType[] importTypes) {
    this.importTypes = importTypes;
  }

  public void build() {
    constraints = new GridBagConstraints();
    setImportTypesFromConfig();
    while (importTypes == null || importTypes.length < 1 || config.getFibNxPath().length() < 1) {
      JOptionPane.showMessageDialog(new JOptionPane(), "Please select your fibNx repo");
      config.getFibNxPathFromUser();
      setImportTypesFromConfig();
    }
    setupFrame();
    buildLogoAndComboBoxRow();
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

  private void buildLogoAndComboBoxRow() {
    JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));

    JPanel comboPanel = new JPanel();
    comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.PAGE_AXIS));
    JLabel comboBoxLabel = new JLabel("Select import type");
    comboBoxLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    comboBox = new JComboBox<ImportType>(importTypes);
    comboBox.addActionListener(handleComboBoxUpdated());
    comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
    comboPanel.add(comboBoxLabel);
    comboPanel.add(Box.createVerticalStrut(5));
    comboPanel.add(comboBox);

    JLabel logoLabel;
    try {
      BufferedImage logo = ImageIO.read(getClass().getClassLoader().getResourceAsStream("vandelay.png"));
      Image scaled = logo.getScaledInstance(100, -1, Image.SCALE_SMOOTH);
      logoLabel = new JLabel(new ImageIcon(scaled));
    } catch (IOException e) {
      System.err.println(e);
      logoLabel = new JLabel("Vandelay Industries");
    }

    rowPanel.add(comboPanel);
    rowPanel.add(logoLabel);

    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.gridwidth = 2;
    constraints.anchor = GridBagConstraints.CENTER;
    this.add(rowPanel, constraints);
  }

  private void buildScrollPane() {
    table = new JTable();
    formScrollPane = new JScrollPane(table);
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.gridwidth = 2;
    constraints.ipady = 400;
    constraints.ipadx = 1100;
    this.add(formScrollPane, constraints);
  }

  private void createTable(ImportType importType) {
    tableModel = new TableModel(importType.getFields());
    table.setModel(tableModel);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    table.setFont(new Font("Helvetica", Font.PLAIN, 14));
    table.setShowGrid(true);
    table.setGridColor(Color.LIGHT_GRAY);
    table.setSelectionBackground(new Color(232, 242, 254));
    table.setSelectionForeground(Color.BLACK);
    JTableHeader header = table.getTableHeader();
    header.setFont(new Font("Helvetica", Font.PLAIN, 14));
    header.setPreferredSize(new Dimension(175, 55));
    this.setColumnWidths(175);
    this.revalidate();
    this.repaint();
  }

  private void buildExportImportButton() {
    JButton exportImportButton = new JButton("Export Import");
    constraints.gridx = 1;
    constraints.gridy = 2;
    constraints.ipadx = 0;
    constraints.ipady = 0;
    exportImportButton.addActionListener(handleExportImportButtonClicked());
    this.add(exportImportButton, constraints);
  }

  private void buildAddRowButton() {
    JButton addRowButton = new JButton("Add Row");
    constraints.gridx = 0;
    constraints.gridy = 2;
    constraints.ipadx = 0;
    constraints.ipady = 0;
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

  //
  // Helper methods
  //

  private void setColumnWidths(int newWidth) {
    for (int column = 0; column < table.getColumnCount(); column++) {
      TableColumn tableColumn = table.getColumnModel().getColumn(column);
      tableColumn.setPreferredWidth(newWidth);
    }
    table.setRowHeight(40);
  }

  private void setImportTypesFromConfig() {
    importGrabber = new ImportGrabber(config.getFibNxPath());
    this.setImportTypes(importGrabber.getImportTypes());
  }

  private void setFonts() {
    UIManager.put("Label.font", new Font("Verdana", Font.PLAIN, 14));
    UIManager.put("Button.font", new Font("Verdana", Font.BOLD, 14));
    UIManager.put("Table.font", new Font("Verdana", Font.PLAIN, 13));
    UIManager.put("Table.rowHeight", 24);
  }
}
