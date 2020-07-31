package com.github.codegenerator.common.in.model.db;

public class FieldMeta {

    private String fieldCamelNameMin;
    private String fieldCamelNameMax;

    private String  fieldType;

    private Class fieldClazz;

    private Column column;

    public String getFieldCamelNameMin() {
        return fieldCamelNameMin;
    }

    public void setFieldCamelNameMin(String fieldCamelNameMin) {
        this.fieldCamelNameMin = fieldCamelNameMin;
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

    public String getFieldCamelNameMax() {
        return fieldCamelNameMax;
    }

    public void setFieldCamelNameMax(String fieldCamelNameMax) {
        this.fieldCamelNameMax = fieldCamelNameMax;
    }

    public Class getFieldClazz() {
        return fieldClazz;
    }

    public void setFieldClazz(Class fieldClazz) {
        this.fieldClazz = fieldClazz;
    }
}
