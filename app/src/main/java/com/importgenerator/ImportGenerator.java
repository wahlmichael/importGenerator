package com.importgenerator;

import java.awt.Dimension;
import javax.swing.JScrollPane;

import javax.swing.*;

class ImportGenerator {
  JScrollPane currentForm;
  JTable table;
  TableModel tableModel;
  Config config = new Config();

  private void createAndShowGUI() {
    JFrame frame = new JFrame("Vandelay Industries");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 500);
    frame.setLayout(null);

    JLabel label = new JLabel("Generate your imports!");
    label.setBounds(20, 20, 200, 30);
    frame.add(label);

    // getting imports
    ImportGrabber importGrabber = new ImportGrabber(config.getFibNxPath());
    ImportType[] types = importGrabber.getImportTypes();

    JComboBox<ImportType> dropDown = new JComboBox<ImportType>(types);
    dropDown.setBounds(200, 100, 100, 50);

    // refactor later
    dropDown.addActionListener(x -> {
      ImportType importType = (ImportType) dropDown.getSelectedItem();
      JScrollPane form = generateForm(importType);
      if (currentForm != null) {
        frame.remove(currentForm);
      }
      currentForm = form;
      form.setBounds(50, 200, 800, 150);
      frame.add(form);
      frame.revalidate();
      frame.repaint();
    });
    frame.add(dropDown);

    JButton button = new JButton("click");
    button.setBounds(130, 100, 100, 40);

    // refactor later
    button.addActionListener(x -> {
      System.out.println("button clicked");

      CsvBuilder.createCsv(tableModel.getColumnsNames(), tableModel.getData());
    });
    frame.add(button);

    frame.setVisible(true);
    if (config.getFibNxPath().isEmpty()) {
      config.getFibNxPathFromUser();
    }
  }

  JScrollPane generateForm(ImportType importType) {
    Field[] fields = importType.getFields();
    tableModel = new TableModel(fields);
    table = new JTable(tableModel);
    table.setPreferredScrollableViewportSize(new Dimension(500, 70));
    JScrollPane scrollPane = new JScrollPane(table);

    return scrollPane;
  }

  public static void main(String[] args) {
    ImportGenerator importGenerator = new ImportGenerator();
    SwingUtilities.invokeLater(() -> importGenerator.createAndShowGUI());
    System.out.println("2");
  }
}
