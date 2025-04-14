package com.importgenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvBuilder {
  public static void createCsv(String[] columnNames, String[][] data) {
    System.out.println("creatingtable");
    createDir();
    File file = getFile();
    String fileContent = buildFileContent(columnNames, data);
    writeToFile(file, fileContent);
  }

  private static void createDir() {
    File dir = new File("~/Documents/ImportFiles");
    if (!dir.exists()) {
      dir.mkdirs();
    }
  }

  private static File getFile() {
    File file;
    String filePath;
    int attempts = 0;
    do {
      filePath = "Test" + (attempts > 0 ? attempts : "") + ".csv";
      file = new File(filePath);
      attempts++;
    } while (file.exists());

    try {
      if (file.createNewFile()) {
        System.out.println("File Created");
      } else {
        System.out.println("Error creating file");
      }
    } catch (IOException e) {
      System.out.println("An error occured");
      e.printStackTrace();
    }
    return file;
  }

  private static String buildFileContent(String[] columnNames, String[][] data) {
    String fileContent = "";
    for (int i = 0; i < columnNames.length; i++) {
      fileContent += columnNames[i] + "|";
    }
    fileContent += "\n";
    for (int j = 0; j < data.length; j++) {
      for (int k = 0; k < data[j].length - 1; k++) {
        fileContent += data[j][k] + "|";
      }
      if (j < data.length - 1) {
        fileContent += "\n";
      }
    }
    return fileContent;
  }

  private static void writeToFile(File file, String content) {
    try {
      FileWriter writer = new FileWriter(file);
      writer.write(content);
      writer.close();
      System.out.println(content);
    } catch (IOException e) {
      System.out.println("An error occured");
      e.printStackTrace();
    }
  }
}
