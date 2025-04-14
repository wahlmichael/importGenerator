package com.importgenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.*;

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

    ImportType[] importTypes = getTypesFromFibImports(fibImports);
    setImportTypes(importTypes);
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
    for (int i = 0; i < importTypes.length; i++) {
      try {
        JsonNode root = mapper.readTree(new File(fibNxPath + "/" + fileNames.get(i)));
        String importName = root.path("Name").path("en").asText();
        JsonNode importColumnElements = root.path("ImportColumnList").path("ValueObjectList");
        Iterator<JsonNode> importColumnsIterator = root.path("ImportColumnList").path("ValueObjectList").elements();
        int numColumns = getJsonNodeIteratorSize(importColumnElements.elements());
        Field[] importFields = new Field[numColumns];
        for (int j = 0; j < numColumns; j++) {
          JsonNode importColumnAttributeNode = importColumnsIterator.next().path("ValueObject")
              .path("ImportColumnAttribute");
          String columnName = importColumnAttributeNode.path("Name").path("en").toString();
          columnName = removeQuotesFromJsonString(columnName);
          String columnCode = importColumnAttributeNode.path("Code").toString();
          columnCode = removeQuotesFromJsonString(columnCode);
          String columnType = importColumnAttributeNode.path("AttributeType").toString();
          columnType = removeQuotesFromJsonString(columnType);
          Boolean isRequired = importColumnAttributeNode.path("Required").asBoolean();
          String defaultValue = importColumnAttributeNode.path("DefaultValue").toString();
          defaultValue = removeQuotesFromJsonString(defaultValue);
          Field column = new Field(columnName, columnCode, columnType, isRequired, defaultValue);
          importFields[j] = column;
        }
        ImportType fullImport = new ImportType(importName, importFields);
        importTypes[i] = fullImport;
      } catch (IOException e) {
        System.err.println(e);
      }
    }
    return importTypes;
  }

  private int getJsonNodeIteratorSize(Iterator<JsonNode> iterator) {
    int i = 0;
    while (iterator.hasNext()) {
      i++;
      iterator.next();
    }
    return i;
  }

  private String removeQuotesFromJsonString(String string) {
    String removedQuotesStr;
    if (string.length() > 0 && string.startsWith("\"")) {
      removedQuotesStr = string.substring(1, string.length() - 1);
    } else {
      removedQuotesStr = string;
    }
    return removedQuotesStr;
  }
}
