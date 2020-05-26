package com.github.codegenerator.common.in.model.db;

import java.util.List;

public class TableMeta {

    private String tableCamelNameMin;

    private String tableCamelNameMax;

    private List<FieldMeta> fields;

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
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getTableCamelNameMax() {
        return tableCamelNameMax;
    }

    public void setTableCamelNameMax(String tableCamelNameMax) {
        this.tableCamelNameMax = tableCamelNameMax;
    }
}
