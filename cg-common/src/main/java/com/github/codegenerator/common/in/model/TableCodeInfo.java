package com.github.codegenerator.common.in.model;

import com.github.codegenerator.common.in.model.db.TableMeta;

import java.util.List;

/**
 * 表与表对应变量的对象
 */
public class TableCodeInfo {

    /**
     * groupId
     */
    private String groupId;
    /**
     * 表元数据
     */
    private TableMeta tableMeta;
    /**
     * 表对应db名称
     */
    private String dbName;
    /**
     * 驼峰名（首字母大写）
     */
    private String tableCamelName;
    /**
     * 驼峰名(首字母小写)
     */
    private String tableCamelNameMin;
    /**
     * java文件的imports
     */
    private List<String> javaImports;
    /**
     * 创建时间字段
     */
    private String createTimeStr;
    /**
     * 更新时间字段
     */
    private String updateTimeStr;
    /**
     * 删除字段
     */
    private String deleteStr;

    /**
     * pojo继承字段
     */
    private String extendStr;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public TableMeta getTableMeta() {
        return tableMeta;
    }

    public void setTableMeta(TableMeta tableMeta) {
        this.tableMeta = tableMeta;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableCamelName() {
        return tableCamelName;
    }

    public void setTableCamelName(String tableCamelName) {
        this.tableCamelName = tableCamelName;
    }

    public String getTableCamelNameMin() {
        return tableCamelNameMin;
    }

    public void setTableCamelNameMin(String tableCamelNameMin) {
        this.tableCamelNameMin = tableCamelNameMin;
    }

    public List<String> getJavaImports() {
        return javaImports;
    }

    public void setJavaImports(List<String> javaImports) {
        this.javaImports = javaImports;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getUpdateTimeStr() {
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }

    public String getDeleteStr() {
        return deleteStr;
    }

    public void setDeleteStr(String deleteStr) {
        this.deleteStr = deleteStr;
    }

    public String getExtendStr() {
        return extendStr;
    }

    public void setExtendStr(String extendStr) {
        this.extendStr = extendStr;
    }
}
