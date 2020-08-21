package com.github.codegenerator.common.in.model;

import java.util.List;

/**
 * 表与表对应变量的对象
 */
public class CodeConfigInfo {

    /**
     * groupId
     */
    private String groupId;
    /**
     * 驼峰名（首字母大写）
     */
    private String tableCamelNameMax;
    /**
     * 驼峰名(首字母小写)
     */
    private String tableCamelNameMin;
    /**
     * java文件的imports
     */
    private List<String> javaImports;

    /**
     * 应用名
     */
    private String appId;
    /**
     * 代码输出类型
     */
    private transient Integer codeLocationType;

    /**
     * 业务包外置
     */
    private transient String outBusiPack;
    /**
     * 业务包内置
     */
    private transient String inBusiPack;


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTableCamelNameMax() {
        return tableCamelNameMax;
    }

    public void setTableCamelNameMax(String tableCamelNameMax) {
        this.tableCamelNameMax = tableCamelNameMax;
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

    public Integer getCodeLocationType() {
        return codeLocationType;
    }

    public void setCodeLocationType(Integer codeLocationType) {
        this.codeLocationType = codeLocationType;
    }

    public String getOutBusiPack() {
        return outBusiPack;
    }

    public void setOutBusiPack(String outBusiPack) {
        this.outBusiPack = outBusiPack;
    }

    public String getInBusiPack() {
        return inBusiPack;
    }

    public void setInBusiPack(String inBusiPack) {
        this.inBusiPack = inBusiPack;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
