package com.importgenerator;

import javax.swing.JScrollPane;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.*;

class ImportGenerator {
  JScrollPane currentForm;
  JTable table;
  TableModel tableModel;

  // frame.setVisible(true);
  // if (config.getFibNxPath().isEmpty()) {
  // config.getFibNxPathFromUser();
  // }

  private void createAndShowGUI(Config config) {
    ImportGrabber importGrabber = new ImportGrabber(config.getFibNxPath());
    ImportType[] types = importGrabber.getImportTypes();
    new GuiFrame(config, types);
  }

  public static void main(String[] args) {
    Config config = new Config();
    ImportGenerator importGenerator = new ImportGenerator();
    for (LookAndFeelInfo lafInfo : UIManager.getInstalledLookAndFeels()) {
      System.out.println(lafInfo.getClassName());
    }
    try {
      UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
    SwingUtilities.invokeLater(() -> importGenerator.createAndShowGUI(config));
  }
}
