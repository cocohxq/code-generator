package com.github.codegenerator.common.em;

public enum DbEnum implements TupleEnum {

    DB_MYSQL(1, "mysql", "mysql", "com.mysql.jdbc.Driver"), DB_ORACLE(2, "oracle", "oracle", "oracle.jdbc.driver.OracleDriver"), DB_MONGO(3, "mongo", "mongo", null);

    private Integer type;
    private String name;
    private String driver;
    private String code;

    DbEnum(int type, String name, String code, String driver) {
        this.type = type;
        this.name = name;
        this.code = code;
        this.driver = driver;
    }

    public static DbEnum getDbByType(int type) {
        for (DbEnum db : DbEnum.values()) {
            if (type == db.getType()) {
                return db;
            }
        }
        return null;
    }

    @Override
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

}
