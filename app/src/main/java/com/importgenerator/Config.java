package com.importgenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JFileChooser;

public class Config {
  private String fibNxPath;
  private String outputPath;
  private Properties configProps;
  private final String FIB_NX_PATH_KEY = "fibNxPath";
  private final String OUTPUT_PATH_KEY = "outputPath";
  private final String PATH_TO_IMPORTS = "/apps/lmp-server/designs/fiRewards";
  private final Path writableConfigPath = Paths
      .get(System.getProperty("user.home"), ".importgenerator", "app.properties");
  private final String appConfigPath = "config/app.properties";

  Config() {
    configProps = new Properties();
    loadConfig();
    fibNxPath = configProps.getProperty(FIB_NX_PATH_KEY, "");
    outputPath = configProps.getProperty(OUTPUT_PATH_KEY, "");
  }

  public String getFibNxPath() {
    return this.fibNxPath;
  }

  public void setFibNxPath(String fibNxPath) {
    this.fibNxPath = fibNxPath;
  }

  public String getOutputPath() {
    return this.outputPath;
  }

  public void setOutputPath(String outputPath) {
    this.outputPath = outputPath;
  }

  private void loadConfig() {
    InputStream inputStream = null;
    try {
      if (Files.exists(writableConfigPath)) {
        inputStream = Files.newInputStream(writableConfigPath);
        System.out.println("Reading config from " + writableConfigPath);
      } else {
        inputStream = getClass().getClassLoader().getResourceAsStream(appConfigPath);
        System.out.println("Reading default config from classpath: " + appConfigPath);
      }

      if (inputStream != null) {
        configProps.clear();
        configProps.load(inputStream);
        System.out.println("Loaded config");
      } else {
        System.err.println("No config file found");
      }
    } catch (IOException e) {
      System.err.println("failed to load config" + e.getMessage());
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
        }
      }
    }
  }

  public void getFibNxPathFromUser() {
    String nxPath;
    final JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fc.showOpenDialog(null);
    nxPath = fc.getSelectedFile().toString() + PATH_TO_IMPORTS;
    try {
      Files.createDirectories(writableConfigPath.getParent());
      OutputStream outputStream = Files.newOutputStream(writableConfigPath);
      configProps.setProperty(FIB_NX_PATH_KEY, nxPath);
      configProps.store(outputStream, "Updated NX Path");
      outputStream.close();
    } catch (IOException e) {
      System.err.println(e);
    }

    setFibNxPath(nxPath);
  }

  public void getOutputPathFromUser() {
    String outputPath;
    final JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fc.showOpenDialog(null);
    outputPath = fc.getSelectedFile().toString();
    try {
      Files.createDirectories(writableConfigPath.getParent());
      OutputStream outputStream = Files.newOutputStream(writableConfigPath);
      configProps.setProperty(OUTPUT_PATH_KEY, outputPath);
      configProps.store(outputStream, "Updated Output Path");
      outputStream.close();
    } catch (IOException e) {
      System.err.println(e);
    }
    setOutputPath(outputPath);
  }
}
