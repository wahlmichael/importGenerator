package com.importgenerator;

class Field {
  String name;
  String type;

  Field(String fieldName, String fieldType) {
    name = fieldName;
    type = fieldType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
