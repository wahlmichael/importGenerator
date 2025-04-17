package com.importgenerator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
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
  JLabel exportingToLabel;
  JButton exportImportButton;
  JLabel statusLabel;

  private static final Color TEXT_COLOR = Color.WHITE;

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
    buildBottomButtonRow();
    buildExportLocationPanel();
    buildStatusBar();
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
    comboBox = new JComboBox<>();
    comboBox.addItem(null);

    for (ImportType type : importTypes) {
      comboBox.addItem(type);
    }

    comboBox.setRenderer(getCustomComboBoxRenderer());

    comboBox.addActionListener(handleComboBoxUpdated());
    comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
    comboPanel.add(comboBoxLabel);
    comboPanel.add(Box.createVerticalStrut(14));
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
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.weightx = 1.0;
    constraints.anchor = GridBagConstraints.CENTER;
    this.add(rowPanel, constraints);
  }

  private void buildScrollPane() {
    table = new JTable();
    formScrollPane = new JScrollPane(table);
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.gridwidth = 2;
    constraints.fill = GridBagConstraints.BOTH;
    constraints.ipady = 400;
    constraints.ipadx = 1100;
    constraints.insets = new Insets(10, 30, 10, 30);
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

  private void createEmptyTable() {
    table.setModel(new DefaultTableModel());
  }

  private void buildBottomButtonRow() {
    JPanel mainPanel = new JPanel(new BorderLayout());

    // Setup left panel
    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
    JButton addRowButton = createAddRowButton();
    JButton duplicateRowButton = createDuplicateRowButton();
    JButton deleteRowButton = createDeleteRowButton();
    leftPanel.add(addRowButton);
    leftPanel.add(duplicateRowButton);
    leftPanel.add(deleteRowButton);

    // Setup right panel
    JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
    JButton exportImportButton = createExportImportButton();
    rightPanel.add(exportImportButton);

    // Setup main panel
    mainPanel.add(leftPanel, BorderLayout.WEST);
    mainPanel.add(rightPanel, BorderLayout.EAST);
    constraints.gridx = 0;
    constraints.gridy = 2;
    constraints.gridwidth = 2;
    constraints.ipadx = 0;
    constraints.ipady = 0;
    constraints.weightx = 1.0;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets(20, 30, 20, 30);
    this.add(mainPanel, constraints);
  }

  private void buildExportLocationPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    String exportingToLabelText;
    if (config.getOutputPath() != null && config.getOutputPath().length() > 1) {
      exportingToLabelText = "Outputting to: " + config.getOutputPath();
    } else {
      exportingToLabelText = "Select output folder";
    }
    exportingToLabel = new JLabel(exportingToLabelText);
    JButton updateExportFolderButton = createUpdateExportFolderButton();

    panel.add(exportingToLabel, BorderLayout.CENTER);
    panel.add(Box.createHorizontalStrut(10));
    panel.add(updateExportFolderButton, BorderLayout.EAST);

    constraints.gridx = 0;
    constraints.gridy = 3;
    constraints.gridwidth = 1;
    constraints.ipadx = 0;
    constraints.ipady = 0;
    constraints.weightx = 1.0;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets(20, 40, 20, 30);
    this.add(panel, constraints);
  }

  private void buildStatusBar() {
    statusLabel = new JLabel(Quotes.getRandomQuote());
    statusLabel.setFont(new Font("Verdana", Font.ITALIC, 12));
    statusLabel.setForeground(new Color(100, 100, 100));
    statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

    JPanel statusPanel = new JPanel(new BorderLayout());
    statusPanel.add(statusLabel, BorderLayout.EAST);

    constraints.gridx = 1;
    constraints.gridy = 3;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets(0, 20, 10, 30);

    this.add(statusPanel, constraints);
  }

  // Buttons

  private JButton createAddRowButton() {
    JButton addRowButton = new StyledButton("Add Row",
        new Color(56, 158, 102),
        new Color(78, 185, 124),
        TEXT_COLOR);
    addRowButton.addActionListener(handleAddRowButtonClicked());
    return addRowButton;
  }

  private JButton createDeleteRowButton() {
    JButton deleteRowButton = new StyledButton("Delete",
        new Color(165, 91, 75),
        new Color(185, 107, 91),
        TEXT_COLOR);
    deleteRowButton.addActionListener(handleDeleteRowButtonClicked());
    return deleteRowButton;
  }

  private JButton createDuplicateRowButton() {
    JButton duplicateRowButton = new StyledButton("Duplicate",
        new Color(75, 105, 162),
        new Color(95, 128, 190),
        TEXT_COLOR);
    duplicateRowButton.addActionListener(handleDuplicateRowButtonClicked());
    return duplicateRowButton;
  }

  private JButton createExportImportButton() {
    exportImportButton = new StyledButton("Export Import",
        new Color(220, 160, 109),
        new Color(240, 180, 115),
        Color.BLACK);
    exportImportButton.addActionListener(handleExportImportButtonClicked());
    exportImportButton.setEnabled(false);
    return exportImportButton;
  }

  private JButton createUpdateExportFolderButton() {
    JButton updateExportFolderButton = new StyledButton("Update output folder",
        new Color(220, 160, 109),
        new Color(240, 180, 115),
        Color.BLACK);
    updateExportFolderButton.addActionListener(handleUpdateExportFolderButtonClicked());
    return updateExportFolderButton;
  }

  //
  // Action Listeners
  //

  private ActionListener handleComboBoxUpdated() {
    ActionListener action = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        exportButtonEnabler();
        if (comboBox.getSelectedItem() == null) {
          createEmptyTable();
          return;
        }
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
        if (table.isEditing()) {
          table.getCellEditor().stopCellEditing();
        }
        CsvBuilder.createCsv(tableModel.getColumnCodes(), tableModel.getData(), config.getOutputPath(),
            comboBox.getSelectedItem().toString());
        if (CsvBuilder.wasLastSuccess) {
          setStatus("Import exported", new Color(56, 158, 102));
        } else {
          setStatus("Import export failed", new Color(165, 91, 75));
        }
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

  private ActionListener handleDeleteRowButtonClicked() {
    ActionListener action = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (table.isEditing()) {
          table.getCellEditor().stopCellEditing();
        }
        tableModel.deleteRow();
      }
    };
    return action;
  }

  private ActionListener handleDuplicateRowButtonClicked() {
    ActionListener action = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (table.isEditing()) {
          table.getCellEditor().stopCellEditing();
        }
        tableModel.duplicateRow();
      }
    };
    return action;
  }

  private ActionListener handleUpdateExportFolderButtonClicked() {
    ActionListener action = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        config.getOutputPathFromUser();
        exportButtonEnabler();
        exportingToLabel.setText("Outputting to: " + config.getOutputPath());
        exportingToLabel.revalidate();
        exportingToLabel.repaint();
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

  private DefaultListCellRenderer getCustomComboBoxRenderer() {
    DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
          boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setFont(new Font("Verdana", Font.PLAIN, 13));
        if (value == null) {
          setText("Select import type");
        } else {
          setText(value.toString());
        }
        return component;
      }
    };
    return renderer;
  }

  private void exportButtonEnabler() {
    if (comboBox.getSelectedItem() != null && config.getOutputPath() != null) {
      exportImportButton.setEnabled(true);
    } else {
      exportImportButton.setEnabled(false);
    }
  }

  private void setStatus(String message, Color color) {
    statusLabel.setText(message);
    statusLabel.setForeground(color);

    new Timer(4000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        statusLabel.setText(Quotes.getRandomQuote());
        statusLabel.setForeground(new Color(100, 100, 100));
        ((Timer) e.getSource()).stop();
      }
    }).start();
  }
}
