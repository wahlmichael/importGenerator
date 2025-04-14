package com.importgenerator;

class Field {
  String name;
  String code;
  String type;
  Boolean isRequired;
  String defaultValue;

  Field(String name, String code, String type, Boolean isRequired, String defaultValue) {
    this.name = name;
    this.code = code;
    this.type = type;
    this.isRequired = isRequired;
    this.defaultValue = defaultValue;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Boolean getIsRequired() {
    return this.isRequired;
  }

  public String getDefaultValue() {
    return defaultValue;
  }
}
