package com.github.codegenerator.common.in.model;

import com.github.codegenerator.common.in.model.db.Database;
import com.github.codegenerator.common.in.model.db.TableMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenerateInfo {

    //代码最终存放路径
    private String codepath;

    /**
     * 表映射成的java对象信息
     */
    private Database database;

    /**
     * 选中的表
     */
    private List<TableMeta> selectedTables;

    /**
     * 表解析的模板对应变量,每张表的信息   类名、类字段、类imports等
     */
    private List<TableCodeInfo> tableCodeInfoList;


    /**
     * 表结合模板解析的相关信息  文件名、路径等
     */
    private List<TableCodeTemplateInfo> tableCodeTemplateInfoList = new ArrayList<>();

    /**
     * 选中的模板树名称
     */
    private String selectedTmpTreeName;

    /**
     * 公共属性
     */
    private CommonValueStack commonValueStack = new CommonValueStack();

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public List<TableMeta> getSelectedTables() {
        return selectedTables;
    }

    public void setSelectedTables(List<TableMeta> selectedTables) {
        this.selectedTables = selectedTables;
    }

    public String getCodepath() {
        return codepath;
    }

    public void setCodepath(String codepath) {
        this.codepath = codepath;
    }

    public CommonValueStack getCommonValueStack() {
        return commonValueStack;
    }

    public void setCommonValueStack(CommonValueStack commonValueStack) {
        this.commonValueStack = commonValueStack;
    }

    public List<TableCodeTemplateInfo> getTableCodeTemplateInfoList() {
        return tableCodeTemplateInfoList;
    }

    public void setTableCodeTemplateInfoList(List<TableCodeTemplateInfo> tableCodeTemplateInfoList) {
        this.tableCodeTemplateInfoList = tableCodeTemplateInfoList;
    }

    public String getSelectedTmpTreeName() {
        return selectedTmpTreeName;
    }

    public void setSelectedTmpTreeName(String selectedTmpTreeName) {
        this.selectedTmpTreeName = selectedTmpTreeName;
    }

    public List<TableCodeInfo> getTableCodeInfoList() {
        return tableCodeInfoList;
    }

    public void setTableCodeInfoList(List<TableCodeInfo> tableCodeInfoList) {
        this.tableCodeInfoList = tableCodeInfoList;
    }
}
