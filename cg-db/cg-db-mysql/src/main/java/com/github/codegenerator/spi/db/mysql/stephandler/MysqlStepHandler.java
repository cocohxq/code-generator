package com.github.codegenerator.spi.db.mysql.stephandler;

import com.github.codegenerator.common.em.DbEnum;
import com.github.codegenerator.common.in.model.db.Column;
import com.github.codegenerator.common.in.model.db.Table;
import com.github.codegenerator.spi.db.common.stephandler.DbStepHandler;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MysqlStepHandler extends DbStepHandler {


    private final static String TABLE_NAME = "TABLE_NAME";
    private final static String TABLE_COMMENT = "TABLE_COMMENT";
    private final static String COLUMN_NAME = "COLUMN_NAME";
    private final static String COLUMN_TYPE = "COLUMN_TYPE";
    private final static String COLUMN_COMMENT = "COLUMN_COMMENT";
    private final static String IS_NULLABLE = "IS_NULLABLE";
    private final static String COLUMN_DEFAULT = "COLUMN_DEFAULT";
    private final static String NULLABLE_YES = "YES";


    @Override
    public int getDbType() {
        return DbEnum.DB_MYSQL.getType();
    }

    @Override
    public String getJdbcUrl(String ip, String port, String dbName) {
        return String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE&useSSL=false&connectTimeout=180000&allowMultiQueries=true", ip, port, dbName);
    }

    @Override
    public Class convertType(String columnType) throws Exception {
        columnType = columnType.toUpperCase();
        if (columnType.startsWith("VARCHAR") || columnType.startsWith("CHAR") || columnType.contains("TEXT") || columnType.startsWith("VAR") || columnType.startsWith("JSON")) {
            return String.class;
        } else if (columnType.startsWith("INT") || columnType.startsWith("TINYINT") || columnType.startsWith("MEDIUMINT") || columnType.startsWith("SMALLINT")) {
            return Integer.class;
        } else if (columnType.startsWith("BIGINT")) {
            return Long.class;
        } else if (columnType.startsWith("DATETIME") || columnType.startsWith("TIMESTAMP") || columnType.startsWith("DATE")) {
            return Date.class;
        } else if (columnType.startsWith("DECIMAL") || columnType.startsWith("FLOAT") || columnType.startsWith("DOUBLE")) {
            return BigDecimal.class;
        } else if (columnType.startsWith("BIT")) {
            return Boolean.class;
        } else if (columnType.startsWith("BLOB")) {
            return byte[].class;
        } else {
            throw new RuntimeException("数据库字段类型无法匹配java类型，字段类型:" + columnType);
        }
    }


    @Override
    public List<Table> getTables(Connection cn, String dbName) throws Exception {
        ResultSet rs = null;
        try {
            rs = cn.createStatement().executeQuery(String.format("select TABLE_NAME,TABLE_COMMENT from INFORMATION_SCHEMA.TABLES where table_schema='%s'", dbName));
            List<Table> list = new ArrayList<>();
            while (rs.next()) {
                Table table = new Table();
                table.setTableName(rs.getString(TABLE_NAME));
                table.setComment(rs.getString(TABLE_COMMENT));
                list.add(table);
            }
            return list;
        } finally {
            try {
                if (null != rs) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    @Override
    public List<Column> getColumns(Connection cn, String dbName, String tableName) throws Exception {
        ResultSet rs = cn.createStatement().executeQuery(String.format("select COLUMN_NAME,COLUMN_DEFAULT,IS_NULLABLE,COLUMN_TYPE,COLUMN_COMMENT from INFORMATION_SCHEMA.COLUMNS where table_schema='%s' and table_name='%s'; ", dbName, tableName));
        List<Column> list = new ArrayList<>();
        while (rs.next()) {
            Column column = new Column();
            column.setColumnName(rs.getString(COLUMN_NAME));
            column.setColumnType(rs.getString(COLUMN_TYPE));
            column.setComment(rs.getString(COLUMN_COMMENT));
            column.setColumnDefault(rs.getString(COLUMN_DEFAULT));
            column.setNullable(NULLABLE_YES.equals(rs.getString(IS_NULLABLE)));
            list.add(column);
        }
        return list;
    }
}
