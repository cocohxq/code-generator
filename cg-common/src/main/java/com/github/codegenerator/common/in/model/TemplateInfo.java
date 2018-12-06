package com.github.codegenerator.common.in.model;

import java.util.List;
import java.util.Map;

public class TemplateInfo {

    private String templateFileName;

    /**
     * class模板
     */
    private String templateContent;
    /**
     * 模板内容,每张表的信息
     */
    private List<Map<String, Object>> tableContentList;


    public TemplateInfo() {
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public List<Map<String, Object>> getTableContentList() {
        return tableContentList;
    }

    public void setTableContentList(List<Map<String, Object>> tableContentList) {
        this.tableContentList = tableContentList;
    }

    public String getTemplateFileName() {
        return templateFileName;
    }

    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
    }
}
