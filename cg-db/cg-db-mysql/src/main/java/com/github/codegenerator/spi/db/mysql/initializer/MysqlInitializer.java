package com.github.codegenerator.spi.db.mysql.initializer;

import com.github.codegenerator.common.em.DbEnum;
import com.github.codegenerator.common.in.model.db.Column;
import com.github.codegenerator.common.in.model.db.Table;
import com.github.codegenerator.common.util.LogUtils;
import com.github.codegenerator.spi.db.common.initializer.DbInitializer;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MysqlInitializer extends DbInitializer {



    @Override
    public int getDbType() {
        return DbEnum.DB_MYSQL.getType();
    }

    @Override
    public String getJdbcUrl(String ip, String port, String dbName) {
        return String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE&useSSL=false",ip,port,dbName);
    }

    @Override
    public String convertType(String columnType) {
        if (columnType.startsWith("VARCHAR") || columnType.startsWith("CHAR") || columnType.startsWith("TEXT")) {
            return "String";
        } else if (columnType.startsWith("INT") || columnType.startsWith("TINYINT")) {
            return "Integer";
        } else if (columnType.startsWith("BIGINT")) {
            return "Long";
        } else if (columnType.startsWith("DATETIME") || columnType.startsWith("TIMESTAMP") || columnType.startsWith("DATE")) {
            return "Date";
        } else if (columnType.startsWith("DECIMAL")) {
            return "BigDecimal";
        } else if (columnType.startsWith("FLOAT") || columnType.startsWith("DOUBLE")) {
            return "Double";
        }else{
            LogUtils.error("initiailze db convertType error",null,this.getClass());
            throw new RuntimeException("类型无法匹配");
        }
    }
}
