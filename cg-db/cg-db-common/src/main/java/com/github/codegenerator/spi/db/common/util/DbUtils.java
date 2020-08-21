package com.github.codegenerator.spi.db.common.util;


import com.github.codegenerator.common.in.model.db.Database;

import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;

public class DbUtils {


    public static Connection getConnection(Database db) throws Exception {
        Driver driver = (Driver) Class.forName(db.getDriverName()).newInstance();
        Properties info = new Properties();
        info.put("user", db.getUserName());
        info.put("password", db.getPassword());
        Connection connection = driver.connect(db.getUrl(), info);
        return connection;
    }
}
