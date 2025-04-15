package com.importgenerator;

import javax.swing.JScrollPane;
import javax.swing.*;

class ImportGenerator {
  JScrollPane currentForm;
  JTable table;
  TableModel tableModel;

  private void createAndShowGUI(Config config) {
    new GuiFrame(config);
  }

  public static void main(String[] args) {
    Config config = new Config();
    ImportGenerator importGenerator = new ImportGenerator();
    SwingUtilities.invokeLater(() -> importGenerator.createAndShowGUI(config));
  }
}
