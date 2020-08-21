package com.github.codegenerator.common.in.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 表和模板关联的对象
 */
public class TableCodeTemplateInfo {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private GenerateInfo generateInfo;

    /**
     * ${tableCamelNameMax}DTO.java.ftl
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
     * ${tableCamelNameMax}DTO
     */
    private String javaClassName;

    /**
     * 生成的代码文件名
     * ${tableCamelNameMax}DTO.java
     */
    private String targetFileName;


    /**
     * 生成的代码文件最终存放路径
     * a/b/c/d/${tableCamelNameMax}DTO.java
     */
    private String targetFilePath;

    public TableCodeTemplateInfo() {
    }

    //Object转Map
    public static Map<String, Object> getObjectToMap(Object obj, String... ignoreFields) throws IllegalAccessException {
        Set<String> ignoreSet = null != ignoreFields ? Arrays.stream(ignoreFields).collect(Collectors.toSet()):new HashSet<>();
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if(ignoreSet.contains(field.getName())){
                continue;
            }
            field.setAccessible(true);
            String fieldCamelNameMin = field.getName();
            Object value = field.get(obj);
            if (value == null) {
                value = "";
            }
            map.put(fieldCamelNameMin, value);
        }
        return map;
    }

    public Map<String, Object> getTmpValMap() {
        try {
            Map<String, Object> valMap = getObjectToMap(this.generateInfo.getCodeConfigInfo());
            valMap.putAll(getObjectToMap(this.generateInfo.getDatabase(),"tableList"));
            valMap.putAll(getObjectToMap(this.generateInfo.getTableConfigInfo()));
            valMap.put("commonValueStack", this.generateInfo.getCommonValueStack());
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


    public GenerateInfo getGenerateInfo() {
        return generateInfo;
    }

    public void setGenerateInfo(GenerateInfo generateInfo) {
        this.generateInfo = generateInfo;
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
