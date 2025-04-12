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
    JFrame frame = new JFrame("HelloWorldSwing");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 500);
    frame.setLayout(null);

    JLabel label = new JLabel("Generate your imports!");
    label.setBounds(20, 20, 200, 30);
    frame.add(label);

    ImportType[] types = createImportTypes();
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
      form.setBounds(50, 200, 300, 150);
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

  private ImportType[] createImportTypes() {
    Field field1 = new Field("Primary Identifier", "Number");
    Field field2 = new Field("Client Identifier", "String");
    Field field3 = new Field("Primary Identifier", "Number");
    Field field4 = new Field("Program Id", "String");
    Field[] fields1 = { field1, field2 };
    Field[] fields2 = { field3, field4 };
    ImportType type1 = new ImportType("RAZR Common Account Import", fields1);
    ImportType type2 = new ImportType("RAZR Common Person Import", fields2);
    ImportType[] types = { type1, type2 };
    return types;
  }

  public static void main(String[] args) {
    ImportGenerator importGenerator = new ImportGenerator();
    SwingUtilities.invokeLater(() -> importGenerator.createAndShowGUI());
    System.out.println("2");
  }
}
