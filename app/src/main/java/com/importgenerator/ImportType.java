package com.importgenerator;

class ImportType {
  private String name;
  private Field[] fields;

  ImportType(String importName, Field[] importFields) {
    name = importName;
    fields = importFields;
  }

  String getName() {
    return name;
  }

  Field[] getFields() {
    return fields;
  }

  public String toString() {
    return getName();
  }
}
