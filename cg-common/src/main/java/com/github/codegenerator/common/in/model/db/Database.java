package com.github.codegenerator.common.in.model.db;

import java.util.List;

public class Database {


    private String ip;
    private String port;
    private String driverName;
    private String dbName;
    private String userName;
    private String password;
    private String url;
    private List<TableMeta> tableList;

    public Database() {
    }

    public Database(String ip,String port,String driverName, String userName, String password, String url, String dbName) {
        this.ip = ip;
        this.port = port;
        this.driverName = driverName;
        this.userName = userName;
        this.password = password;
        this.url = url;
        this.dbName = dbName;
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

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TableMeta> getTableList() {
        return tableList;
    }

    public void setTableList(List<TableMeta> tableList) {
        this.tableList = tableList;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}
