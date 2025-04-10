package com.importgenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CsvBuilder {
  public static void createCsv(String[] columnNames, String[][] data) {
    System.out.println("creatingtable");
    createDir();
    try {
      File file = new File("~/Documents/ImportFiles/Test.csv");
      Path curRelPath = Paths.get("");
      System.out.println(curRelPath.toString());
      String s = curRelPath.toAbsolutePath().toString();
      System.out.println(s);
      if (file.createNewFile()) {
        System.out.println("File created");
      } else {
        System.out.println("already exists");
      }
    } catch (IOException e) {
      System.out.println("an error occured");
      e.printStackTrace();
    }
  }

  private static void createDir() {
    File dir = new File("~/Documents/ImportFiles");
    if (!dir.exists()) {
      dir.mkdirs();
    }
  }
}
