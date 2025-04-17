package com.importgenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvBuilder {
  public static Boolean wasLastSuccess = false;

  public static void createCsv(String[] columnNames, String[][] data, String dir, String importName) {
    File file = getFile(dir, importName);
    String fileContent = buildFileContent(columnNames, data);
    writeToFile(file, fileContent);
  }

  private static File getFile(String dir, String importName) {
    File file;
    String fileName;
    int attempts = 0;
    String importNameNoSpaces = importName.replaceAll("\\s+", "");
    do {
      fileName = dir + "/" + importNameNoSpaces + (attempts > 0 ? attempts : "") + ".csv";
      file = new File(fileName);
      attempts++;
    } while (file.exists());
    System.out.println(file);
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
    String fileContent = "Header\n";
    for (int i = 0; i < columnNames.length; i++) {
      fileContent += columnNames[i];
      if (i < columnNames.length - 1) {
        fileContent += "|";
      }
    }
    fileContent += "\n";
    for (int j = 0; j < data.length; j++) {
      for (int k = 0; k < data[j].length; k++) {
        fileContent += data[j][k];
        if (k < data[j].length - 1) {
          fileContent += "|";
        } else {
          fileContent += "\n";
        }
      }
      if (j == data.length - 1) {
        fileContent += "Footer";
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
      wasLastSuccess = true;
    } catch (IOException e) {
      System.out.println("An error occured");
      e.printStackTrace();
      wasLastSuccess = false;
    }
  }
}
