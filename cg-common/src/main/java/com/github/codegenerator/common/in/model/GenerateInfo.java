package com.github.codegenerator.common.in.model;

import com.github.codegenerator.common.in.model.db.Database;
import com.github.codegenerator.common.in.model.db.TableMeta;

import java.util.HashMap;
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
     * 所有最后选中的模板  key:tmp package路径 module/...  module存放的路径   value：tmp
     */
    private Map<String,TemplateInfo> selectedTmps = new HashMap<>();

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

    public Map<String, TemplateInfo> getSelectedTmps() {
        return selectedTmps;
    }

    public void setSelectedTmps(Map<String, TemplateInfo> selectedTmps) {
        this.selectedTmps = selectedTmps;
    }

    public String getSelectedTmpTreeName() {
        return selectedTmpTreeName;
    }

    public void setSelectedTmpTreeName(String selectedTmpTreeName) {
        this.selectedTmpTreeName = selectedTmpTreeName;
    }
}
