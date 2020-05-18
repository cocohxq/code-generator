package com.github.codegenerator.common.in.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 表和模板关联的对象
 */
public class TableCodeTemplateInfo {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private TableCodeInfo tableCodeInfo;

    /**
     * ${tableCamelName}DTO.java.ftl
     */
    private String templateFileName;

    /**
     * class模板
     */
    private String templateContent;

    /**
     * java包名
     * a.b.c.d
     */
    private String javaPackage;
    /**
     * java类名
     * ${tableCamelName}DTO
     */
    private String javaClassName;

    /**
     * 生成的代码文件名
     * ${tableCamelName}DTO.java
     */
    private String targetFileName;


    /**
     * 生成的代码文件最终存放路径
     * a/b/c/d/${tableCamelName}DTO.java
     */
    private String targetFilePath;

    public TableCodeTemplateInfo() {
    }

    //Object转Map
    public static Map<String, Object> getObjectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            if (value == null) {
                value = "";
            }
            map.put(fieldName, value);
        }
        return map;
    }

    public Map<String, Object> getTmpValMap(GenerateInfo info) {
        try {
            Map<String, Object> valMap = getObjectToMap(this.getTableCodeInfo());
            valMap.put("commonValueStack", info.getCommonValueStack());
            valMap.put("javaPackage", this.getJavaPackage());
            valMap.put("javaClassName", this.getJavaClassName());
            return valMap;
        } catch (IllegalAccessException e) {
            logger.error("error", e);
        }
        return null;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getTemplateFileName() {
        return templateFileName;
    }

    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
    }

    public TableCodeInfo getTableCodeInfo() {
        return tableCodeInfo;
    }

    public void setTableCodeInfo(TableCodeInfo tableCodeInfo) {
        this.tableCodeInfo = tableCodeInfo;
    }

    public String getJavaPackage() {
        return javaPackage;
    }

    public void setJavaPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

    public String getJavaClassName() {
        return javaClassName;
    }

    public void setJavaClassName(String javaClassName) {
        this.javaClassName = javaClassName;
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    public void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    public String getTargetFilePath() {
        return targetFilePath;
    }

    public void setTargetFilePath(String targetFilePath) {
        this.targetFilePath = targetFilePath;
    }
}
