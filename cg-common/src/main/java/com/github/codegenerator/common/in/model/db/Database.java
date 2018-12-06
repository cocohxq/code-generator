package com.github.codegenerator.common.in.model.db;

import java.util.List;
import java.util.Map;

public class Database {


    private String driverName;
    private String dbName;
    private String user;
    private String password;
    private String url;
    private List<TableMeta> tableList;

    public Database() {
    }

    public Database(String driverName, String user, String password, String url,String dbName) {
        this.driverName = driverName;
        this.user = user;
        this.password = password;
        this.url = url;
        this.dbName = dbName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
