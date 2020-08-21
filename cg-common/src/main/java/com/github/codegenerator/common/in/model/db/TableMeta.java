package com.github.codegenerator.common.in.model.db;

import java.util.List;

public class TableMeta {

    //首字母小写驼峰命名
    private String tableCamelNameMin;
    //首字母大写驼峰命名
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
