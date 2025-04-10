package com.importgenerator;

class Field {
  String name;
  String type;

  Field(String fieldName, String fieldType) {
    name = fieldName;
    type = fieldType;
  }

  String getName() {
    return name;
  }

  String getType() {
    return type;
  }
}
