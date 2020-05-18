package com.github.codegenerator.common.in.model;

import java.util.List;

public class Config {

    //当前提交step
    private transient Integer stepType;

    //db配置
    private String configName;
    private String ip;
    private String port;
    private String username;
    private String pwd;
    private String dbName;
    private Integer dbType;
    private transient String operation;

    //代码配置
    private transient String groupId;
    private transient String outBusiPack;
    private transient String inBusiPack;
    private transient Integer codeLocationType;
    private transient String createTimeStr;
    private transient String updateTimeStr;
    private transient String deleteStr;
    private transient String extendStr;


    //表选择
    private transient List<String> tableNames;

    //模板
    private transient List<String> tmps;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public Integer getDbType() {
        return dbType;
    }

    public void setDbType(Integer dbType) {
        this.dbType = dbType;
    }

    public Integer getStepType() {
        return stepType;
    }

    public void setStepType(Integer stepType) {
        this.stepType = stepType;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
    }

    public List<String> getTmps() {
        return tmps;
    }

    public void setTmps(List<String> tmps) {
        this.tmps = tmps;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public Integer getCodeLocationType() {
        return codeLocationType;
    }

    public void setCodeLocationType(Integer codeLocationType) {
        this.codeLocationType = codeLocationType;
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

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getExtendStr() {
        return extendStr;
    }

    public void setExtendStr(String extendStr) {
        this.extendStr = extendStr;
    }
}
