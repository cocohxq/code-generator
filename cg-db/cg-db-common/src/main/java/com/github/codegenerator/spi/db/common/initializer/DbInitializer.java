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

            //使用JDBC此种方式 可能会出现获取的字段类型同数据库字段类型不一致的问题 eg：db-tinyint(1) 会被读取成BIT 从而转换成boolean
            //DatabaseMetaData metaData = cn.getMetaData();
            //提取表
            //tablesSet = metaData.getTables(null,"%","%",new String[]{"TABLE"});


            List<Table> tables = getTables(cn,cfg.getDbName());


            List<TableMeta> tableList = new ArrayList<>();
            for(Table table : tables){
                if(null == table || null == table.getTableName() || "".equals(table.getTableName())){
                    throw new RuntimeException("can`t find tables in database "+cfg.getDbName());
                }
                //提取表对应的所有列
                List<Column> columns = getColumns(cn,cfg.getDbName(),table.getTableName());
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
     * 获取列
     * @param
     * @return
     */
    public abstract List<Column> getColumns(Connection cn,String dbName,String tableName);

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

    public abstract List<Table> getTables(Connection cn,String dbName);
}
