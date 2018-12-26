package com.github.codegenerator.common.in.model.db;

public class FieldMeta {

    private String fieldName;

    private String  fieldType;

    private Column column;

    //orm框架中对应的常量值  比如时间now(),deleted的1
    private String ormConstantValue;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }
}
