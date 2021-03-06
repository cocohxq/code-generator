package com.github.codegenerator.common.in.model;

import com.github.codegenerator.common.in.model.db.TableMeta;

/**
 * 表配置对象
 */
public class TableConfigInfo {

    /**
     * 表元数据
     */
    private TableMeta tableMeta;
    /**
     * 表对应db名称
     */
    private String dbName;
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

    /**
     * pojo继承字段
     */
    private String inStr;

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

    public String getInStr() {
        return inStr;
    }

    public void setInStr(String inStr) {
        this.inStr = inStr;
    }
}
