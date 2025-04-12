package com.importgenerator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;

public class Config {
  private String fibNxPath;
  private String rootPath;
  private String appConfigPath;
  private Properties configProps;
  final private String FIB_NX_PATH_KEY = "fibNxPath";

  Config() {
    fibNxPath = "";
    rootPath = System.getProperty("user.dir");
    appConfigPath = rootPath + "/config/app.properties";
    configProps = new Properties();
    getFibNxPathFromConfig();
  }

  public String getFibNxPath() {
    return this.fibNxPath;
  }

  public void setFibNxPath(String fibNxPath) {
    this.fibNxPath = fibNxPath;
  }

  private void getFibNxPathFromConfig() {
    String nxPath;
    try {
      configProps.load(new FileInputStream(appConfigPath));
    } catch (IOException e) {
      System.err.println(e);
    }
    nxPath = configProps.getProperty(FIB_NX_PATH_KEY);
    setFibNxPath(nxPath);
  }

  public void getFibNxPathFromUser() {
    String nxPath;
    final JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fc.showOpenDialog(null);
    nxPath = fc.getSelectedFile().toString();

    try {
      FileOutputStream outputStream = new FileOutputStream(appConfigPath);
      configProps.setProperty(FIB_NX_PATH_KEY, nxPath);
      configProps.store(outputStream, "properties");
      outputStream.close();
    } catch (IOException e) {
      System.err.println(e);
    }

    setFibNxPath(nxPath);
  }
}
