package com.github.codegenerator.common.in.model;

import com.github.codegenerator.common.in.model.db.Database;

import java.util.ArrayList;
import java.util.List;

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
    private TableConfigInfo tableConfigInfo;

    /**
     * 表解析的模板对应变量,每张表的信息   类名、类字段、类imports等
     */
    private CodeConfigInfo codeConfigInfo;


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

    public TableConfigInfo getTableConfigInfo() {
        return tableConfigInfo;
    }

    public void setTableConfigInfo(TableConfigInfo tableConfigInfo) {
        this.tableConfigInfo = tableConfigInfo;
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

    public CodeConfigInfo getCodeConfigInfo() {
        return codeConfigInfo;
    }

    public void setCodeConfigInfo(CodeConfigInfo codeConfigInfo) {
        this.codeConfigInfo = codeConfigInfo;
    }
}
