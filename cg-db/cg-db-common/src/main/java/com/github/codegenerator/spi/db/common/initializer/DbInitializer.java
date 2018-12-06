package com.github.codegenerator.spi.db.common.initializer;

import com.github.codegenerator.common.em.DbEnum;
import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.in.model.db.*;
import com.github.codegenerator.common.spi.initializer.AbstractInitializer;
import com.github.codegenerator.common.util.LogUtils;
import com.github.codegenerator.spi.db.common.util.BuildUtils;
import com.github.codegenerator.spi.db.common.util.DbUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class DbInitializer extends AbstractInitializer {

    private final static String TABLE_NAME = "TABLE_NAME";
    private final static String TABLE_COMMENT = "REMARKS";
    private final static String COLUMN_NAME = "COLUMN_NAME";
    private final static String COLUMN_TYPE = "TYPE_NAME";
    private final static String COLUMN_COMMENT = "REMARKS";

    @Override
    public boolean before(SessionGenerateContext context) {
        Config config = context.getConfig();
        if(null == config.getDbType() || !config.getDbType().equals(getDbType())){
            return false;
        }
        if(null == config.getIp() || null == config.getPort() || null == config.getDbName()){
            throw new RuntimeException("ip、port、dbName名称存在空值");
        }
        //构建信息重建
        context.getGenerateInfo().setGroupId(null);
        context.getGenerateInfo().setSelectDbType(null);
        context.getGenerateInfo().setDatabase(null);
        return true;
    }

    @Override
    public void doInitialize(SessionGenerateContext context) {
        ResultSet tablesSet = null;
        Connection cn = null;
        Statement tabStmt = null;
        Statement colStmt = null;
        ResultSet columnsRet = null;
        try {
            Config cfg = context.getConfig();
            Database db = new Database(DbEnum.getDbByType(getDbType()).getDriver(),cfg.getUsername(),cfg.getPwd(),getJdbcUrl(cfg.getIp(),cfg.getPort(),cfg.getDbName()),cfg.getDbName());
            cn = DbUtils.getConnection(db);

            DatabaseMetaData metaData = cn.getMetaData();
            //提取表
            tablesSet = metaData.getTables(null,"%","%",new String[]{"TABLE"});

            List<TableMeta> tableList = new ArrayList<>();
            while (tablesSet.next()){
                Table table = getTable(tablesSet,db.getDbName());
                if(null == table || null == table.getTableName() || "".equals(table.getTableName())){
                    throw new RuntimeException("can`t find tables in database "+cfg.getDbName());
                }
                //提取表对应的所有列
                columnsRet =  metaData.getColumns(null,"%",table.getTableName(),"%");
                List<Column> columns = getColumns(columnsRet);
                if(null == columns){
                    throw new RuntimeException("table: "+table.getTableName()+"can`t find colums");
                }
                //根据表信息构建表对应的对象信息
                TableMeta tableMeta = new TableMeta();
                tableList.add(tableMeta);
                tableMeta.setTable(table);
                tableMeta.setTableCamelNameMin(BuildUtils.conver2CameltName(table.getTableName()));//表名转换为驼峰命名
                List<FieldMeta> list = new ArrayList<>(columns.size());
                tableMeta.setFields(list);
                //这里不把id放入field数组
                columns.stream().filter(l -> !l.getColumnName().equalsIgnoreCase("id")).forEach(l -> {
                    FieldMeta fieldMeta = new FieldMeta();
                    fieldMeta.setColumn(l);
                    fieldMeta.setFieldName(BuildUtils.conver2CameltName(l.getColumnName()));
                    fieldMeta.setFieldType(convertType(l.getColumnType()));
                    list.add(fieldMeta);
                });
            }
            db.setTableList(tableList);

            context.getGenerateInfo().setGroupId(context.getConfig().getGroupId());
            context.getGenerateInfo().setSelectDbType(context.getConfig().getDbType());
            context.getGenerateInfo().setDatabase(db);
            context.setStepInitResult(db);
        } catch (Exception e) {
            LogUtils.error("initiailze db error",e,this.getClass());
        }finally {
            try {
                if(null != tablesSet){
                    tablesSet.close();
                }
                if(null != columnsRet){
                    columnsRet.close();
                }
                if(null != tabStmt){
                    tabStmt.close();
                }
                if(null != colStmt){
                    colStmt.close();
                }
                if(null != cn){
                    cn.close();
                }
            }catch (Exception e1){
                LogUtils.error("close db error",e1,this.getClass());
            }
        }
    }


    @Override
    public void after(SessionGenerateContext context) {
    }

    /**
     * db类型
     * @return
     */
    public abstract int getDbType();

    public abstract String getJdbcUrl(String ip,String port,String dbName);


    /**
     * 获取表名
     * @return
     */
    public Table getTable(ResultSet curTable,String dbName){
        try {
            Table table = new Table();
            table.setTableName(curTable.getString(TABLE_NAME));
            table.setComment(curTable.getString(TABLE_COMMENT));
            return table;
        } catch (Exception e) {
            LogUtils.error("initiailze db getTableName error",e,this.getClass());
            return null;
        }
    }

    /**
     * 获取列
     * @param
     * @return
     */
    public List<Column> getColumns(ResultSet allColums){
        try {
            List<Column> list = new ArrayList<>();
            while(allColums.next()){
                Column column = new Column();
                column.setColumnName(allColums.getString(COLUMN_NAME));
                column.setColumnType(allColums.getString(COLUMN_TYPE));
                column.setComment(allColums.getString(COLUMN_COMMENT));
                list.add(column);
            }
            return list;
        } catch (Exception e) {
            LogUtils.error("initiailze db getColumns error",e,this.getClass());
        }
        return null;
    }

    /**
     * 根据数据列数据类型来转换java类型
     * @param columnType
     * @return
     */
    public abstract String convertType(String columnType);

    @Override
    public Integer getStepType() {
        return StepEnum.STEP_DB.getType();
    }


    /*

    原始的获取元数据的方法

    tabStmt = cn.createStatement();
            tablesSet = tabStmt.executeQuery(sqlQueryTables());

            List<TableMeta> tableList = new ArrayList<>();
            while (tablesSet.next()){
                String tableName = getTableName(tablesSet,db.getDbName());
                if(null == tableName || "".equals(tableName)){
                    throw new RuntimeException("can`t find tables in database "+cfg.getDbName());
                }
                Table table = new Table();
                table.setTableName(tableName);
                colStmt = cn.createStatement();
                columnsRet =  colStmt.executeQuery(sqlQueryTableColumns(tableName));
                List<Column> columns = getColumns(columnsRet);
                if(null == columns){
                    throw new RuntimeException("table: "+tableName+"can`t find colums");
                }
                table.setColumnList(columns);
                //根据表信息构建表对应的对象信息
                TableMeta tableMeta = new TableMeta();
                tableList.add(tableMeta);
                tableMeta.setTable(table);
                tableMeta.setTableCamelNameMin(BuildUtils.conver2CameltName(tableName));//表名转换为驼峰命名
                List<FieldMeta> list = new ArrayList<>(columns.size());
                tableMeta.setFields(list);
                columns.stream().forEach(l -> {
                    FieldMeta fieldMeta = new FieldMeta();
                    fieldMeta.setColumn(l);
                    fieldMeta.setFieldName(BuildUtils.conver2CameltName(l.getColumnName()));
                    fieldMeta.setFieldType(convertType(l.getColumnType()));
                    list.add(fieldMeta);
                });
            }
            db.setTableList(tableList);
            context.setDatabase(db);

     */
}
