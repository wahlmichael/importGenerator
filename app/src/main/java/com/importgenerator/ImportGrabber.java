package com.importgenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.*;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImportGrabber {
  private ImportType[] importTypes;
  private String fibNxPath;

  ImportGrabber(String fibNxPath) {
    this.fibNxPath = fibNxPath;
    createImportTypes();
  }

  public ImportType[] getImportTypes() {
    return this.importTypes;
  }

  private void setImportTypes(ImportType[] importTypes) {
    this.importTypes = importTypes;
  }

  private void createImportTypes() {
    List<String> fibImports;
    try {
      fibImports = getFibImports();
    } catch (IOException e) {
      System.err.println("cannot find fib nx directory");
      System.err.println(e);
      return;
    }

    getTypesFromFibImports(fibImports);

    Field field1 = new Field("Primary Identifier", "Number");
    Field field2 = new Field("Client Identifier", "String");
    Field field3 = new Field("Primary Identifier", "Number");
    Field field4 = new Field("Program Id", "String");
    Field[] fields1 = { field1, field2 };
    Field[] fields2 = { field3, field4 };
    ImportType type1 = new ImportType("RAZR Common Account Import", fields1);
    ImportType type2 = new ImportType("RAZR Common Person Import", fields2);
    ImportType[] types = { type1, type2 };
    setImportTypes(types);
  }

  private List<String> getFibImports() throws IOException {
    System.out.println(String.format("Getting files from %s", fibNxPath));
    List<String> allFiles;
    try (Stream<Path> stream = Files.list(Paths.get(fibNxPath))) {
      allFiles = stream
          .filter(file -> !Files.isDirectory(file))
          .map(Path::getFileName)
          .map(Path::toString)
          .filter(path -> path.startsWith("DataImport.DelimitedImportForm"))
          .collect(Collectors.toList());
    }
    return allFiles;
  }

  private ImportType[] getTypesFromFibImports(List<String> fileNames) {
    ImportType[] importTypes = new ImportType[fileNames.size()];
    ObjectMapper mapper = new ObjectMapper();
    for (int i = 0; i < 1; i++) {
      try {
        JsonNode root = mapper.readTree(new File(fibNxPath + "/" + fileNames.get(i)));
        String importName = root.path("Name").path("en").asText();
        // String jsonImportColumnListString =
        // root.path("ImportColumnList").path("ValueObjectList").toString();
        String jsonImportColumnListString = "[{\"Name\":\"a test\", \"AttributeType\":\"tattribute\"}]";
        List<Field> columns = mapper.readValue(jsonImportColumnListString, new TypeReference<List<Field>>() {
        });
        System.out.println(columns);
        Field[] importFields;

        System.out.println(importName);
      } catch (IOException e) {
        System.err.println(e);
      }
    }
    return importTypes;
  }
}
