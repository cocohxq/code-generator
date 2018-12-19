package com.github.codegenerator.common.in.model.db;

import java.util.List;
import java.util.stream.Collectors;

public class TableMeta {

    private String tableCamelNameMin;

    private List<FieldMeta> fields;
    private List<FieldMeta> noIdFields;

    private Table table;


    public String getTableCamelNameMin() {
        return tableCamelNameMin;
    }

    public void setTableCamelNameMin(String tableCamelNameMin) {
        this.tableCamelNameMin = tableCamelNameMin;
    }

    public List<FieldMeta> getFields() {
        return fields;
    }

    public void setFields(List<FieldMeta> fields) {
        this.fields = fields;
        this.noIdFields = fields.stream().filter(l -> !l.getFieldName().equalsIgnoreCase("id")).collect(Collectors.toList());
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<FieldMeta> getNoIdFields() {
        return noIdFields;
    }

}
